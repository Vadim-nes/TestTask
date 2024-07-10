package main.java.ru.clevertec.check.model;

import main.java.ru.clevertec.check.utils.EditCSV;
import main.java.ru.clevertec.check.utils.Utils;
import main.java.ru.clevertec.check.dataSource.DataProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Customer {
    private final boolean discountNumberFlag;
    private final int discountNumber;
    private final double bankAccount;
    private final List<Order> orders;
    private final DataProvider provider;
    private final boolean isDiscountPresent;

    private Customer(CustomerBuilder customerBuilder) {
        this.discountNumberFlag = customerBuilder.discountFlag;
        this.discountNumber = customerBuilder.discountNumber;
        this.bankAccount = customerBuilder.bankAccount;
        this.orders = customerBuilder.orders;
        this.provider = customerBuilder.provider;
        this.isDiscountPresent = customerBuilder.isDiscountPresent;
    }

    public static class CustomerBuilder {

        private boolean discountFlag;
        private int discountNumber;
        private double bankAccount;
        private List<Order> orders;
        private DataProvider provider;
        private boolean isDiscountPresent;
        public CustomerBuilder setDiscountFlag(boolean discountFlag) {
            this.discountFlag = discountFlag;
            return this;

        }

        public CustomerBuilder setDiscountNumber(int discountNumber) {
            this.discountNumber = discountNumber;
            return this;
        }

        public CustomerBuilder setBankAccount(double bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public CustomerBuilder setOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        public CustomerBuilder setProvider(DataProvider provider) {
            this.provider = provider;
            return this;
        }

        public CustomerBuilder isDiscountPresent(boolean isDiscountPresent) {
            this.isDiscountPresent = isDiscountPresent;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public boolean isDiscountNumberFlag() {
        return discountNumberFlag;
    }

    public int getDiscountNumber() {
        return discountNumber;
    }

    public double getBankAccount() {
        return bankAccount;
    }

    public void runSellOperation() {
        groupProductsByProductId();
    }

    private void groupProductsByProductId() {

        Stream<Order> streamOrder = orders.stream();

        Map<Integer, Integer> a = streamOrder.collect(
                Collectors.groupingBy(Order::getId, Collectors.summingInt(Order::getCount)));
        calculateTotalPrice(a);
    }

    private double calculateDiscount(Map.Entry<Integer, Integer> item, Product product, Discount discount) {

        try {
            if (item.getValue() > 5 &&
                    product.isWholesale()) {
                return Discount.WHOLESALE_DISCOUNT;

            } else if (discountNumberFlag &&
                    Utils.findDiscountByNum(discountNumber, provider) != null) {
                return discount.getDiscount() * 0.01;

            } else if (discountNumberFlag || isDiscountPresent) {
                return Discount.STANDARD_DISCOUNT;
            }

        } catch (NullPointerException ex) {
            System.out.println(Utils.errorText(Errors.BAD_REQ));
            EditCSV.createErrorCSV(Errors.BAD_REQ);
        }
        return 0;
    }

    private void calculateTotalPrice(Map<Integer, Integer> map) {

        double totalPriceWithDiscount = 0;
        double totalDiscount = 0;
        double totalPriceWithoutDiscount;
        double discountPercentage = 0;
        double totalPriceTest = totalPriceCounter(map);
        StringBuilder dataCSV = new StringBuilder();
        StringBuilder totalPricesCSV = new StringBuilder();



        for (Map.Entry<Integer, Integer> item : map.entrySet()) {

            Product product = Utils.findProductById(item.getKey(), provider);
            Discount discount = Utils.findDiscountByNum(discountNumber, provider);
            discountPercentage = calculateDiscount(item, product, discount);

            if (product.remainingProductCount(item.getValue()) && Utils.cashValidator(bankAccount, totalPriceTest)) {

                try {

                    double temp = (1 - discountPercentage) * product.getPrice() * item.getValue();
                    totalPriceWithDiscount += temp;
                    double temp2 = discountPercentage * product.getPrice() * item.getValue();
                    totalDiscount += temp2;

                    dataCSV.append(item.getValue()).append(Utils.delimiter).append(product.getDescription()).
                            append(Utils.delimiter).append(String.format("%.2f$", product.getPrice())).append(Utils.delimiter).
                            append(String.format("%.2f$", temp2)).append(Utils.delimiter).
                            append(String.format("%.2f$", temp)).append(",").append(print(discountPercentage)).append("\n");

                    EditCSV.updateCSVproductCount(product.toString(),item.getValue());
                    EditCSV.deleteCSVrow(discountNumber);

                } catch (NullPointerException ignored) {
                }
            }
        }

        if (Utils.cashValidator(bankAccount, totalPriceWithDiscount)) {

            totalPriceWithoutDiscount = totalPriceWithDiscount + totalDiscount;

            totalPricesCSV.append(String.format("%.2f$", totalPriceWithoutDiscount)).append(Utils.delimiter).
                    append(String.format("%.2f$", totalDiscount)).append(Utils.delimiter).
                    append(String.format("%.2f$", totalPriceWithDiscount));

            String discountsDataCSV = discountNumber + Utils.delimiter + String.format("%.0f%%", discountPercentage * 100);

            Utils.printLineCSV(provider.getCsvWriter(), dataCSV.toString());
            if (isDiscountPresent) Utils.printDiscountSectionCSV(provider.getCsvWriter(), discountsDataCSV);
            Utils.printFooterCSV(provider.getCsvWriter(), totalPricesCSV.toString());

        }
    }

    private double totalPriceCounter(Map<Integer, Integer> map){

        double totalPr = 0;
        double discountPerc = 0;

        for (Map.Entry<Integer, Integer> item : map.entrySet()) {

            Product product = Utils.findProductById(item.getKey(), provider);
            Discount discount = Utils.findDiscountByNum(discountNumber, provider);
            discountPerc = calculateDiscount(item, product, discount);

                try {
                    double temp = (1 - discountPerc) * product.getPrice() * item.getValue();
                    totalPr += temp;
                } catch (NullPointerException ignored) {
                }
            }
        return totalPr;
    }

    private String print(double s) {
        String a;
        if (s == 0.1) {
            a = String.format("%.0f", s * 100) + "%";
        } else {
            a = "NO PERSONAL 10 % DISCOUNT";
        }
        return a;
    }

    @Override
    public String toString() {
        return "Customer{" + "discountFlag=" + discountNumberFlag + ", discoutNumber=" + discountNumber
                + ", bankAccount=" + bankAccount + ", orders=" + orders + ", isDiscountPresent=" +isDiscountPresent + "}";
    }
}
