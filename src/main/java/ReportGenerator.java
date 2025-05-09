import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ReportGenerator {
    static class TaskRunnable implements Runnable {
        private final String path;
        private double totalCost;
        private int totalAmount;
        private int totalDiscountSum;
        private int totalLines;
        private Product mostExpensiveProduct;
        private double highestCostAfterDiscount;
        private int[] amounts = new int[10];
        private int[] discounts = new int[10];
        private int[] discountSums = new int[10];

        public TaskRunnable(String path) {
            this.path = path;
            this.totalCost = 0;
            this.totalAmount = 0;
            this.totalDiscountSum = 0;
            this.totalLines = 0;
            this.highestCostAfterDiscount = 0;
            this.mostExpensiveProduct = null;
        }

        @Override
        public void run() {
            //TODO:
            // - Read all lines from the input file (path)
            // - For each line, parse product ID, amount, and discount
            // - The format of the files are like this:
            //      [productId],[amount],[discountAmount]
            // - Find the corresponding product from catalog
            // - Calculate discounted cost and update total stats (totalAmount, totalCost, totalDiscountSum, totalLines)
            // - Track the most expensive purchase after discount
            try {
                Scanner yearlyReader = new Scanner(new FileReader(path));
                String line;
                while (yearlyReader.hasNextLine()) {
                    line = yearlyReader.nextLine();
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    int discount = Integer.parseInt(parts[2]);

                    amounts[id] += amount;
                    discounts[id] += discount;
                    discountSums[id] += discount * amount;

                    double v = productCatalog[id].price * amount * (100 - discount);
                    totalCost +=  v / 100;

                    if (highestCostAfterDiscount < v/100 ) {
                        highestCostAfterDiscount = v/100;
                        mostExpensiveProduct = productCatalog[id];
                    }

                    totalDiscountSum += discount;

                    totalLines++;
                    totalAmount+=amount;
                }

                makeReport();
            } catch (FileNotFoundException e) {
                System.out.println("File not found for path: " + path);
            }
        }

        public synchronized void makeReport() {
            // TODO:
            // - Print the filename
            // - Print total cost and total items bought
            // - Calculate and print average discount
            // - Display info about the most expensive purchase after discount

            System.out.println("\n**************\nfile path: " + path);
            System.out.println("Total cost: " + totalCost);
            System.out.println("Total amount: " + totalAmount);
            System.out.println("Average discount for each purchase: " + totalDiscountSum / totalLines + "%");
            System.out.println("Highest cost: " + highestCostAfterDiscount);
            System.out.println("Most expensive product: " + mostExpensiveProduct.getProductName());

            for (int i = 1; i < amounts.length; i++) {
                System.out.println();
                System.out.println(productCatalog[i].getProductName());
                System.out.println("Amount of this item: " + amounts[i]);
                System.out.println("Total discounts of this item: " + discounts[i] + "%");
                System.out.println("Average discount for this item per purchase: " + discountSums[i] / amounts[i] + "%");
            }
        }
    }

    static class Product {
        private int productID;
        private String productName;
        private double price;

        public Product(int productID, String productName, double price) {
            this.productID = productID;
            this.productName = productName;
            this.price = price;
        }

        public int getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }
    }
    private static final String[] ORDER_FILES = {"src/main/resources/2021_order_details.txt",
                                                 "src/main/resources/2022_order_details.txt",
                                                 "src/main/resources/2023_order_details.txt",
                                                 "src/main/resources/2024_order_details.txt"};

    static Product[] productCatalog = new Product[10];

    public static void loadProducts() {
        // TODO:
        // - Read lines from Products.txt
        // - For each line, parse product ID, name, and price
        // - The format of the file is like this:
        //      [productId],[name],[price]
        // - Store Product objects in the productCatalog array
        String productPath = "src/main/resources/Products.txt";

        try {
            Scanner reader = new Scanner(new FileReader(productPath));
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");
                int productID = Integer.parseInt(parts[0]);
                String productName = parts[1];
                double price = Double.parseDouble(parts[2]);
                productCatalog[productID] = new Product(productID, productName, price);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found for product catalog");
        }
    }

    public static void main(String[] args) {
        // TODO:
        // - Create one TaskRunnable and Thread for each order file
        // - Start all threads
        // - Wait for all threads to finish
        // - After all threads are done, call makeReport() on each TaskRunnable

        loadProducts();

        for (String path : ORDER_FILES) {
            Thread thread = new Thread(new TaskRunnable(path));
            thread.start();
        }


    }
}