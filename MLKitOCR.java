import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.ArrayList;
import java.util.List;

public class MLKitOCR {
    private TextRecognizer textRecognizer;
    private OCRCallback callback;

    public interface OCRCallback {
        void onTextRecognized(List<String> items);
        void onError(Exception e);
    }

    public MLKitOCR() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    public void analyzeGroceryList(Bitmap bitmap, OCRCallback callback) {
        this.callback = callback;
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        
        textRecognizer.process(image)
            .addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text visionText) {
                    processOCRResults(visionText);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onError(e);
                }
            });
    }

    private void processOCRResults(Text visionText) {
        List<String> groceryItems = new ArrayList<>();
        
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText().trim();
                // Basic filtering - could be enhanced with ML
                if (isPotentialGroceryItem(lineText)) {
                    groceryItems.add(lineText);
                }
            }
        }
        
        callback.onTextRecognized(groceryItems);
    }

    private boolean isPotentialGroceryItem(String text) {
        // Simple heuristic - could be replaced with ML classification
        return !text.isEmpty() && 
               !text.matches(".*\\d{1,2}/\\d{1,2}/\\d{2,4}.*") && // Dates
               !text.matches(".*\\$?\\d+\\.?\\d{0,2}.*"); // Prices
    }

    public void close() {
        textRecognizer.close();
    }
}