package com.tuktuk.tuktukspareparts;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPartController {

    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtQuantity;
    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField txtThreshold;
    @FXML
    private TextField txtImageFile;
    @FXML
    private Label lblMessage;

    @FXML
    public void initialize() {
        cmbCategory.setItems(FXCollections.observableArrayList(
                "Engine", "Electrical", "Bodywork", "Brakes"));
    }

    @FXML
    private void handleSave() {

        String code = txtCode.getText().trim().toUpperCase();
        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String category = cmbCategory.getValue();
        String date = "";
        if (datePicker.getValue() != null) {
            date = datePicker.getValue().toString();
        }

        String thresholdStr = txtThreshold.getText().trim();
        int threshold = 5;

        if (!thresholdStr.isEmpty()) {
            try {
                threshold = Integer.parseInt(thresholdStr);
                if (threshold < 0) {
                    lblMessage.setText("threshold cannot be negative");
                    return;
                }
            } catch (Exception e) {
                lblMessage.setText("threshold must be a whole number");
                return;
            }
        }

        String imageFile = txtImageFile.getText().trim();

        if (code.isEmpty() || name.isEmpty() || priceStr.isEmpty()
                || quantityStr.isEmpty() || category == null) {
            lblMessage.setText("please fill in all required fields");
            return;
        }

        if (!code.matches("P\\d+")) {
            lblMessage.setText("part code must start with P e.g. P011");
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

        if (brand.isEmpty()) {
            brand = "Unknown";
        }

        Part newPart = new Part(code, name, brand, price,
                quantity, category, date, threshold, imageFile);

        boolean added = HelloApplication.inventoryManager.addPart(newPart);

        if (added) {
            HelloApplication.auditLogger.logAddPart(code, name, quantity);
            Stage stage = (Stage) txtCode.getScene().getWindow();
            stage.close();
        } else {
            lblMessage.setText("a part with this code already exists");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) txtCode.getScene().getWindow();
        stage.close();
    }
}