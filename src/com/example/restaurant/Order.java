package com.example.restaurant;

public class Order {
    private String name;

    public Order(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
