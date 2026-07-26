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

    // test 1 - basic total without any discount
    @Test
    public void testBasicTotalNoDiscount() {
        cart.addItem(enginePart, 1);
        cart.addItem(brakesPart, 2);
        // 1x1000 + 2x500 = 2000
        assertEquals(2000.0, cart.calculateTotal());
    }

    // test 2 - bulk discount applied for exactly 3 units
    @Test
    public void testBulkDiscountAtThreeUnits() {
        cart.addItem(enginePart, 3);
        // 3x1000 = 3000, 5% off = 150, total = 2850
        assertEquals(2850.0, cart.calculateTotal());
    }

    // test 3 - bulk discount applied for more than 3 units
    @Test
    public void testBulkDiscountAboveThreeUnits() {
        cart.addItem(enginePart, 5);
        // 5x1000 = 5000, 5% off = 250, total = 4750
        assertEquals(4750.0, cart.calculateTotal());
    }

    // test 4 - no bulk discount for exactly 2 units
    @Test
    public void testNoBulkDiscountForTwo() {
        cart.addItem(enginePart, 2);
        // 2x1000 = 2000, no discount
        assertEquals(2000.0, cart.calculateTotal());
    }

    // test 5 - synergy discount applied when engine and electrical in cart
    @Test
    public void testSynergyDiscountApplied() {
        cart.addItem(enginePart, 1);
        cart.addItem(electricalPart, 1);
        // 1000 + 1000 = 2000, 10% synergy = 200, total = 1800
        assertEquals(1800.0, cart.calculateTotal());
    }

    // test 6 - no synergy discount without electrical part
    @Test
    public void testNoSynergyWithoutElectrical() {
        cart.addItem(enginePart, 1);
        cart.addItem(brakesPart, 1);
        // 1000 + 500 = 1500, no synergy discount
        assertEquals(1500.0, cart.calculateTotal());
    }

    // test 7 - no synergy discount without engine part
    @Test
    public void testNoSynergyWithoutEngine() {
        cart.addItem(electricalPart, 1);
        cart.addItem(brakesPart, 1);
        // 1000 + 500 = 1500, no synergy discount
        assertEquals(1500.0, cart.calculateTotal());
    }

    // test 8 - both discounts applied in correct order
    @Test
    public void testBothDiscountsCorrectOrder() {
        cart.addItem(enginePart, 3);
        cart.addItem(electricalPart, 1);

        assertEquals(3465.0, cart.calculateTotal());
    }

    // test 9 - stock reduced correctly after checkout
    @Test
    public void testStockDeductedAfterCheckout() {
        inventoryManager.addPart(enginePart);
        Part found = inventoryManager.findPart("P001");
        cart.addItem(found, 3);
        cart.checkout(inventoryManager);
        // 10 - 3 = 7 remaining
        assertEquals(7, inventoryManager.findPart("P001").getQuantity());
    }

    // test 10 - stock of multiple items deducted correctly
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

    // test 11 - cannot add zero quantity to cart
    @Test
    public void testCannotAddZeroQuantity() {
        boolean result = cart.addItem(enginePart, 0);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    // test 12 - cannot add negative quantity to cart
    @Test
    public void testCannotAddNegativeQuantity() {
        boolean result = cart.addItem(enginePart, -5);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    // test 13 - cannot add more than available stock
    @Test
    public void testCannotExceedStock() {
        // enginePart has 10 in stock
        boolean result = cart.addItem(enginePart, 15);
        assertFalse(result);
        assertEquals(0, cart.getItemCount());
    }

    // test 14 - cannot checkout empty cart
    @Test
    public void testCannotCheckoutEmptyCart() {
        boolean result = cart.checkout(inventoryManager);
        assertFalse(result);
    }

    // test 15 - cart is empty after checkout
    @Test
    public void testCartClearedAfterCheckout() {
        inventoryManager.addPart(enginePart);
        Part found = inventoryManager.findPart("P001");
        cart.addItem(found, 2);
        cart.checkout(inventoryManager);
        assertTrue(cart.isEmpty());
    }
}