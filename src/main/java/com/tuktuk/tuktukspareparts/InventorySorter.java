package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class InventorySorter {
    // sorts parts by part code in ascending order using bubble sort
    public static void sortByCode(ArrayList<Part> parts) {

        int n = parts.size();

        // outer loop - to go through the list n times
        for (int i = 0; i < n - 1; i++) {

            // inner loop - comparing values one by one
            for (int j = 0; j < n - i - 1; j++) {

                // get the code of current part and next part
                String currentCode = parts.get(j).getCode();
                String nextCode = parts.get(j + 1).getCode();

                // compare them alphabetically
                // if current comes after next, swap them
                if (currentCode.compareTo(nextCode) > 0) {

                    // swap the two parts
                    Part temp = parts.get(j);
                    parts.set(j, parts.get(j + 1));
                    parts.set(j + 1, temp);
                }
            }
        }
    }
    // sorts parts by category name alphabetically
    public static void sortByCategory(ArrayList<Part> parts) {

        int n = parts.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                String currentCategory = parts.get(j).getCategory();
                String nextCategory = parts.get(j + 1).getCategory();

                // if current category comes after next category, swap
                if (currentCategory.compareTo(nextCategory) > 0) {

                    Part temp = parts.get(j);
                    parts.set(j, parts.get(j + 1));
                    parts.set(j + 1, temp);
                }
            }
        }
    }
    // it first sorts by category, then sorts by code within each category
    public static ArrayList<Part> getSortedParts(ArrayList<Part> parts) {

        if (parts.size() == 0) {
            System.out.println("no parts to sort");
            return new ArrayList<>();
        }

        // make a copy so that dont change the original list
        ArrayList<Part> sortedList = new ArrayList<>(parts);

        sortByCode(sortedList);

        sortByCategory(sortedList);

        return sortedList;
    }
}
