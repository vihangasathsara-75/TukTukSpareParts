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

    @Test
    public void testSearchByCategory() {
        ArrayList<Part> results = SearchManager.search(testParts, "Engine", "", 0, 0);
        for (int i = 0; i < results.size(); i++) {
            assertEquals("Engine", results.get(i).getCategory());
        }
    }

    @Test
    public void testSearchByKeywordInName() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "Bajaj", 0, 0);
        assertEquals(2, results.size());
    }

    @Test
    public void testSearchByKeywordInBrand() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "NGK", 0, 0);
        assertEquals(1, results.size());
        assertEquals("P004", results.get(0).getCode());
    }

    @Test
    public void testSearchByPriceRange() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 800, 1300);
        assertEquals(3, results.size());
    }

    @Test
    public void testSearchThreeFiltersCombined() {
        ArrayList<Part> results = SearchManager.search(testParts, "Engine", "Bajaj", 500, 1000);
        assertEquals(1, results.size());
        assertEquals("P005", results.get(0).getCode());
    }

    @Test
    public void testEmptySearchReturnsAll() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 0, 0);
        assertEquals(5, results.size());
    }

    @Test
    public void testSearchNoResults() {
        ArrayList<Part> results = SearchManager.search(testParts, "Brakes", "Bajaj", 0, 0);
        assertEquals(0, results.size());
    }

    @Test
    public void testSearchCaseInsensitiveCategory() {
        ArrayList<Part> results1 = SearchManager.search(testParts, "engine", "", 0, 0);
        ArrayList<Part> results2 = SearchManager.search(testParts, "ENGINE", "", 0, 0);
        ArrayList<Part> results3 = SearchManager.search(testParts, "Engine", "", 0, 0);
        // all three should return same results
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
    }

    @Test
    public void testSearchCaseInsensitiveKeyword() {
        ArrayList<Part> results1 = SearchManager.search(testParts, "", "bajaj", 0, 0);
        ArrayList<Part> results2 = SearchManager.search(testParts, "", "BAJAJ", 0, 0);
        assertEquals(results1.size(), results2.size());
    }

    @Test
    public void testSearchByMaxPriceOnly() {
        ArrayList<Part> results = SearchManager.search(testParts, "", "", 0, 1000);
        for (int i = 0; i < results.size(); i++) {
            assertTrue(results.get(i).getPrice() <= 1000);
        }
    }

    @Test
    public void testSortingGroupsByCategory() {
        ArrayList<Part> sorted = InventorySorter.getSortedParts(testParts);
        assertEquals("Bodywork", sorted.get(0).getCategory());
        assertEquals("Brakes", sorted.get(1).getCategory());
        assertEquals("Electrical", sorted.get(2).getCategory());
        assertEquals("Engine", sorted.get(3).getCategory());
        assertEquals("Engine", sorted.get(4).getCategory());
    }

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
        assertEquals("P001", sorted.get(0).getCode());
        assertEquals("P003", sorted.get(1).getCode());
        assertEquals("P005", sorted.get(2).getCode());
    }

    @Test
    public void testSortingEmptyList() {
        ArrayList<Part> empty = new ArrayList<>();
        ArrayList<Part> sorted = InventorySorter.getSortedParts(empty);
        assertEquals(0, sorted.size());
    }

    @Test
    public void testSortingDoesNotChangeOriginal() {
        String firstCodeBefore = testParts.get(0).getCode();
        InventorySorter.getSortedParts(testParts);
        assertEquals(firstCodeBefore, testParts.get(0).getCode());
    }

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