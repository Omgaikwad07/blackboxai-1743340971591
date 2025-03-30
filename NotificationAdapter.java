import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final List<Product> expiringProducts;

    public NotificationAdapter(List<Product> expiringProducts) {
        this.expiringProducts = expiringProducts;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Product product = expiringProducts.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return expiringProducts.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView expiryDate;
        private final TextView daysLeft;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.notification_product_name);
            expiryDate = itemView.findViewById(R.id.notification_expiry_date);
            daysLeft = itemView.findViewById(R.id.notification_days_left);
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            expiryDate.setText(formatExpiryDate(product.getExpiryDate()));
            daysLeft.setText(calculateDaysLeft(product.getExpiryDate()));
        }

        private String formatExpiryDate(String dateString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return "Expires: " + outputFormat.format(date);
            } catch (ParseException e) {
                return "Expires: " + dateString;
            }
        }

        private String calculateDaysLeft(String expiryDate) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date expiry = sdf.parse(expiryDate);
                Date today = new Date();
                
                long diff = expiry.getTime() - today.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                
                if (days < 0) {
                    return "Expired " + Math.abs(days) + " days ago";
                } else if (days == 0) {
                    return "Expires today!";
                } else {
                    return days + " days left";
                }
            } catch (ParseException e) {
                return "";
            }
        }
    }
}