package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class SearchManager {

    public static ArrayList<Part> search(ArrayList<Part> allParts,
            String categoryFilter,
            String keywordFilter,
            double minPrice,
            double maxPrice) {

        ArrayList<Part> results = new ArrayList<>();

        for (int i = 0; i < allParts.size(); i++) {

            Part part = allParts.get(i);

            boolean matches = true;

            if (!categoryFilter.isEmpty()) {
                if (!part.getCategory().equalsIgnoreCase(categoryFilter)) {
                    matches = false;
                }
            }

            if (!keywordFilter.isEmpty()) {
                boolean nameContains = part.getName().toLowerCase()
                        .contains(keywordFilter.toLowerCase());
                boolean brandContains = part.getBrand().toLowerCase()
                        .contains(keywordFilter.toLowerCase());

                if (!nameContains && !brandContains) {
                    matches = false;
                }
            }

            if (minPrice > 0 || maxPrice > 0) {
                if (part.getPrice() < minPrice || part.getPrice() > maxPrice) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(part);
            }
        }

        return results;
    }

    public static Part findByCode(ArrayList<Part> allParts, String code) {

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getCode().equalsIgnoreCase(code)) {
                return allParts.get(i);
            }
        }

        return null;
    }

}