package com.tuktuk.tuktukspareparts;

import java.util.ArrayList;

public class Cart {

    // list to hold items added to the cart
    private ArrayList<Part> cartItems;

    // list to hold the quantity for each item in the cart
    private ArrayList<Integer> cartQuantities;

    public Cart() {
        cartItems = new ArrayList<>();
        cartQuantities = new ArrayList<>();
    }

    // adds a part and quantity to the cart
    public boolean addItem(Part part, int quantity) {

        if (quantity <= 0) {
            System.out.println("quantity must be more than 0");
            return false;
        }

        // cant add more than whats in stock
        if (quantity > part.getQuantity()) {
            System.out.println("not enough stock for: " + part.getName());
            return false;
        }

        // check if this part is already in the cart
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getCode().equals(part.getCode())) {
                // update quantity instead of adding again
                int newQuantity = cartQuantities.get(i) + quantity;

                // make sure combined quantity doesnt exceed stock
                if (newQuantity > part.getQuantity()) {
                    System.out.println("total quantity exceeds stock");
                    return false;
                }

                cartQuantities.set(i, newQuantity);
                System.out.println("updated quantity for: " + part.getName());
                return true;
            }
        }

        // part not in cart yet so add it
        cartItems.add(part);
        cartQuantities.add(quantity);
        System.out.println("added to cart: " + part.getName());
        return true;
    }

    // removes an item from the cart by its part code
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

    // empties the cart completely
    public void clearCart() {
        cartItems.clear();
        cartQuantities.clear();
        System.out.println("cart cleared");
    }

    // returns the list of items in the cart
    public ArrayList<Part> getCartItems() {
        return cartItems;
    }

    // returns the list of quantities in the cart
    public ArrayList<Integer> getCartQuantities() {
        return cartQuantities;
    }

    // returns how many different items are in the cart
    public int getItemCount() {
        return cartItems.size();
    }

    // checks if cart is empty
    public boolean isEmpty() {
        return cartItems.size() == 0;
    }

    // calculates the final total with all discounts applied
    public double calculateTotal() {

        // calculate subtotal for each item
        double totalAfterItemDiscounts = 0;

        // also check if cart has engine and electrical parts for discount
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (int i = 0; i < cartItems.size(); i++) {
            Part part = cartItems.get(i);
            int qty = cartQuantities.get(i);

            // calculate subtotal for this item
            double subtotal = part.getPrice() * qty;

            // check for bulk discount
            if (qty >= 3) {
                double discount = subtotal * 0.05;
                subtotal = subtotal - discount;
                System.out.println("bulk discount applied for: " + part.getName());
            }

            // add this items subtotal to running total
            totalAfterItemDiscounts = totalAfterItemDiscounts + subtotal;

            // check category for discount
            if (part.getCategory().equalsIgnoreCase("Engine")) {
                hasEngine = true;
            }
            if (part.getCategory().equalsIgnoreCase("Electrical")) {
                hasElectrical = true;
            }
        }

        // apply discount if cart has both engine and electrical
        double finalTotal = totalAfterItemDiscounts;

        if (hasEngine && hasElectrical) {
            double synergyDiscount = totalAfterItemDiscounts * 0.10;
            finalTotal = totalAfterItemDiscounts - synergyDiscount;
            System.out.println("synergy discount applied - 10% off total");
        }

        return finalTotal;
    }

}