package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class SearchManager {

    // searches through all parts and returns ones that match the filters
    public static ArrayList<Part> search(ArrayList<Part> allParts,
                                         String categoryFilter,
                                         String keywordFilter,
                                         double minPrice,
                                         double maxPrice) {

        // holds all the parts that match
        ArrayList<Part> results = new ArrayList<>();

        // go through every part one by one
        for (int i = 0; i < allParts.size(); i++) {

            Part part = allParts.get(i);

            // assume this part matches until proven false
            boolean matches = true;

            // check category filter
            if (!categoryFilter.isEmpty()) {
                if (!part.getCategory().equalsIgnoreCase(categoryFilter)) {
                    matches = false;
                }
            }

            // checks if part name OR brand contains the keyword
            if (!keywordFilter.isEmpty()) {
                boolean nameContains = part.getName().toLowerCase()
                        .contains(keywordFilter.toLowerCase());
                boolean brandContains = part.getBrand().toLowerCase()
                        .contains(keywordFilter.toLowerCase());

                if (!nameContains && !brandContains) {
                    matches = false;
                }
            }

            // only check if user set a price range (both min and max above 0)
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

}