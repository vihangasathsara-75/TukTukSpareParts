package com.tuktuk.tuktukspareparts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AuditLogger {

    private String logFile = "audit_log.txt";

    private void writeLog(String message) {

        try {
            // true means appends to end of file
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));

            // get current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // write timestamp and message
            writer.write(timestamp + " | " + message);
            writer.newLine();

            writer.close();

        } catch (Exception e) {
            System.out.println("couldnt write to audit log");
        }
    }

    // logs when a new part is added
    public void logAddPart(String partCode, String partName, int quantity) {
        String message = "ADDED | " + partCode + " | " + partName + " | Qty: " + quantity;
        writeLog(message);
        System.out.println("logged: part added - " + partCode);
    }

    // logs when a part is deleted
    public void logDeletePart(String partCode, String partName) {
        String message = "DELETED | " + partCode + " | " + partName + " | -";
        writeLog(message);
        System.out.println("logged: part deleted - " + partCode);
    }

    // logs when a checkout is processed
    public void logCheckout(ArrayList<Part> cartItems, ArrayList<Integer> cartQuantities, double total) {

        for (int i = 0; i < cartItems.size(); i++) {
            Part part = cartItems.get(i);
            int qty = cartQuantities.get(i);
            String message = "CHECKOUT | " + part.getCode() + " | " + part.getName() + " | Qty: " + qty;
            writeLog(message);
        }

        writeLog("CHECKOUT TOTAL | Rs." + total);
        System.out.println("logged: checkout processed - total Rs." + total);
    }
}