package com.example.restaurant;

import java.util.List;
import java.util.Random;

public class Waitstaff extends Thread {
    private TicketingSystem ticketingSystem;
    private List<String> allDishes;
    private String waiterName;
    private Random random;

    public Waitstaff(TicketingSystem ticketingSystem, List<String> allDishes, String waiterName) {
        this.ticketingSystem = ticketingSystem;
        this.allDishes = allDishes;
        this.waiterName = waiterName;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = generateRandomOrder();
                if (order != null) {
                    ticketingSystem.addOrder(order, waiterName);
                    System.out.println(waiterName + " added order: " + order);
                }
                Thread.sleep(random.nextInt(5000) + 3000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Order generateRandomOrder() {
        // Select a random dish name
        String dishName = allDishes.get(random.nextInt(allDishes.size()));
        // Check if dish name is not empty
        if (dishName != null && !dishName.trim().isEmpty()) {
            return new Order(dishName);
        } else {
            // Log or handle empty dish name scenario
            System.err.println("Warning: Empty dish name encountered.");
            return null;
        }
    }
}
