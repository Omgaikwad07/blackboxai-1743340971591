import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        recyclerView = view.findViewById(R.id.product_recycler_view);
        progressIndicator = view.findViewById(R.id.progress_circular);
        productList = new ArrayList<>();
        
        setupRecyclerView();
        loadProducts();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(productList, product -> {
            // Handle product click - navigate to details
            Bundle args = new Bundle();
            args.putSerializable("product", product);
            Navigation.findNavController(requireView())
                .navigate(R.id.action_home_to_product_details, args);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        progressIndicator.setVisibility(View.VISIBLE);
        String userId = FirebaseUtil.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users/" + userId + "/products");
        
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        product.setKey(productSnapshot.getKey());
                        productList.add(product);
                    }
                }
                // Sort by expiry date (nearest first)
                Collections.sort(productList, Comparator.comparing(Product::getExpiryDate));
                adapter.notifyDataSetChanged();
                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressIndicator.setVisibility(View.GONE);
                // Handle error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseReference != null) {
            databaseReference.removeEventListener(this);
        }
    }
}