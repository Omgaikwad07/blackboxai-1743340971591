import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpiryService extends Service {
    private FirebaseDatabase database;
    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkExpiryDates();
        return START_STICKY;
    }

    private void checkExpiryDates() {
        String userId = FirebaseUtil.getCurrentUser().getUid();
        
        // First get user's notification preference
        database.getReference("users/" + userId + "/settings/notificationDays")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot settingsSnapshot) {
                    int daysBefore = 3; // Default value
                    if (settingsSnapshot.exists()) {
                        daysBefore = settingsSnapshot.getValue(Integer.class);
                    }
                    
                    // Then check products against this preference
                    database.getReference("users/" + userId + "/products")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot productsSnapshot) {
                                for (DataSnapshot productSnapshot : productsSnapshot.getChildren()) {
                                    Product product = productSnapshot.getValue(Product.class);
                                    if (product != null) {
                                        NotificationManager.checkAndSendNotification(
                                            getApplicationContext(),
                                            product,
                                            daysBefore
                                        );
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
    }
}