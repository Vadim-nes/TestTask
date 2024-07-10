package main.java.ru.clevertec.check.dataSource;
import main.java.ru.clevertec.check.model.Discount;
import main.java.ru.clevertec.check.model.Product;

import java.io.FileWriter;
import java.util.List;

public interface DataProvider {
     List<Product> getProducts();
     List<Discount> getDiscounts();

     FileWriter getCsvWriter();
}
