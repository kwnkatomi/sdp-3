public class KafkaEventSender implements MessageSender {

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
            System.out.println("Kafka provider is unavailable.");
            return false;
        }

        System.out.println("KAFKA EVENT");
        System.out.println("Topic: notifications");
        System.out.println("Key: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Payload: " + body);
        System.out.println("Event published.");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }
}