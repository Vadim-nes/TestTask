package main.java.ru.clevertec.check.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Order {

    private final int id;
    private final int count;

    private Order(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.count = orderBuilder.count;
    }

    public static class OrderBuilder {
        private int id;
        private int count;

        public OrderBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setCount(int count) {
            this.count = count;
            return this;
        }

        public Order build () {
            return new Order(this);
        }
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", count=" + count + '}';
    }
}
