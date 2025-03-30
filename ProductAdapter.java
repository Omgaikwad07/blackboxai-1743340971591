import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> productList;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView expiryTextView;
        private final TextView daysLeftTextView;
        private final ImageView categoryIcon;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            expiryTextView = itemView.findViewById(R.id.expiry_date);
            daysLeftTextView = itemView.findViewById(R.id.days_left);
            categoryIcon = itemView.findViewById(R.id.category_icon);
        }

        public void bind(Product product, OnProductClickListener listener) {
            nameTextView.setText(product.getName());
            expiryTextView.setText(formatExpiryDate(product.getExpiryDate()));
            daysLeftTextView.setText(calculateDaysLeft(product.getExpiryDate()));
            setCategoryIcon(product.getCategory());

            itemView.setOnClickListener(v -> listener.onProductClick(product));
        }

        private String formatExpiryDate(String dateString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return dateString;
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
                    return "Expires today";
                } else {
                    return days + " days left";
                }
            } catch (ParseException e) {
                return "";
            }
        }

        private void setCategoryIcon(String category) {
            int iconRes = R.drawable.ic_grocery; // Default icon
            if (category != null) {
                switch (category.toLowerCase()) {
                    case "dairy":
                        iconRes = R.drawable.ic_dairy;
                        break;
                    case "vegetables":
                        iconRes = R.drawable.ic_vegetable;
                        break;
                    case "meat":
                        iconRes = R.drawable.ic_meat;
                        break;
                }
            }
            categoryIcon.setImageResource(iconRes);
        }
    }
}