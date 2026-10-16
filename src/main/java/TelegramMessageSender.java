public class TelegramMessageSender implements MessageSender {

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
            System.out.println("Telegram provider is unavailable.");
            return false;
        }

        System.out.println("TELEGRAM");
        System.out.println("Chat: " + recipient);
        System.out.println("'" + title + "'");
        System.out.println(body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }
}