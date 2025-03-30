import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.navigation.Navigation;

public class ScanFragment extends Fragment {
    private DecoratedBarcodeView barcodeView;
    private boolean isProcessingScan = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);
        
        setupBarcodeScanner();
        return view;
    }

    private void setupBarcodeScanner() {
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!isProcessingScan && result.getText() != null) {
                    isProcessingScan = true;
                    processBarcodeResult(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Optional: Handle possible result points
            }
        });
    }

    private void processBarcodeResult(String barcode) {
        if (isValidBarcode(barcode)) {
            navigateToProductDetails(barcode);
        } else {
            showInvalidBarcodeMessage();
            resumeScanning();
        }
    }

    private boolean isValidBarcode(String barcode) {
        // Basic validation - could be enhanced with regex
        return barcode != null && barcode.length() >= 8;
    }

    private void navigateToProductDetails(String barcode) {
        Bundle args = new Bundle();
        args.putString("barcode", barcode);
        Navigation.findNavController(requireView())
            .navigate(R.id.action_scan_to_product_details, args);
    }

    private void showInvalidBarcodeMessage() {
        Toast.makeText(requireContext(), 
            R.string.invalid_barcode_message, 
            Toast.LENGTH_SHORT).show();
    }

    private void resumeScanning() {
        isProcessingScan = false;
        barcodeView.resume();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
        isProcessingScan = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        barcodeView.pause();
    }
}