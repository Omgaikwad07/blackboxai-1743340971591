public class Product {
    private String name;
    private String expiryDate;
    private String category;
    private int quantity;
    private String barcode;

    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String name, String expiryDate, String category, int quantity, String barcode) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.category = category;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
}