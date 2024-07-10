package main.java.ru.clevertec.check.utils;

import main.java.ru.clevertec.check.dataSource.DataProvider;
import main.java.ru.clevertec.check.model.*;
import java.util.ArrayList;
import java.util.List;

public class ParseCSV {

    private final DataProvider provider;

    public ParseCSV(DataProvider provider) {
        this.provider = provider;
    }


    public Customer parseInput(String[] args) {

        String input = Utils.inputLine(args);

        if (input.matches("[0-9- ].+ balanceDebitCard=[0-9-.]+")) {
            System.out.println("CONSOLE INPUT FORMAT IS CORRECT");

            int discountNumber = 0;
            boolean isDiscountFlag = false;
            boolean isDiscountPresent = false;
            double bankAccount = 0;

            List<Order> orders = new ArrayList<>();

            for (int i = args.length - 1; i >= 0; i--) {
                String[] temp = args[i].split("[+\\-*/=]");

                if (temp[0].equals("discountCard")) {
                    isDiscountPresent = true;

                    if (temp[1].matches("[0-9]{4}")) {
                        discountNumber = Utils.parseStrToInt(temp[1]);
                        isDiscountFlag = true;

                    } else {
                        System.out.println("DISCOUNT NUMBER IS NOT PRESENT IN THE LIST");
                    }
                }
                try {
                    if (temp[0].equals("balanceDebitCard")) {
                        bankAccount = Utils.parseStringToDouble(temp[1]);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println(Utils.errorText(Errors.BAD_REQ));
                    EditCSV.createErrorCSV(Errors.BAD_REQ);
                }
                if (Utils.isNumber(temp[0]) && Utils.isNumber(temp[1])) {
                    orders.add(new Order.OrderBuilder()
                            .setId(Utils.parseStrToInt(temp[0]))
                            .setCount(Utils.parseStrToInt(temp[1]))
                            .build());

                }
            }
            return new Customer.CustomerBuilder().setDiscountFlag(isDiscountFlag)
                    .setDiscountNumber(discountNumber)
                    .setBankAccount(bankAccount)
                    .setOrders(orders)
                    .setProvider(provider)
                    .isDiscountPresent(isDiscountPresent)
                    .build();
        } else {
            System.out.println(Utils.errorText(Errors.BAD_REQ));
            EditCSV.createErrorCSV(Errors.BAD_REQ);
        }
        return null;
    }
}