package com.tuktuk.tuktukspareparts;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class CartController {

    @FXML private TextField txtPartCode;
    @FXML private TextField txtQuantity;
    @FXML private TableView<Part> cartTable;
    @FXML private TableColumn<Part, String> colCode;
    @FXML private TableColumn<Part, String> colName;
    @FXML private TableColumn<Part, Double> colPrice;
    @FXML private TableColumn<Part, Integer> colQuantity;
    @FXML private TableColumn<Part, Double> colSubtotal;
    @FXML private Label lblTotal;
    @FXML private Label lblDiscount;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colSubtotal.setCellValueFactory(cellData -> {
            Part part = cellData.getValue();
            int index = HelloApplication.cart.getCartItems().indexOf(part);
            int qty = HelloApplication.cart.getCartQuantities().get(index);
            double subtotal = part.getPrice() * qty;
            return new SimpleDoubleProperty(subtotal).asObject();
        });

        refreshCart();
    }

    private void refreshCart() {
        ObservableList<Part> cartItems = FXCollections.observableArrayList(
                HelloApplication.cart.getCartItems()
        );
        cartTable.setItems(cartItems);
        lblTotal.setText("Total: Rs." + String.format("%.2f",
                HelloApplication.cart.calculateTotal()));
        updateDiscountLabel();
    }

    private void updateDiscountLabel() {
        ArrayList<Part> items = HelloApplication.cart.getCartItems();
        ArrayList<Integer> quantities = HelloApplication.cart.getCartQuantities();

        boolean hasBulk = false;
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (int i = 0; i < items.size(); i++) {
            if (quantities.get(i) >= 3) hasBulk = true;
            if (items.get(i).getCategory().equalsIgnoreCase("Engine")) hasEngine = true;
            if (items.get(i).getCategory().equalsIgnoreCase("Electrical")) hasElectrical = true;
        }

        String discountInfo = "";
        if (hasBulk) discountInfo = discountInfo + "5% bulk discount applied ";
        if (hasEngine && hasElectrical) discountInfo = discountInfo + "10% synergy discount applied";
        lblDiscount.setText(discountInfo);
    }

    @FXML
    private void handleAddToCart() {

        String code = txtPartCode.getText().trim();
        String qtyStr = txtQuantity.getText().trim();

        if (code.isEmpty() || qtyStr.isEmpty()) {
            lblMessage.setText("please enter part code and quantity");
            return;
        }

        Part part = HelloApplication.inventoryManager.findPart(code);
        if (part == null) {
            lblMessage.setText("part not found: " + code);
            return;
        }

        int qty = 0;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (Exception e) {
            lblMessage.setText("quantity must be a whole number");
            return;
        }

        boolean added = HelloApplication.cart.addItem(part, qty);
        if (added) {
            lblMessage.setText("");
            refreshCart();
        } else {
            lblMessage.setText("could not add item - check quantity");
        }
    }

    @FXML
    private void handleRemove() {
        Part selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("please select an item to remove");
            return;
        }
        HelloApplication.cart.removeItem(selected.getCode());
        refreshCart();
    }

    @FXML
    private void handleClear() {
        HelloApplication.cart.clearCart();
        refreshCart();
    }

    @FXML
    private void handleCheckout() {
        if (HelloApplication.cart.isEmpty()) {
            lblMessage.setText("cart is empty");
            return;
        }

        double total = HelloApplication.cart.calculateTotal();
        ArrayList<Part> items = new ArrayList<>(HelloApplication.cart.getCartItems());
        ArrayList<Integer> quantities = new ArrayList<>(HelloApplication.cart.getCartQuantities());

        HelloApplication.cart.checkout(HelloApplication.inventoryManager);
        HelloApplication.auditLogger.logCheckout(items, quantities, total);

        lblMessage.setText("checkout complete - total Rs." + String.format("%.2f", total));
        refreshCart();
    }
}