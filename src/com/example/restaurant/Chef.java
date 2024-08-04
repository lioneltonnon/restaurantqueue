package com.example.restaurant;

import java.util.Random;

public class Chef extends Thread {
    private TicketingSystem ticketingSystem;
    private String chefName;
    private Random random;

    public Chef(TicketingSystem ticketingSystem, String chefName) {
        this.ticketingSystem = ticketingSystem;
        this.chefName = chefName;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = ticketingSystem.takeOrder(chefName);
                prepareDish(order);
                System.out.println("Chef " + chefName + " has completed: " + order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void prepareDish(Order order) throws InterruptedException {
        System.out.println("Chef " + chefName + " preparing: " + order);

        // Simulate preparation time with a random delay
        int preparationTime = random.nextInt(5000) + 3000; // Between 2000ms and 5000ms
        Thread.sleep(preparationTime);
    }
}
