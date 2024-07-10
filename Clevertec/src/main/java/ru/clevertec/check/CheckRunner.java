package main.java.ru.clevertec.check;
import main.java.ru.clevertec.check.dataSource.DataProviderImpl;
import main.java.ru.clevertec.check.model.Customer;
import main.java.ru.clevertec.check.model.Errors;
import main.java.ru.clevertec.check.utils.EditCSV;
import main.java.ru.clevertec.check.utils.ParseCSV;
import main.java.ru.clevertec.check.utils.Utils;

import java.io.IOException;

public class CheckRunner {

    public static void main(String[] args) {

        DataProviderImpl data = new DataProviderImpl();
        ParseCSV calculations = new ParseCSV(data);
        Customer customer =  calculations.parseInput(args);
        customer.runSellOperation();

    }
}