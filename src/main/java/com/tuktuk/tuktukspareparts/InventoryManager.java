package com.tuktuk.tuktukspareparts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class InventoryManager {

    private ArrayList<Part> partList;

    private String filename = "inventory_legacy.txt";

    public InventoryManager() {
        partList = new ArrayList<>();

    }

    public void loadParts() {
        partList = DataParser.loadParts(filename);
        System.out.println("loaded " + partList.size() + " parts from file");
    }

    public ArrayList<Part> getAllParts() {
        return partList;
    }

    public boolean addPart(Part newPart) {

        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(newPart.getCode())) {
                System.out.println("part with this code already exists");
                return false;
            }
        }

        partList.add(newPart);
        System.out.println("part added: " + newPart.getCode());
        saveParts();
        return true;
    }

    public boolean deletePart(String code) {

        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {
                partList.remove(i);
                System.out.println("part deleted: " + code);
                saveParts();
                return true;
            }
        }

        System.out.println("part not found: " + code);
        return false;
    }

    public boolean updatePart(String code, Part updatedPart) {

        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {

                partList.set(i, updatedPart);
                System.out.println("part updated: " + code);
                saveParts();
                return true;
            }
        }

        System.out.println("part not found: " + code);
        return false;
    }

    public void saveParts() {
        if (partList == null || partList.isEmpty()) {
            System.out.println("Save aborted: partList is empty.");
            return;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for (int i = 0; i < partList.size(); i++) {
                Part p = partList.get(i);

                writer.write(p.getCode() + "," +
                        p.getName() + "," +
                        p.getBrand() + "," +
                        p.getPrice() + "," +
                        p.getQuantity() + "," +
                        p.getCategory() + "," +
                        p.getDate() + "," +
                        p.getLowStockThreshold() + "," +
                        p.getImageFile());

                writer.newLine();
            }

            writer.close();
            System.out.println("parts saved to file");

        } catch (Exception e) {
            System.out.println("couldnt save to file: " + filename);
        }
    }

    public Part findPart(String code) {

        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getCode().equals(code)) {
                return partList.get(i);
            }
        }

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

    public ArrayList<Part> getLowStockParts() {
        ArrayList<Part> lowStock = new ArrayList<>();
        for (int i = 0; i < partList.size(); i++) {
            Part p = partList.get(i);
            if (p.getQuantity() < p.getLowStockThreshold()) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }
}
