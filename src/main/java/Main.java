public class Main {

    public static void main(String[] args) {
        EmailMessageSender email = new EmailMessageSender();
        TelegramMessageSender telegram = new TelegramMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        KafkaEventSender kafka = new KafkaEventSender();

        //email.setAvailable(false);

        Notification emailNotification =
                new EncryptedNotification(email);

        Notification telegramNotification =
                new EncryptedNotification(telegram);

        Notification smsNotification =
                new EncryptedNotification(sms);

        Notification kafkaNotification =
                new EncryptedNotification(kafka);

        emailNotification.send("sara@example.com", "Hello email!");
        System.out.println();

        telegramNotification.send("@sara", "Hello telegram!");
        System.out.println();

        smsNotification.send("+77001234567", "Hello sms!");
        System.out.println();

        kafkaNotification.send("user-1", "Hello kafka!");
        System.out.println();

        System.out.println("SMS LENGTH CHECKS");

        boolean accepted160 = sms.sendMessage(
                "+77001234567",
                "Length check",
                "A".repeat(160)
        );

        boolean accepted161 = sms.sendMessage(
                "+77001234567",
                "Length check",
                "A".repeat(161)
        );

        System.out.println("160 characters accepted: " + accepted160);
        System.out.println("161 characters accepted: " + accepted161);

        System.out.println("\nURGENT NOTIFICATION THROUGH TELEGRAM");

        Notification urgent =
                new UrgentNotification(telegram, sms);

        urgent.send("user-1", "Help, this is urgent.");


        System.out.println("\nRUNTIME SWITCH TO EMAIL");

        urgent.setSender(email);

        urgent.send("sara@example.com", "Help, this is urgent.");


        System.out.println("\nTELEGRAM UNAVAILABLE");

        urgent.setSender(telegram);
        telegram.setAvailable(false);

        urgent.send("user-1", "Help, this is urgent.");


        System.out.println("\nBOTH PROVIDERS UNAVAILABLE");

        sms.setAvailable(false);

        urgent.send("user-1", "Help, this is urgent.");

        telegram.setAvailable(true);
        sms.setAvailable(true);

        System.out.println("\nBATCH THROUGH SMS");

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(sms);

        batch.addRecipient("+77001111111");
        batch.addRecipient("+77002222222");
        batch.addRecipient("+77003333333");

        batch.sendBatch("Please don't forget to bring snacks! :P");


        System.out.println("\nBATCH WITH AN OVERSIZED MESSAGE");

        batch.sendBatch("A".repeat(161));
    }
}