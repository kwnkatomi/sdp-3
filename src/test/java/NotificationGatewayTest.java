import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationGatewayTest {

    @Test
    void emailDeliversSuccessfully() {
        EmailMessageSender email = new EmailMessageSender();

        boolean result = email.sendMessage(
                "sara@example.com", "Test", "Hello!"
        );

        assertTrue(result);
    }

    @Test
    void unavailableEmailRejectsDelivery() {
        EmailMessageSender email = new EmailMessageSender();
        email.setAvailable(false);

        boolean result = email.sendMessage(
                "sara@example.com", "Test", "Hello!"
        );

        assertFalse(result);
    }

    @Test
    void smsAcceptsExactly160Characters() {
        SmsMessageSender sms = new SmsMessageSender();

        boolean result = sms.sendMessage(
                "+77001234567", "Test", "A".repeat(160)
        );

        assertTrue(result);
    }

    @Test
    void smsRejects161Characters() {
        SmsMessageSender sms = new SmsMessageSender();

        boolean result = sms.sendMessage(
                "+77001234567", "Test", "A".repeat(161)
        );

        assertFalse(result);
    }

    @Test
    void encryptedNotificationEncodesMessageBeforeDelivery() {
        RecordingSender sender = new RecordingSender();
        Notification notification = new EncryptedNotification(sender);

        notification.send("user-1", "Hello!");

        assertEquals(List.of("user-1"), sender.attemptedRecipients);
        assertEquals("SGVsbG8h", sender.lastBody);
        assertEquals("Encrypted notification", sender.lastTitle);
    }

    @Test
    void urgentNotificationUsesPrimaryWithoutCallingFallback() {
        RecordingSender primary = new RecordingSender();
        RecordingSender fallback = new RecordingSender();

        Notification notification =
                new UrgentNotification(primary, fallback);

        notification.send("user-1", "Help, this is urgent.");

        assertEquals(List.of("user-1"), primary.attemptedRecipients);
        assertEquals("[URGENT] Help, this is urgent.", primary.lastBody);
        assertTrue(fallback.attemptedRecipients.isEmpty());
    }

    @Test
    void urgentNotificationUsesFallbackWhenPrimaryIsUnavailable() {
        RecordingSender primary = new RecordingSender();
        primary.available = false;

        RecordingSender fallback = new RecordingSender();

        Notification notification =
                new UrgentNotification(primary, fallback);

        notification.send("user-1", "Help, this is urgent.");

        assertTrue(primary.attemptedRecipients.isEmpty());
        assertEquals(List.of("user-1"), fallback.attemptedRecipients);
        assertEquals("[URGENT] Help, this is urgent.", fallback.lastBody);
    }

    @Test
    void batchCountsSuccessesAndContinuesAfterFailure() {
        RecordingSender sender = new RecordingSender();
        sender.rejectedRecipient = "bad";

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(sender);

        batch.addRecipient("user-1");
        batch.addRecipient("bad");
        batch.addRecipient("user-2");

        batch.sendBatch("Reminder!");

        assertEquals(3, batch.getTotal());
        assertEquals(2, batch.getSuccessful());
        assertEquals(1, batch.getFailed());

        assertEquals(
                List.of("user-1", "bad", "user-2"),
                sender.attemptedRecipients
        );
    }

    @Test
    void batchResetsCountersForEachRun() {
        RecordingSender sender = new RecordingSender();

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(sender);

        batch.addRecipient("user-1");

        batch.sendBatch("First message");

        assertEquals(1, batch.getSuccessful());
        assertEquals(0, batch.getFailed());

        sender.available = false;

        batch.sendBatch("Second message");

        assertEquals(1, batch.getTotal());
        assertEquals(0, batch.getSuccessful());
        assertEquals(1, batch.getFailed());
    }

    @Test
    void sameNotificationCanSwitchSendersAtRuntime() {
        RecordingSender first = new RecordingSender();
        RecordingSender second = new RecordingSender();

        Notification notification = new UrgentNotification(first);

        notification.send("user-1", "First message");

        notification.setSender(second);

        notification.send("user-2", "Second message");

        assertEquals(List.of("user-1"), first.attemptedRecipients);
        assertEquals(List.of("user-2"), second.attemptedRecipients);
        assertEquals("[URGENT] First message", first.lastBody);
        assertEquals("[URGENT] Second message", second.lastBody);
    }

    @Test
    void pushProviderDeliversSuccessfully() {
        PushNotificationSender push = new PushNotificationSender();

        boolean result = push.sendMessage(
                "device-001", "Test", "Hello!"
        );

        assertTrue(result);
    }

    @Test
    void marketingNotificationAddsPromotionAndUnsubscribeText() {
        RecordingSender sender = new RecordingSender();

        Notification notification = new MarketingNotification(sender);

        notification.send("user-1", "Get 20% off!");

        assertEquals(List.of("user-1"), sender.attemptedRecipients);
        assertEquals("Special offer", sender.lastTitle);
        assertEquals(
                "[PROMO] Get 20% off!\nReply STOP to unsubscribe.",
                sender.lastBody
        );
    }

    private static class RecordingSender implements MessageSender {

        private boolean available = true;
        private String rejectedRecipient;

        private final List<String> attemptedRecipients =
                new ArrayList<>();

        private String lastTitle;
        private String lastBody;

        @Override
        public boolean sendMessage(
                String recipient,
                String title,
                String body
        ) {
            attemptedRecipients.add(recipient);
            lastTitle = title;
            lastBody = body;

            return available && !recipient.equals(rejectedRecipient);
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public String getChannelName() {
            return "RecordingSender";
        }
    }
}