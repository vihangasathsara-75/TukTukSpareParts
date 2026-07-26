package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;
import java.util.Random;

public class DealerManager {

    private ArrayList<Dealer> allDealers;

    public DealerManager() {
        allDealers = DataParser.loadDealers("dealers_legacy.txt");
        System.out.println("loaded " + allDealers.size() + " dealers from file");
    }

    public ArrayList<Dealer> getAllDealers() {
        return allDealers;
    }

    public ArrayList<Dealer> getRandomDealers() {

        ArrayList<Dealer> selected = new ArrayList<>();
        Random random = new Random();

        while (selected.size() < 4) {

            int randomIndex = random.nextInt(allDealers.size());
            Dealer randomDealer = allDealers.get(randomIndex);

            boolean alreadyPicked = false;

            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).getCode().equals(randomDealer.getCode())) {
                    alreadyPicked = true;
                    break;
                }
            }

            if (!alreadyPicked) {
                selected.add(randomDealer);
            }
        }

        return selected;
    }

    public ArrayList<Dealer> getSortedByLocation(ArrayList<Dealer> dealers) {

        ArrayList<Dealer> sorted = new ArrayList<>(dealers);

        int n = sorted.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                String currentLocation = sorted.get(j).getLocation();
                String nextLocation = sorted.get(j + 1).getLocation();

                if (currentLocation.compareTo(nextLocation) > 0) {

                    Dealer temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        return sorted;
    }

    public ArrayList<Dealer> getRandomDealersSortedByLocation() {

        ArrayList<Dealer> randomDealers = getRandomDealers();

        ArrayList<Dealer> sortedDealers = getSortedByLocation(randomDealers);

        return sortedDealers;
    }

}