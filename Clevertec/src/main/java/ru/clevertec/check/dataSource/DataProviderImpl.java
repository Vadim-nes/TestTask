package main.java.ru.clevertec.check.dataSource;

import main.java.ru.clevertec.check.utils.Utils;
import main.java.ru.clevertec.check.model.Discount;
import main.java.ru.clevertec.check.model.Product;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataProviderImpl implements DataProvider {

    public static final String pathDiscounts = "./src/main/resources/discountCards.csv";
    public static final String pathProducts = "./src/main/resources/products.csv";

    public DataProviderImpl() {

        readCSV(pathDiscounts);
        readCSV(pathProducts);

            try {
                csvWriter = new FileWriter("result.csv");
                Utils.printHeaderCSV(csvWriter);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    private final List<Product> products = new ArrayList<>();
    private final List <Discount> discounts = new ArrayList<>();
    private final FileWriter csvWriter;

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public List<Discount> getDiscounts() {
        return discounts;
    }

    public FileWriter getCsvWriter() {
        return csvWriter;
    }

    private void readCSV(String path) {


        List<List<String>> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {

            String str = "";

            while ((str = br.readLine()) != null) {
                List <String> s = List.of(str.split(","));
                list.add(s);
            }
            System.out.println("File path is OK: " + path);
            addElements(list);

        } catch (IOException | RuntimeException ex) {
            System.out.println("Error: wrong file path: " + path);
        }
    }

    private void addElements(List<List<String>> a) {

        for (int i = 0; i < a.size(); i++ ) {
            if (i >=1 && a.get(0).size() == 5) {

                products.add(new Product.ProductBuilder()
                        .setId(Utils.parseStrToInt(a.get(i).get(0)))
                        .setDescription(a.get(i).get(1))
                        .setPrice(Utils.parseStringToDouble(a.get(i).get(2)))
                        .setQuantity(Utils.parseStrToInt(a.get(i).get(3)))
                        .setWholesale(Utils.parseStringToBool(a.get(i).get(4)))
                        .build());

            } else if (i >=1 && a.get(0).size() == 3) {

                discounts.add(new Discount.DiscountBuilder()
                        .setId(Utils.parseStrToInt(a.get(i).get(0)))
                        .setNumber(Utils.parseStrToInt(a.get(i).get(1)))
                        .setDiscount(Utils.parseStrToInt(a.get(i).get(2)))
                        .build());
            }
        }
    }
}
