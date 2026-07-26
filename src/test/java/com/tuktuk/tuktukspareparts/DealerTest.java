package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class DealerTest {

    private DealerManager dealerManager;

    @BeforeEach
    public void setUp() {
        dealerManager = new DealerManager();
    }

    @Test
    public void testExactlyFourDealersSelected() {
        ArrayList<Dealer> selected = dealerManager.getRandomDealers();
        assertEquals(4, selected.size());
    }

    @Test
    public void testNoDuplicateDealers() {
        ArrayList<Dealer> selected = dealerManager.getRandomDealers();
        for (int i = 0; i < selected.size(); i++) {
            for (int j = i + 1; j < selected.size(); j++) {
                assertNotEquals(selected.get(i).getCode(),
                        selected.get(j).getCode(),
                        "Duplicate dealer found: " + selected.get(i).getCode());
            }
        }
    }

    @Test
    public void testDealersSortedByLocation() {
        ArrayList<Dealer> sorted = dealerManager.getRandomDealersSortedByLocation();
        for (int i = 0; i < sorted.size() - 1; i++) {
            String current = sorted.get(i).getLocation();
            String next = sorted.get(i + 1).getLocation();
            assertTrue(current.compareTo(next) <= 0,
                    "Dealers not sorted: " + current + " should come before " + next);
        }
    }

    @Test
    public void testGetRandomDealersSortedReturnsCorrectCount() {
        ArrayList<Dealer> result = dealerManager.getRandomDealersSortedByLocation();
        assertEquals(4, result.size());
    }

    @Test
    public void testMultipleSelectionsAlwaysGiveFourUnique() {
        for (int run = 0; run < 5; run++) {
            ArrayList<Dealer> selected = dealerManager.getRandomDealers();
            assertEquals(4, selected.size());
            for (int i = 0; i < selected.size(); i++) {
                for (int j = i + 1; j < selected.size(); j++) {
                    assertNotEquals(selected.get(i).getCode(), selected.get(j).getCode());
                }
            }
        }
    }

    @Test
    public void testDealerBubbleSortByLocation() {
        ArrayList<Dealer> dealers = new ArrayList<>();
        dealers.add(new Dealer("D001", "Shop C", "0771111111", "Malabe"));
        dealers.add(new Dealer("D002", "Shop A", "0772222222", "Athurugiriya"));
        dealers.add(new Dealer("D003", "Shop B", "0773333333", "Kaduwela"));
        dealers.add(new Dealer("D004", "Shop D", "0774444444", "Pittugala"));
        ArrayList<Dealer> sorted = dealerManager.getSortedByLocation(dealers);
        assertEquals("Athurugiriya", sorted.get(0).getLocation());
        assertEquals("Kaduwela", sorted.get(1).getLocation());
        assertEquals("Malabe", sorted.get(2).getLocation());
        assertEquals("Pittugala", sorted.get(3).getLocation());
    }

    @Test
    public void testDealerManagerLoadsFromFile() {
        ArrayList<Dealer> all = dealerManager.getAllDealers();
        assertEquals(8, all.size());
    }

    @Test
    public void testSelectedDealersAreFromFile() {
        ArrayList<Dealer> all = dealerManager.getAllDealers();
        ArrayList<Dealer> selected = dealerManager.getRandomDealers();
        for (int i = 0; i < selected.size(); i++) {
            boolean foundInAll = false;
            for (int j = 0; j < all.size(); j++) {
                if (selected.get(i).getCode().equals(all.get(j).getCode())) {
                    foundInAll = true;
                    break;
                }
            }
            assertTrue(foundInAll,
                    "Selected dealer not from file: " + selected.get(i).getCode());
        }
    }

    @Test
    public void testDealerHasAllFields() {
        ArrayList<Dealer> all = dealerManager.getAllDealers();
        for (int i = 0; i < all.size(); i++) {
            assertNotNull(all.get(i).getCode());
            assertNotNull(all.get(i).getName());
            assertNotNull(all.get(i).getContactNumber());
            assertNotNull(all.get(i).getLocation());
            assertFalse(all.get(i).getCode().isEmpty());
            assertFalse(all.get(i).getName().isEmpty());
        }
    }

    @Test
    public void testSortedDealersKeepAllInfo() {
        ArrayList<Dealer> sorted = dealerManager.getRandomDealersSortedByLocation();
        for (int i = 0; i < sorted.size(); i++) {
            assertNotNull(sorted.get(i).getName());
            assertFalse(sorted.get(i).getName().isEmpty());
        }
    }
}