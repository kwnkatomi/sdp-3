public class UrgentNotification extends Notification {

    private final MessageSender fallbackSender;

    public UrgentNotification(MessageSender sender) {
        this(sender, null);
    }

    public UrgentNotification(
            MessageSender sender,
            MessageSender fallbackSender
    ) {
        super(sender);
        this.fallbackSender = fallbackSender;
    }

    @Override
    public void send(String recipient, String message) {
        String urgentMessage = "[URGENT] " + message;

        if (trySend(sender, recipient, urgentMessage)) {
            return;
        }

        if (fallbackSender == null) {
            System.out.println(
                    "Urgent delivery failed: no fallback configured."
            );
            return;
        }

        System.out.println("Trying fallback provider...");

        if (!trySend(fallbackSender, recipient, urgentMessage)) {
            System.out.println("Urgent delivery failed.");
        }
    }

    private boolean trySend(
            MessageSender provider,
            String recipient,
            String message
    ) {
        if (!provider.isAvailable()) {
            System.out.println(
                    provider.getChannelName() + " is unavailable."
            );
            return false;
        }

        boolean successful = provider.sendMessage(
                recipient,
                "Urgent notification",
                message
        );

        if (successful) {
            System.out.println(
                    "Urgent delivery successful through "
                            + provider.getChannelName() + "."
            );
        } else {
            System.out.println(
                    provider.getChannelName() + " rejected delivery."
            );
        }

        return successful;
    }
}