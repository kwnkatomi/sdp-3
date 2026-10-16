public class Main {

    public static void main(String[] args) {
        EmailMessageSender email = new EmailMessageSender();

        //email.setAvailable(false);

        Notification notification =
                new EncryptedNotification(email);

        notification.send(
                "sara@example.com",
                "Hello! I'm sending you an encrypted message."
        );
    }
}