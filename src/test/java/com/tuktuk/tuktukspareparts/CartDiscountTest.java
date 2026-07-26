package com.tuktuk.tuktukspareparts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CartDiscountTest {

    private Cart cart;
    private InventoryManager inventoryManager;
    private Part enginePart;
    private Part electricalPart;
    private Part brakesPart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
        inventoryManager = new InventoryManager();
        enginePart = new Part("P001", "Engine Part", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        electricalPart = new Part("P002", "Electrical Part", "Brand",
                1000.0, 10, "Electrical", "2024-01-01", 5, "test.jpg");
        brakesPart = new Part("P003", "Brakes Part", "Brand",
                500.0, 10, "Brakes", "2024-01-01", 5, "test.jpg");
    }

    @Test
    public void testBasicTotalNoDiscount() {
        cart.addItem(enginePart, 1);
        cart.addItem(brakesPart, 2);
        assertEquals(2000.0, cart.calculateTotal());
    }

    @Test
    public void testBulkDiscountAtThreeUnits() {
        cart.addItem(enginePart, 3);
        assertEquals(2850.0, cart.calculateTotal());
    }

    @Test
    public void testBulkDiscountAboveThreeUnits() {
        cart.addItem(enginePart, 5);
        assertEquals(4750.0, cart.calculateTotal());
    }

    @Test
    public void testNoBulkDiscountForTwo() {
        cart.addItem(enginePart, 2);
        assertEquals(2000.0, cart.calculateTotal());
    }

    @Test
    public void testSynergyDiscountApplied() {
        cart.addItem(enginePart, 1);
        cart.addItem(electricalPart, 1);
        assertEquals(1800.0, cart.calculateTotal());
    }

    @Test
    public void testNoSynergyWithoutElectrical() {
        cart.addItem(enginePart, 1);
        cart.addItem(brakesPart, 1);
        assertEquals(1500.0, cart.calculateTotal());
    }

    @Test
    public void testNoSynergyWithoutEngine() {
        cart.addItem(electricalPart, 1);
        cart.addItem(brakesPart, 1);
        assertEquals(1500.0, cart.calculateTotal());
    }

    @Test
    public void testBothDiscountsCorrectOrder() {
        cart.addItem(enginePart, 3);
        cart.addItem(electricalPart, 1);

        assertEquals(3465.0, cart.calculateTotal());
    }

    @Test
    public void testStockDeductedAfterCheckout() {
        inventoryManager.addPart(enginePart);
        Part found = inventoryManager.findPart("P001");
        cart.addItem(found, 3);
        cart.checkout(inventoryManager);
        assertEquals(7, inventoryManager.findPart("P001").getQuantity());
    }

    @Test
    public void testMultipleStockDeductions() {
        Part eng = new Part("P001", "Engine Part", "Brand",
                1000.0, 10, "Engine", "2024-01-01", 5, "test.jpg");
        Part elec = new Part("P002", "Electrical Part", "Brand",
                1000.0, 8, "Electrical", "2024-01-01", 5, "test.jpg");
        inventoryManager.addPart(eng);
        inventoryManager.addPart(elec);
        cart.addItem(inventoryManager.findPart("P001"), 2);
        cart.addItem(inventoryManager.findPart("P002"), 3);
        cart.checkout(inventoryManager);
        assertEquals(8, inventoryManager.findPart("P001").getQuantity());
        assertEquals(5, inventoryManager.findPart("P002").getQuantity());
    }

    @Test
    public void testCannotAddZeroQuantity() {
        boolean result = cart.addItem(enginePart, 0);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    @Test
    public void testCannotAddNegativeQuantity() {
        boolean result = cart.addItem(enginePart, -5);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    @Test
    public void testCannotExceedStock() {
        boolean result = cart.addItem(enginePart, 15);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    @Test
    public void testCannotCheckoutEmptyCart() {
        boolean result = cart.checkout(inventoryManager);
        assertFalse(result);
    }

    @Test
    public void testCartClearedAfterCheckout() {
        inventoryManager.addPart(enginePart);
        Part found = inventoryManager.findPart("P001");
        cart.addItem(found, 2);
        cart.checkout(inventoryManager);
        assertTrue(cart.isEmpty());
    }
}