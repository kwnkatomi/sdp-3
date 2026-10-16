public class PushNotificationSender implements MessageSender {

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
            System.out.println("Push provider is unavailable.");
            return false;
        }

        System.out.println("PUSH NOTIFICATION");
        System.out.println("Device token: " + recipient);
        System.out.println("Popup title: " + title);
        System.out.println("Popup message: " + body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Push";
    }
}