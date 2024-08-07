package com.example.restaurant;

import javax.swing.*; // Importing Swing components for GUI creation
import java.awt.*; // Importing AWT components for GUI layout
import java.util.LinkedList; // Importing LinkedList for order queue implementation
import java.util.Queue; // Importing Queue interface

// Main class for the ticketing system
public class TicketingSystem {
    // Buffer to hold orders, implemented as a queue
    private Queue<Order> buffer = new LinkedList<>();
    // Maximum capacity of the buffer
    private final int MAX_CAPACITY = 5;

    // GUI Components
    private JFrame frame; // Main window frame
    private JTextArea textArea; // Text area for displaying logs and status
    private JLabel statusLabel; // Label to show the current status
    private BufferPanel bufferPanel; // Custom panel to visualize the buffer

    // Constructor to set up the GUI components
    public TicketingSystem() {
        // Initialize the main frame
        frame = new JFrame("Restaurant Ticketing System");
        // Initialize the text area, set to non-editable
        textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        // Initialize the status label
        statusLabel = new JLabel("Status: Ready");
        // Initialize the buffer panel
        bufferPanel = new BufferPanel();

        // Create a panel to hold components
        JPanel panel = new JPanel();
        // Set layout for the panel
        panel.setLayout(new BorderLayout());
        // Add the text area with scroll pane at the center
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        // Add the status label at the bottom
        panel.add(statusLabel, BorderLayout.SOUTH);
        // Add the buffer panel at the top
        panel.add(bufferPanel, BorderLayout.NORTH);

        // Set the panel as the content pane of the frame
        frame.setContentPane(panel);
        // Pack the components within the window
        frame.pack();
        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Make the window visible
        frame.setVisible(true);
    }

    // Method to add an order to the buffer, synchronized to prevent race conditions
    public synchronized void addOrder(Order order, String waiterName) throws InterruptedException {
        // Wait if the buffer is full
        while (buffer.size() == MAX_CAPACITY) {
            updateLog("Buffer is full, " + waiterName + " waiting to add order...\n");
            wait(); // Wait until a slot becomes available
        }
        // Add the order to the buffer
        buffer.add(order);
        // Update display with the action performed
        updateDisplay(waiterName + " added " + order);
        // Notify other waiting threads
        notifyAll();
    }

    // Method to take an order from the buffer, synchronized to prevent race conditions
    public synchronized Order takeOrder(String chefName) throws InterruptedException {
        // Wait if the buffer is empty
        while (buffer.isEmpty()) {
            updateLog("Buffer is empty, " + chefName + " waiting for orders...\n");
            wait(); // Wait until an order is available
        }
        // Remove the order from the buffer
        Order order = buffer.poll();
        // Update display with the action performed
        updateDisplay(chefName + " removed " + order);
        // Notify other waiting threads
        notifyAll();
        // Return the removed order
        return order;
    }

    // Method to update the display with a message
    private void updateDisplay(String message) {
        // Use SwingUtilities to ensure the update is done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Append the message to the text area
            textArea.append(message + "\n");
            // Update the visual representation of the buffer
            bufferPanel.updateBuffer(buffer.size());
            // Update the status label
            updateStatus("Buffer: " + buffer);
        });
    }

    // Method to log a message in the text area
    private void updateLog(String message) {
        // Use SwingUtilities to ensure the update is done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> textArea.append(message));
    }

    // Method to update the status label
    private void updateStatus(String status) {
        // Use SwingUtilities to ensure the update is done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> statusLabel.setText("Status: " + status));
    }

    // Inner class to represent the buffer visually
    class BufferPanel extends JPanel {
        private int currentBufferSize = 0; // Current size of the buffer

        // Constructor for BufferPanel
        public BufferPanel() {
            // Set the preferred size of the panel
            setPreferredSize(new Dimension(150, 50));
        }

        // Method to update the buffer size and repaint the panel
        public void updateBuffer(int size) {
            this.currentBufferSize = size;
            repaint(); // Repaint the panel to reflect changes
        }

        // Override paintComponent to draw the buffer visualization
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the outline of the buffer
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 130, 30); // Outline for the buffer

            // Draw buffer slots
            for (int i = 0; i < MAX_CAPACITY; i++) {
                // Check if the slot is filled or empty
                if (i < currentBufferSize) {
                    g.setColor(Color.GREEN); // Filled slots
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Empty slots
                }
                // Draw the slot
                g.fillRect(15 + i * 25, 15, 20, 20);
            }
        }
    }
}
