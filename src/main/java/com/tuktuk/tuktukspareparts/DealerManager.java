package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;
import java.util.Random;

public class DealerManager {

    // full list of all dealers loaded from file
    private ArrayList<Dealer> allDealers;

    // constructor - loads dealers from file
    public DealerManager() {
        allDealers = DataParser.loadDealers("dealers_legacy.txt");
        System.out.println("loaded " + allDealers.size() + " dealers from file");
    }

    public ArrayList<Dealer> getAllDealers() {
        return allDealers;
    }

    // randomly picks 4 unique dealers from the full list
    public ArrayList<Dealer> getRandomDealers() {

        ArrayList<Dealer> selected = new ArrayList<>();
        Random random = new Random();

        // keep trying until we have 4 unique dealers
        while (selected.size() < 4) {

            // pick a random index from the full dealer list
            int randomIndex = random.nextInt(allDealers.size());
            Dealer randomDealer = allDealers.get(randomIndex);

            boolean alreadyPicked = false;

            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).getCode().equals(randomDealer.getCode())) {
                    alreadyPicked = true;
                    break;
                }
            }

            // only add if not already picked
            if (!alreadyPicked) {
                selected.add(randomDealer);
            }
        }

        return selected;
    }

    // sorts the 4 selected dealers alphabetically by location
    public ArrayList<Dealer> getSortedByLocation(ArrayList<Dealer> dealers) {

        ArrayList<Dealer> sorted = new ArrayList<>(dealers);

        int n = sorted.size();

        // bubble sort
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                String currentLocation = sorted.get(j).getLocation();
                String nextLocation = sorted.get(j + 1).getLocation();

                if (currentLocation.compareTo(nextLocation) > 0) {

                    // swap them
                    Dealer temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        return sorted;
    }

    // picks 4 random unique dealers and returns them sorted by location
    public ArrayList<Dealer> getRandomDealersSortedByLocation() {

        ArrayList<Dealer> randomDealers = getRandomDealers();

        ArrayList<Dealer> sortedDealers = getSortedByLocation(randomDealers);

        return sortedDealers;
    }

}