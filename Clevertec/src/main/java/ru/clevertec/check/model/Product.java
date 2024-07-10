package main.java.ru.clevertec.check.model;

public class Product {

        private final int id;
        private final String description;
        private final double price;
        private final int quantity;
        private final boolean wholesale;

        private Product(ProductBuilder dataProductBuilder ) {
            this.id = dataProductBuilder.id;
            this.description = dataProductBuilder.description;
            this.price = dataProductBuilder.price;
            this.quantity = dataProductBuilder.quantity;
            this.wholesale = dataProductBuilder.wholesale;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() { return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public boolean isWholesale() {
            return wholesale;
        }

        public static class ProductBuilder {

            private int id;
            private String description;
            private double price;

            private int quantity;

            private boolean wholesale;

            public ProductBuilder setId(int id) {
                this.id = id;
                return this;
            }

            public ProductBuilder setDescription(String description) {
                this.description = description;
                return this;
            }

            public ProductBuilder setPrice(double price) {
                this.price = price;
                return this;
            }

            public ProductBuilder setQuantity(int quantity) {
                this.quantity = quantity;
                return this;
            }

            public ProductBuilder setWholesale(boolean wholesale) {
                this.wholesale = wholesale;
                return this;
            }

            public Product build () {
                return new Product(this);
            }
        }

    @Override
    public String toString() {
        return id + "," + description + "," + price + "," + quantity + "," + wholesale;
    }

    public boolean remainingProductCount (int count) {
            if (count > 0 && quantity >= count) {
                return true;
            } else {
                System.out.println("Remaining product item count" + description + " is not enough for purchase = " + quantity);
                return false;
            }
    }
}