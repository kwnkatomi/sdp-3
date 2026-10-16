public class SmsMessageSender implements MessageSender {

    private static final int MAX_LENGTH = 160;

    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(
            String recipient,
            String title,
            String body
    ) {
        if (!isAvailable()) {
            System.out.println("SMS provider is unavailable.");
            return false;
        }

        if (body.length() > MAX_LENGTH) {
            System.out.println(
                    "SMS failed: message exceeds "
                            + MAX_LENGTH + " characters."
            );
            return false;
        }

        System.out.println("SMS");
        System.out.println("Phone: " + recipient);
        System.out.println("Message: " + body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}