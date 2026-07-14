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

}