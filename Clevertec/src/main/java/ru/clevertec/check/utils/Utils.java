package main.java.ru.clevertec.check.utils;

import main.java.ru.clevertec.check.dataSource.DataProvider;
import main.java.ru.clevertec.check.model.Discount;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.model.Errors;

import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utils {

    public final static String delimiter = ",";

    public static String inputLine(String[] args) {
        String input = Arrays.toString(args);
        return input.substring(1, input.length() - 1).replace(",", "");
    }

    public static int parseStrToInt(String value) {
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    public static double parseStringToDouble(String value) {

        return Double.parseDouble(value);
    }

    public static boolean parseStringToBool(String value) {
        return Boolean.parseBoolean(value);
    }

    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static Product findProductById(int id, DataProvider provider) {
        Product product = null;
        for (var item : provider.getProducts()) {
            if (item.getId() == id) {
                product = item;
                break;
            }
        }
        return product;
    }

    public static Discount findDiscountByNum(int id, DataProvider provider) {
        Discount discount = null;
        for (var item : provider.getDiscounts()) {
            if (item.getNumber() == id) {
                discount = item;
                break;
            }
        }
        return discount;
    }

    public static boolean cashValidator(double bankAccount, double totalMoney) {
        if (bankAccount >= totalMoney) {
            return true;
        } else {
            EditCSV.createErrorCSV(Errors.NOT_EN_MONEY);
            return false;
        }
    }


    public static String errorText(Errors en) {
        return
                switch (en) {
                    case Errors.BAD_REQ -> "BAD REQUEST";
                    case Errors.NOT_EN_MONEY -> "NOT ENOUGH MONEY";
                    case Errors.INTERNAL_SERVER_ERROR -> "INTERNAL SERVER ERROR";

                };
    }

    public static void printLineCSV(FileWriter csvWriter, String string) {

        try {
            csvWriter.append(string).append("\n");
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printHeaderCSV(FileWriter csvWriter) {

        String time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        try {
            csvWriter.append("Date").append(delimiter).append("Time");
            csvWriter.append("\n");
            csvWriter.append(formatter.format(date)).append(delimiter).append(time);
            csvWriter.append("\n");
            csvWriter.append("\n");
            csvWriter.append("QTY").append(delimiter).append("DESCRIPTION").append(delimiter).append("PRICE").append(delimiter).
                    append("DISCOUNT").append(delimiter).append("TOTAL").append(",").append("[Optional] PERSONAL DISCOUNT");
            csvWriter.append("\n");

            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printDiscountSectionCSV(FileWriter csvWriter, String string) {
        try {
            csvWriter.append("DISCOUNT CARD").append(delimiter).append("DISCOUNT PERCENTAGE");
            csvWriter.append("\n");
            csvWriter.append(string);
            csvWriter.append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printFooterCSV(FileWriter csvWriter, String string) {

        try {
            csvWriter.append("\n");
            csvWriter.append("TOTAL PRICE").append(delimiter).append("TOTAL DISCOUNT").append(delimiter).append("TOTAL WITH DISCOUNT");
            csvWriter.append("\n");
            csvWriter.append(string);
            csvWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
