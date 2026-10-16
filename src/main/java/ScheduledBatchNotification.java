import java.util.ArrayList;
import java.util.List;

public class ScheduledBatchNotification extends Notification {

    private final List<String> recipients = new ArrayList<>();

    private int total;
    private int successful;
    private int failed;

    public ScheduledBatchNotification(MessageSender sender) {
        super(sender);
    }

    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    @Override
    public void send(String recipient, String message) {
        boolean delivered = deliver(recipient, message);

        if (delivered) {
            System.out.println("Delivery successful.");
        } else {
            System.out.println("Delivery failed.");
        }
    }

    public void sendBatch(String message) {
        total = recipients.size();
        successful = 0;
        failed = 0;

        for (String recipient : recipients) {
            if (deliver(recipient, message)) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("BATCH SUMMARY");
        System.out.println("Total: " + total);
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }

    private boolean deliver(String recipient, String message) {
        if (!sender.isAvailable()) {
            System.out.println(
                    sender.getChannelName() + " is unavailable."
            );
            return false;
        }

        return sender.sendMessage(
                recipient,
                "Batch notification",
                message
        );
    }

    public int getTotal() {
        return total;
    }

    public int getSuccessful() {
        return successful;
    }

    public int getFailed() {
        return failed;
    }
}