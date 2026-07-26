package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class InventorySorter {
    public static void sortByCode(ArrayList<Part> parts) {

        int n = parts.size();

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                String currentCode = parts.get(j).getCode();
                String nextCode = parts.get(j + 1).getCode();

                if (currentCode.compareTo(nextCode) > 0) {

                    Part temp = parts.get(j);
                    parts.set(j, parts.get(j + 1));
                    parts.set(j + 1, temp);
                }
            }
        }
    }

    public static void sortByCategory(ArrayList<Part> parts) {

        int n = parts.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                String currentCategory = parts.get(j).getCategory();
                String nextCategory = parts.get(j + 1).getCategory();

                if (currentCategory.compareTo(nextCategory) > 0) {

                    Part temp = parts.get(j);
                    parts.set(j, parts.get(j + 1));
                    parts.set(j + 1, temp);
                }
            }
        }
    }

    public static ArrayList<Part> getSortedParts(ArrayList<Part> parts) {

        if (parts.size() == 0) {
            System.out.println("no parts to sort");
            return new ArrayList<>();
        }

        ArrayList<Part> sortedList = new ArrayList<>(parts);

        sortByCode(sortedList);

        sortByCategory(sortedList);

        return sortedList;
    }
}
