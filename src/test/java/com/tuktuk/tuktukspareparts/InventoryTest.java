package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;

public class InventoryTest {

    private InventoryManager inventoryManager;

    @BeforeEach
    public void setUp() {
        inventoryManager = new InventoryManager();
    }

    @Test
    public void testAddPartIncreasesCount() {
        Part part = new Part("P001", "Test Part", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part);
        assertEquals(1, inventoryManager.getTotalCount());
    }

    @Test
    public void testDuplicateCodeRejected() {
        Part part1 = new Part("P001", "Part One", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        Part part2 = new Part("P001", "Part Two", "Brand",
                2000.0, 5, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part1);
        boolean result = inventoryManager.addPart(part2);
        assertFalse(result);
        // count should still be 1
        assertEquals(1, inventoryManager.getTotalCount());
    }

    @Test
    public void testDeletePartDecreasesCount() {
        Part part = new Part("P001", "Test Part", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part);
        inventoryManager.deletePart("P001");
        assertEquals(0, inventoryManager.getTotalCount());
    }

    @Test
    public void testDeleteNonExistentReturnsFlase() {
        boolean result = inventoryManager.deletePart("P999");
        assertFalse(result);
    }

    @Test
    public void testUpdatePartChangesDetails() {
        Part original = new Part("P001", "Old Name", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(original);
        Part updated = new Part("P001", "New Name", "NewBrand",
                2000.0, 5, "Electrical", "2024-01-01", 5, "test.jpg");
        inventoryManager.updatePart("P001", updated);
        Part found = inventoryManager.findPart("P001");
        assertEquals("New Name", found.getName());
        assertEquals(2000.0, found.getPrice());
    }

    @Test
    public void testFindPartByCode() {
        Part part = new Part("P001", "Test Part", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part);
        Part found = inventoryManager.findPart("P001");
        assertNotNull(found);
        assertEquals("Test Part", found.getName());
    }

    @Test
    public void testFindNonExistentReturnsNull() {
        Part found = inventoryManager.findPart("P999");
        assertNull(found);
    }

    @Test
    public void testTotalValueCalculation() {
        Part part1 = new Part("P001", "Part One", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        Part part2 = new Part("P002", "Part Two", "Brand",
                500.0, 4, "Brakes", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part1);
        inventoryManager.addPart(part2);
        assertEquals(12000.0, inventoryManager.getTotalValue());
    }

    @Test
    public void testLowStockUsesPerPartThreshold() {
        Part lowPart = new Part("P001", "Low Part", "Brand",
                1000.0, 3, "Engine", "2024-01-01", 10, "test.jpg");
        Part okPart = new Part("P002", "Ok Part", "Brand",
                1000.0, 8, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(lowPart);
        inventoryManager.addPart(okPart);
        ArrayList<Part> lowStock = inventoryManager.getLowStockParts();
        assertEquals(1, lowStock.size());
        assertEquals("P001", lowStock.get(0).getCode());
    }

    @Test
    public void testZeroQuantityIsLowStock() {
        Part part = new Part("P001", "Out of Stock", "Brand",
                1000.0, 0, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part);
        ArrayList<Part> lowStock = inventoryManager.getLowStockParts();
        assertEquals(1, lowStock.size());
    }

    @Test
    public void testPartAtThresholdIsNotLowStock() {
        // quantity equals threshold - not low stock
        Part part = new Part("P001", "Test Part", "Brand",
                1000.0, 5, "Engine", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(part);
        ArrayList<Part> lowStock = inventoryManager.getLowStockParts();
        // quantity 5 is NOT less than threshold 5
        assertEquals(0, lowStock.size());
    }

    @Test
    public void testMultipleLowStockPartsDetected() {
        Part part1 = new Part("P001", "Low One", "Brand",
                1000.0, 2, "Engine", "2024-01-01", 10, "test.jpg");
        Part part2 = new Part("P002", "Low Two", "Brand",
                500.0, 1, "Brakes", "2024-01-01", 10, "test.jpg");
        Part part3 = new Part("P003", "Ok Part", "Brand",
                800.0, 15, "Bodywork", "2024-01-01", 10, "test.jpg");
        inventoryManager.addPart(part1);
        inventoryManager.addPart(part2);
        inventoryManager.addPart(part3);
        ArrayList<Part> lowStock = inventoryManager.getLowStockParts();
        assertEquals(2, lowStock.size());
    }

    @Test
    public void testAuditLogCreatedOnAdd() {
        AuditLogger logger = new AuditLogger();
        logger.logAddPart("P001", "Test Part", 10);
        File logFile = new File("audit_log.txt");
        assertTrue(logFile.exists());
    }

    @Test
    public void testAuditLogCreatedOnDelete() {
        AuditLogger logger = new AuditLogger();
        logger.logDeletePart("P001", "Test Part");
        File logFile = new File("audit_log.txt");
        assertTrue(logFile.exists());
    }

    @Test
    public void testAuditLogCreatedOnCheckout() {
        AuditLogger logger = new AuditLogger();
        ArrayList<Part> items = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        items.add(new Part("P001", "Test", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg"));
        quantities.add(2);
        logger.logCheckout(items, quantities, 2000.0);
        File logFile = new File("audit_log.txt");
        assertTrue(logFile.exists());
    }
}