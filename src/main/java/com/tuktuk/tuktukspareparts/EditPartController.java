package com.tuktuk.tuktukspareparts;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPartController {

    @FXML private Label lblCode;
    @FXML private TextField txtName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQuantity;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private TextField txtDate;
    @FXML private TextField txtImageFile;
    @FXML private Label lblMessage;

    private Part currentPart;

    @FXML
    public void initialize() {
        cmbCategory.setItems(FXCollections.observableArrayList(
                "Engine", "Electrical", "Bodywork", "Brakes"
        ));
    }

    public void setPart(Part part) {
        this.currentPart = part;
        lblCode.setText(part.getCode());
        txtName.setText(part.getName());
        txtBrand.setText(part.getBrand());
        txtPrice.setText(String.valueOf(part.getPrice()));
        txtQuantity.setText(String.valueOf(part.getQuantity()));
        cmbCategory.setValue(part.getCategory());
        txtDate.setText(part.getDate());
        txtImageFile.setText(part.getImageFile());
    }

    @FXML
    private void handleSave() {

        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String category = cmbCategory.getValue();
        String date = txtDate.getText().trim();
        String imageFile = txtImageFile.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty()
                || quantityStr.isEmpty() || category == null) {
            lblMessage.setText("please fill in all required fields");
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            lblMessage.setText("price must be a number");
            return;
        }

        if (price <= 0) {
            lblMessage.setText("price must be greater than 0");
            return;
        }

        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            lblMessage.setText("quantity must be a whole number");
            return;
        }

        if (quantity < 0) {
            lblMessage.setText("quantity cannot be negative");
            return;
        }

        Part updatedPart = new Part(currentPart.getCode(), name, brand,
                price, quantity, category, date, imageFile);

        HelloApplication.inventoryManager.updatePart(currentPart.getCode(), updatedPart);

        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}