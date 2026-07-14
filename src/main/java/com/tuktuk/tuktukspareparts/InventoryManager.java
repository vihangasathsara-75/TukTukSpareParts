package com.tuktuk.tuktukspareparts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class InventoryManager {

    // this is the list that holds all parts while the app is running
    private ArrayList<Part> partList;

    private String filename = "inventory_legacy.txt";

    public InventoryManager() {
        partList = new ArrayList<>();

    }


    // when the app starts this loads all parts from the file into the list
    public void loadParts() {
        partList = DataParser.loadParts(filename);
        System.out.println("loaded " + partList.size() + " parts from file");
    }

    // the GUI calls this to get the full list of parts to display
    public ArrayList<Part> getAllParts() {
        return partList;
    }


    // adds a new part to the list
    // returns false if a part with the same code already exists
    public boolean addPart(Part newPart) {

        // check if a part with this code already exists
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(newPart.getCode())) {
                System.out.println("part with this code already exists");
                return false;
            }
        }

        // code is unique so add it
        partList.add(newPart);
        System.out.println("part added: " + newPart.getCode());
        saveParts();
        return true;
    }


    // deletes a part by its code
    // returns false if part wasnt found
    public boolean deletePart(String code) {

        // loop through list and find the part with matching code
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {
                partList.remove(i);
                System.out.println("part deleted: " + code);
                saveParts();
                return true;
            }
        }

        // if we get here the code wasnt found
        System.out.println("part not found: " + code);
        return false;
    }


    // finds a part by code and replaces it with updated details
    // returns false if part wasnt found
    public boolean updatePart(String code, Part updatedPart) {

        // loop through list and find the part with matching code
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {

                // replace the old part with the updated one
                partList.set(i, updatedPart);
                System.out.println("part updated: " + code);
                saveParts();
                return true;
            }
        }

        // part wasnt found
        System.out.println("part not found: " + code);
        return false;
    }


    // saves the current list back to the text file
    // this is called after every add, delete or update
    public void saveParts() {

        try {
            // open file for writing - this overwrites the old file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // write each part as one line
            for (int i = 0; i < partList.size(); i++) {
                Part p = partList.get(i);

                // write all fields separated by commas
                writer.write(p.getCode() + "," +
                        p.getName() + "," +
                        p.getBrand() + "," +
                        p.getPrice() + "," +
                        p.getQuantity() + "," +
                        p.getCategory() + "," +
                        p.getDate() + "," +
                        p.getImageFile());

                // move to next line
                writer.newLine();
            }

            writer.close();
            System.out.println("parts saved to file");

        } catch (Exception e) {
            System.out.println("couldnt save to file: " + filename);
        }
    }


    // finds and returns one part by its code
    // returns null if not found
    public Part findPart(String code) {

        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {
                return partList.get(i);
            }
        }

        // part wasnt found
        return null;
    }

    public int getTotalCount() {
        return partList.size();
    }

    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < partList.size(); i++) {
            Part p = partList.get(i);
            total = total + (p.getPrice() * p.getQuantity());
        }
        return total;
    }

    public ArrayList<Part> getLowStockParts(int threshold) {
        ArrayList<Part> lowStock = new ArrayList<>();
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getQuantity() < threshold) {
                lowStock.add(partList.get(i));
            }
        }
        return lowStock;
    }
}

