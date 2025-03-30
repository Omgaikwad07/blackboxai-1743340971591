import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Product> expiringProducts;
    private MaterialButton settingsButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        initializeViews(view);
        setupRecyclerView();
        loadExpiringProducts();
        setupSettingsButton();
        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.notifications_recycler);
        settingsButton = view.findViewById(R.id.notification_settings);
        expiringProducts = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(expiringProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadExpiringProducts() {
        String userId = FirebaseUtil.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users/" + userId + "/products")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    expiringProducts.clear();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null && isExpiringSoon(product.getExpiryDate())) {
                            expiringProducts.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
    }

    private boolean isExpiringSoon(String expiryDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiry = sdf.parse(expiryDate);
            Date today = new Date();
            
            long diff = expiry.getTime() - today.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            
            return days <= 7; // Show products expiring within 7 days
        } catch (ParseException e) {
            return false;
        }
    }

    private void setupSettingsButton() {
        settingsButton.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_notifications_to_settings));
    }
}