public class MarketingNotification extends Notification {

    private MessageSender email;

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String marketingMessage =
                "[PROMO] " + message
                        + "\nReply STOP to unsubscribe.";

        if (!sender.isAvailable()) {
            System.out.println(
                    sender.getChannelName() + " is unavailable."
            );
            return;
        }

        boolean successful = sender.sendMessage(
                recipient,
                "Special offer",
                marketingMessage
        );

        if (successful) {
            System.out.println("Marketing delivery successful.");
        } else {
            System.out.println("Marketing delivery failed.");
        }
    }
}