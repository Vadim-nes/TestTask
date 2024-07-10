package main.java.ru.clevertec.check.model;

public class Discount {

    private final int id;
    private final int number;
    private final int discount;
    public static final double STANDARD_DISCOUNT = 0.02;
    public static final double WHOLESALE_DISCOUNT = 0.1;

    private Discount(DiscountBuilder dataDiscountBuilder) {
        this.id = dataDiscountBuilder.id;
        this.number = dataDiscountBuilder.number;
        this.discount = dataDiscountBuilder.discount;
    }

    public static class DiscountBuilder {

        private int id;
        private int number;
        private int discount;

        public DiscountBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public DiscountBuilder setNumber(int number) {
            this.number = number;
            return this;
        }

        public DiscountBuilder setDiscount(int discount) {
            this.discount = discount;
            return this;
        }

        public Discount build() {
            return new Discount(this);
        }
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getDiscount() {
        return discount;
    }

    public String toString () {

        return getId() + " - " + getNumber() + " - " + getDiscount();
    }
}
