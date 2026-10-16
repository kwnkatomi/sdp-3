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
    }
}