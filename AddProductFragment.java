import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddProductFragment extends Fragment {
    private TextInputEditText nameEditText, expiryDateEditText, quantityEditText;
    private AutoCompleteTextView categoryAutoComplete;
    private MaterialButton saveButton;
    private final Calendar calendar = Calendar.getInstance();
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        initializeViews(view);
        setupFirebase();
        setupCategoryDropdown();
        setupExpiryDatePicker();
        setupSaveButton();
        return view;
    }

    private void initializeViews(View view) {
        nameEditText = view.findViewById(R.id.product_name);
        expiryDateEditText = view.findViewById(R.id.expiry_date);
        quantityEditText = view.findViewById(R.id.quantity);
        categoryAutoComplete = view.findViewById(R.id.category);
        saveButton = view.findViewById(R.id.save_button);
    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseUtil.getCurrentUser().getUid();
    }

    private void setupCategoryDropdown() {
        String[] categories = getResources().getStringArray(R.array.product_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        );
        categoryAutoComplete.setAdapter(adapter);
    }

    private void setupExpiryDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateExpiryDateField();
        };

        expiryDateEditText.setOnClickListener(v -> new DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void updateExpiryDateField() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        expiryDateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = nameEditText.getText().toString().trim();
        String expiryDate = expiryDateEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();
        String category = categoryAutoComplete.getText().toString().trim();

        if (validateInputs(name, expiryDate, quantityStr, category)) {
            int quantity = Integer.parseInt(quantityStr);
            Product product = new Product(name, expiryDate, category, quantity, ""); // Empty barcode
            
            databaseReference.child("users").child(userId).child("products").push()
                .setValue(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), 
                            R.string.product_saved, Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).popBackStack();
                    } else {
                        Toast.makeText(requireContext(), 
                            R.string.save_failed, Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private boolean validateInputs(String name, String expiryDate, String quantity, String category) {
        if (name.isEmpty()) {
            nameEditText.setError(getString(R.string.name_required));
            return false;
        }
        if (expiryDate.isEmpty()) {
            expiryDateEditText.setError(getString(R.string.expiry_date_required));
            return false;
        }
        if (quantity.isEmpty()) {
            quantityEditText.setError(getString(R.string.quantity_required));
            return false;
        }
        if (category.isEmpty()) {
            categoryAutoComplete.setError(getString(R.string.category_required));
            return false;
        }
        return true;
    }
}