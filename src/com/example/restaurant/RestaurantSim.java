package com.example.restaurant;

import java.io.IOException;
import java.util.List;

public class RestaurantSim {
    public static void main(String[] args) {
        List<String> allDishes = MenuLoader.loadDishes("src/resources/dishes.txt");

        TicketingSystem ticketingSystem = new TicketingSystem();
        Thread waitstaff1 = new Waitstaff(ticketingSystem, allDishes, "Waitstaff John");
        Thread waitstaff2 = new Waitstaff(ticketingSystem, allDishes, "Waitstaff Jane");
//        Thread waitstaff3 = new Waitstaff(ticketingSystem, allDishes, "Waitstaff Mike");
        Thread chef1 = new Chef(ticketingSystem, "Chef Viktor Angelov");
        Thread chef2 = new Chef(ticketingSystem, "Chef Sevda Dimitrova");
        Thread chef3 = new Chef(ticketingSystem, "Cook Lio");

        waitstaff1.start();
        waitstaff2.start();
//        waitstaff3.start();
        chef1.start();
        chef2.start();
        chef3.start();
    }
}
