package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class Cart {

    private ArrayList<Part> cartItems;

    private ArrayList<Integer> cartQuantities;

    public Cart() {
        cartItems = new ArrayList<>();
        cartQuantities = new ArrayList<>();
    }

    public boolean addItem(Part part, int quantity) {

        if (quantity <= 0) {
            System.out.println("quantity must be more than 0");
            return false;
        }

        if (quantity > part.getQuantity()) {
            System.out.println("not enough stock for: " + part.getName());
            return false;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getCode().equals(part.getCode())) {
                int newQuantity = cartQuantities.get(i) + quantity;

                if (newQuantity > part.getQuantity()) {
                    System.out.println("total quantity exceeds stock");
                    return false;
                }

                cartQuantities.set(i, newQuantity);
                System.out.println("updated quantity for: " + part.getName());
                return true;
            }
        }

        cartItems.add(part);
        cartQuantities.add(quantity);
        System.out.println("added to cart: " + part.getName());
        return true;
    }

    public boolean removeItem(String code) {

        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getCode().equals(code)) {
                cartItems.remove(i);
                cartQuantities.remove(i);
                System.out.println("removed from cart: " + code);
                return true;
            }
        }

        System.out.println("item not found in cart: " + code);
        return false;
    }

    public void clearCart() {
        cartItems.clear();
        cartQuantities.clear();
        System.out.println("cart cleared");
    }

    public ArrayList<Part> getCartItems() {
        return cartItems;
    }

    public ArrayList<Integer> getCartQuantities() {
        return cartQuantities;
    }

    public int getItemCount() {
        return cartItems.size();
    }

    public boolean isEmpty() {
        return cartItems.size() == 0;
    }

    public double calculateTotal() {

        double totalAfterItemDiscounts = 0;

        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (int i = 0; i < cartItems.size(); i++) {
            Part part = cartItems.get(i);
            int qty = cartQuantities.get(i);

            double subtotal = part.getPrice() * qty;

            if (qty >= 3) {
                double discount = subtotal * 0.05;
                subtotal = subtotal - discount;
                System.out.println("bulk discount applied for: " + part.getName());
            }

            totalAfterItemDiscounts = totalAfterItemDiscounts + subtotal;

            if (part.getCategory().equalsIgnoreCase("Engine")) {
                hasEngine = true;
            }
            if (part.getCategory().equalsIgnoreCase("Electrical")) {
                hasElectrical = true;
            }
        }

        double finalTotal = totalAfterItemDiscounts;

        if (hasEngine && hasElectrical) {
            double synergyDiscount = totalAfterItemDiscounts * 0.10;
            finalTotal = totalAfterItemDiscounts - synergyDiscount;
            System.out.println("synergy discount applied - 10% off total");
        }

        return finalTotal;
    }

    public boolean checkout(InventoryManager inventoryManager) {

        if (isEmpty()) {
            System.out.println("cart is empty - nothing to checkout");
            return false;
        }

        for (int i = 0; i < cartItems.size(); i++) {
            Part part = cartItems.get(i);
            int qty = cartQuantities.get(i);

            int newQuantity = part.getQuantity() - qty;

            part.setQuantity(newQuantity);
            inventoryManager.updatePart(part.getCode(), part);
        }

        double total = calculateTotal();
        System.out.println("checkout complete - total: Rs." + total);

        clearCart();

        return true;
    }

}