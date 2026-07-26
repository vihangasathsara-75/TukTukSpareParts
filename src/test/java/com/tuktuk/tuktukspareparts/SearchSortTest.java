package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class SearchSortTest {

    private ArrayList<Part> testParts;

    @BeforeEach
    public void setUp() {
        testParts = new ArrayList<>();
        testParts.add(new Part("P001", "Bajaj Piston", "Bajaj",
                4500.0, 15, "Engine", "2024-01-01", 5, "test.jpg"));
        testParts.add(new Part("P002", "TVS Brake Pad", "TVS",
                1250.0, 8, "Brakes", "2024-01-01", 5, "test.jpg"));
        testParts.add(new Part("P003", "205 Tyre", "Local",
                6500.0, 24, "Bodywork", "2024-01-01", 5, "test.jpg"));
        testParts.add(new Part("P004", "Spark Plug NGK", "NGK",
                850.0, 50, "Electrical", "2024-01-01", 5, "test.jpg"));
        testParts.add(new Part("P005", "Clutch Cable", "Bajaj",
                950.0, 12, "Engine", "2024-01-01", 5, "test.jpg"));
    }

    // test 1 - search by category returns only matching parts
    @Test
    public void testSearchByCategory() {
        ArrayList<Part> results = SearchManager.search(testParts, "Engine", "", 0, 0);
        assertEquals(2, results.size());
        // both results should be Engine category
        for (int i = 0; i < results.size(); i++) {
            assertEquals("Engine", results.get(i).getCategory());
        }
    }

    // test 2 - search by keyword finds in part name
    @Test
    public void testSearchByKeywordInName() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "Bajaj", 0, 0);
        // P001 Bajaj Piston and P005 Clutch Cable by Bajaj brand
        assertEquals(2, results.size());
    }

    // test 3 - search by keyword finds in brand name
    @Test
    public void testSearchByKeywordInBrand() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "NGK", 0, 0);
        assertEquals(1, results.size());
        assertEquals("P004", results.get(0).getCode());
    }

    // test 4 - search by price range returns parts within range
    @Test
    public void testSearchByPriceRange() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 800, 1300);
        // P002 (1250) and P004 (850) and P005 (950) are in range
        assertEquals(3, results.size());
    }

    // test 5 - search with all three filters combined
    @Test
    public void testSearchThreeFiltersCombined() {
        ArrayList<Part> results = SearchManager.search(testParts, "Engine", "Bajaj", 500, 1000);
        // only P005 Clutch Cable matches Engine + Bajaj brand + price 950
        assertEquals(1, results.size());
        assertEquals("P005", results.get(0).getCode());
    }

    // test 6 - empty search returns all parts
    @Test
    public void testEmptySearchReturnsAll() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 0, 0);
        assertEquals(5, results.size());
    }

    // test 7 - search with no matching results returns empty list
    @Test
    public void testSearchNoResults() {
        ArrayList<Part> results = SearchManager.search(testParts, "Brakes", "Bajaj", 0, 0);
        // no Bajaj brand parts in Brakes category
        assertEquals(0, results.size());
    }

    // test 8 - search is case insensitive for category
    @Test
    public void testSearchCaseInsensitiveCategory() {
        ArrayList<Part> results1 = SearchManager.search(testParts, "engine", "", 0, 0);
        ArrayList<Part> results2 = SearchManager.search(testParts, "ENGINE", "", 0, 0);
        ArrayList<Part> results3 = SearchManager.search(testParts, "Engine", "", 0, 0);
        // all three should return same results
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
    }

    // test 9 - search is case insensitive for keyword
    @Test
    public void testSearchCaseInsensitiveKeyword() {
        ArrayList<Part> results1 = SearchManager.search(testParts, "", "bajaj", 0, 0);
        ArrayList<Part> results2 = SearchManager.search(testParts, "", "BAJAJ", 0, 0);
        assertEquals(results1.size(), results2.size());
    }

    // test 10 - search by max price only works correctly
    @Test
    public void testSearchByMaxPriceOnly() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 0, 1000);
        // P002(1250) above 1000 so excluded, P004(850), P005(950) included
        // but min is 0 so all below 1000 included
        for (int i = 0; i < results.size(); i++) {
            assertTrue(results.get(i).getPrice() <= 1000);
        }
    }

    // test 11 - sorting groups parts by category alphabetically
    @Test
    public void testSortingGroupsByCategory() {
        ArrayList<Part> sorted = InventorySorter.getSortedParts(testParts);
        // Bodywork comes before Brakes, Brakes before Electrical, Electrical before
        // Engine
        assertEquals("Bodywork", sorted.get(0).getCategory());
        assertEquals("Brakes", sorted.get(1).getCategory());
        assertEquals("Electrical", sorted.get(2).getCategory());
        assertEquals("Engine", sorted.get(3).getCategory());
        assertEquals("Engine", sorted.get(4).getCategory());
    }

    // test 12 - sorting orders by part code within same category
    @Test
    public void testSortingByCodeWithinCategory() {
        ArrayList<Part> parts = new ArrayList<>();
        parts.add(new Part("P005", "Engine C", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg"));
        parts.add(new Part("P001", "Engine A", "Brand",
                500.0, 5, "Engine", "2024-01-01", 5, "test.jpg"));
        parts.add(new Part("P003", "Engine B", "Brand",
                800.0, 8, "Engine", "2024-01-01", 5, "test.jpg"));
        ArrayList<Part> sorted = InventorySorter.getSortedParts(parts);
        // within Engine category codes should be P001, P003, P005
        assertEquals("P001", sorted.get(0).getCode());
        assertEquals("P003", sorted.get(1).getCode());
        assertEquals("P005", sorted.get(2).getCode());
    }

    // test 13 - sorting empty list returns empty list without crashing
    @Test
    public void testSortingEmptyList() {
        ArrayList<Part> empty = new ArrayList<>();
        ArrayList<Part> sorted = InventorySorter.getSortedParts(empty);
        assertEquals(0, sorted.size());
    }

    // test 14 - sorting does not change original list
    @Test
    public void testSortingDoesNotChangeOriginal() {
        String firstCodeBefore = testParts.get(0).getCode();
        InventorySorter.getSortedParts(testParts);
        // original list should be unchanged
        assertEquals(firstCodeBefore, testParts.get(0).getCode());
    }

    // test 15 - sorting single item list works
    @Test
    public void testSortingSingleItem() {
        ArrayList<Part> single = new ArrayList<>();
        single.add(new Part("P001", "Test", "Brand",
                100.0, 5, "Engine", "2024-01-01", 5, "test.jpg"));
        ArrayList<Part> sorted = InventorySorter.getSortedParts(single);
        assertEquals(1, sorted.size());
        assertEquals("P001", sorted.get(0).getCode());
    }
}