import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String encodedMessage = Base64.getEncoder()
                .encodeToString(
                        message.getBytes(StandardCharsets.UTF_8)
                );

        boolean successful = sender.sendMessage(
                recipient,
                "Encrypted notification",
                encodedMessage
        );

        if (successful) {
            System.out.println("Delivery successful.");
        } else {
            System.out.println("Delivery failed.");
        }
    }
}