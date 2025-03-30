import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.expirydatetracker.MainActivity;

public class NotificationManager {
    private static final String CHANNEL_ID = "expiry_notifications";
    private static final String CHANNEL_NAME = "Expiry Notifications";
    private static final String CHANNEL_DESC = "Notifications for expiring products";
    private static final int NOTIFICATION_ID = 1;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            android.app.NotificationManager manager = 
                context.getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static void checkAndSendNotification(Context context, Product product, int daysBefore) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiry = sdf.parse(product.getExpiryDate());
            Date today = new Date();
            
            long diff = expiry.getTime() - today.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            
            if (days == daysBefore) {
                sendExpiryNotification(context, product.getName(), product.getExpiryDate(), daysBefore);
            }
        } catch (ParseException e) {
            // Handle parse error
        }
    }

    private static void sendExpiryNotification(Context context, String productName, String expiryDate, int daysBefore) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        String message = daysBefore == 0 ? 
            productName + " expires today!" :
            productName + " expires in " + daysBefore + " days";

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Product Expiring Soon!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build();

        android.app.NotificationManager notificationManager = 
            context.getSystemService(android.app.NotificationManager.class);
        notificationManager.notify(productName.hashCode(), notification); // Unique ID per product
    }

    public static void sendRestockNotification(Context context, String productName) {
        // Similar implementation for restock notifications
    }
}