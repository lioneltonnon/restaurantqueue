package com.example.restaurant;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class TicketingSystem {
    private Queue<Order> buffer = new LinkedList<>();
    private final int MAX_CAPACITY = 5;

    // GUI Components
    private JFrame frame;
    private JTextArea textArea;
    private JLabel statusLabel;
    private BufferPanel bufferPanel;

    public TicketingSystem() {
        // Initialize GUI components
        frame = new JFrame("Restaurant Ticketing System");
        textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        statusLabel = new JLabel("Status: Ready");
        bufferPanel = new BufferPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        panel.add(bufferPanel, BorderLayout.NORTH);

        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void addOrder(Order order, String waiterName) throws InterruptedException {
        while (buffer.size() == MAX_CAPACITY) {
            updateLog("Buffer is full, " + waiterName + " waiting to add order...\n");
            wait();
        }
        buffer.add(order);
        updateDisplay(waiterName + " added " + order);
        notifyAll();
    }

    public synchronized Order takeOrder(String chefName) throws InterruptedException {
        while (buffer.isEmpty()) {
            updateLog("Buffer is empty, " + chefName + " waiting for orders...\n");
            wait();
        }
        Order order = buffer.poll();
        updateDisplay(chefName + " removed " + order);
        notifyAll();
        return order;
    }

    private void updateDisplay(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            // Update the visual representation of the buffer
            bufferPanel.updateBuffer(buffer.size());
            updateStatus("Buffer: " + buffer);
        });
    }

    private void updateLog(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message));
    }

    private void updateStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText("Status: " + status));
    }

    // Inner class to represent the buffer visually
    class BufferPanel extends JPanel {
        private int currentBufferSize = 0;

        public BufferPanel() {
            setPreferredSize(new Dimension(150, 50)); // Size of the panel
        }

        public void updateBuffer(int size) {
            this.currentBufferSize = size;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw buffer rectangle
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 130, 30); // Outline for the buffer

            // Draw buffer slots
            for (int i = 0; i < MAX_CAPACITY; i++) {
                if (i < currentBufferSize) {
                    g.setColor(Color.GREEN); // Filled slots
                } else {
                    g.setColor(Color.LIGHT_GRAY); // Empty slots
                }
                g.fillRect(15 + i * 25, 15, 20, 20);
            }
        }
    }
}
