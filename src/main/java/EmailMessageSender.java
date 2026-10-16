public class EmailMessageSender implements MessageSender {

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
            System.out.println("Email provider is unavailable.");
            return false;
        }

        System.out.println("EMAIL");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + title);
        System.out.println("Body: " + body);
        System.out.println("Notification Gateway");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }
}