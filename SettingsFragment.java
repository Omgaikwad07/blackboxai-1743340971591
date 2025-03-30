import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {
    private RadioGroup notificationRadioGroup;
    private MaterialRadioButton radioOneDay, radioThreeDays, radioOneWeek;
    private MaterialButton saveButton;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeViews(view);
        setupFirebase();
        loadCurrentSettings();
        setupSaveButton();
        return view;
    }

    private void initializeViews(View view) {
        notificationRadioGroup = view.findViewById(R.id.notification_radio_group);
        radioOneDay = view.findViewById(R.id.radio_one_day);
        radioThreeDays = view.findViewById(R.id.radio_three_days);
        radioOneWeek = view.findViewById(R.id.radio_one_week);
        saveButton = view.findViewById(R.id.save_settings_button);
    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseUtil.getCurrentUser().getUid();
    }

    private void loadCurrentSettings() {
        databaseReference.child("users").child(userId).child("settings")
            .child("notificationDays").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Integer days = snapshot.getValue(Integer.class);
                        if (days != null) {
                            setSelectedRadioButton(days);
                        }
                    } else {
                        // Set default to 3 days if no setting exists
                        radioThreeDays.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), 
                        "Failed to load settings", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setSelectedRadioButton(int days) {
        switch (days) {
            case 1:
                radioOneDay.setChecked(true);
                break;
            case 3:
                radioThreeDays.setChecked(true);
                break;
            case 7:
                radioOneWeek.setChecked(true);
                break;
            default:
                radioThreeDays.setChecked(true);
        }
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        int selectedDays = getSelectedNotificationDays();
        databaseReference.child("users").child(userId).child("settings")
            .child("notificationDays").setValue(selectedDays)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), 
                        "Settings saved", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(requireContext(), 
                        "Failed to save settings", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private int getSelectedNotificationDays() {
        int checkedId = notificationRadioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.radio_one_day) return 1;
        if (checkedId == R.id.radio_three_days) return 3;
        return 7; // Default to one week
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up any listeners if needed
    }
}