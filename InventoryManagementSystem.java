import java.io.*;
import java.util.Scanner;

class Product {
    private int productId;
    private String productName;
    private double price;
    private int quantity;

    public Product(int productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return productId + ", " + productName + ", " + price + ", " + quantity;
    }
}

class Inventory {
    private Product[] products;
    private int count;

    public Inventory() {
        products = new Product[100];  // Initial capacity of 100
        count = 0;
    }

    public void addProduct(Product product) {
        if (count >= products.length) {
            expandArray();
        }
        products[count++] = product;
    }

    public void removeProduct(int productId) {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == productId) {
                products[i] = products[count - 1];  // Replace with the last product
                products[count - 1] = null;  // Remove reference for garbage collection
                count--;
                break;
            }
        }
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == updatedProduct.getProductId()) {
                products[i].setProductName(updatedProduct.getProductName());
                products[i].setPrice(updatedProduct.getPrice());
                products[i].setQuantity(updatedProduct.getQuantity());
                break;
            }
        }
    }

    public Product searchProduct(int productId) {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId() == productId) {
                return products[i];
            }
        }
        return null;
    }

    public void displayProducts() {
        for (int i = 0; i < count; i++) {
            System.out.println(products[i]);
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < count; i++) {
                writer.write(products[i].toString());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        products = new Product[100];  // Reset array
        count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                int productId = Integer.parseInt(parts[0]);
                String productName = parts[1];
                double price = Double.parseDouble(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
                addProduct(new Product(productId, productName, price, quantity));
            }
        }
    }

    private void expandArray() {
        Product[] newProducts = new Product[products.length * 2];
        System.arraycopy(products, 0, newProducts, 0, products.length);
        products = newProducts;
    }
}

public class InventoryManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    removeProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    searchProduct();
                    break;
                case 5:
                    displayProducts();
                    break;
                case 6:
                    saveToFile();
                    break;
                case 7:
                    loadFromFile();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Inventory Management System");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Update Product");
        System.out.println("4. Search Product");
        System.out.println("5. Display Products");
        System.out.println("6. Save Products to File");
        System.out.println("7. Load Products from File");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        System.out.print("Enter Product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Product Quantity: ");
        int quantity = scanner.nextInt();
        Product product = new Product(id, name, price, quantity);
        inventory.addProduct(product);
        System.out.println("Product added successfully.");
    }

    private static void removeProduct() {
        System.out.print("Enter Product ID to remove: ");
        int id = scanner.nextInt();
        inventory.removeProduct(id);
        System.out.println("Product removed successfully.");
    }

    private static void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter new Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Product Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Product Quantity: ");
        int quantity = scanner.nextInt();
        Product updatedProduct = new Product(id, name, price, quantity);
        inventory.updateProduct(updatedProduct);
        System.out.println("Product updated successfully.");
    }

    private static void searchProduct() {
        System.out.print("Enter Product ID to search: ");
        int id = scanner.nextInt();
        Product product = inventory.searchProduct(id);
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void displayProducts() {
        inventory.displayProducts();
    }

    private static void saveToFile() {
        System.out.print("Enter filename to save: ");
        String filename = scanner.next();
        try {
            inventory.saveToFile(filename);
            System.out.println("Products saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        System.out.print("Enter filename to load: ");
        String filename = scanner.next();
        try {
            inventory.loadFromFile(filename);
            System.out.println("Products loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }
}