package com.tuktuk.tuktukspareparts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainController {

    @FXML
    private TableView<Part> inventoryTable;
    @FXML
    private TableColumn<Part, String> colCode;
    @FXML
    private TableColumn<Part, String> colName;
    @FXML
    private TableColumn<Part, String> colBrand;
    @FXML
    private TableColumn<Part, String> colCategory;
    @FXML
    private TableColumn<Part, Double> colPrice;
    @FXML
    private TableColumn<Part, Integer> colQuantity;
    @FXML
    private TableColumn<Part, String> colDate;

    @FXML
    private Label lblTotalCount;
    @FXML
    private Label lblTotalValue;
    @FXML
    private Label lblLowStock;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnCart;
    @FXML
    private Button btnDealers;
    @FXML
    private TextField txtThreshold;

    // Public static variable so the Edit screen can read the selected item easily
    public static Part selectedPart = null;

    @FXML
    public void initialize() {
        // Link table columns to Part class properties
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadTable();
    }

    public void loadTable() {
        // Fetch parts list and display in table
        ObservableList<Part> parts = FXCollections.observableArrayList(
                HelloApplication.inventoryManager.getAllParts());

        inventoryTable.setItems(parts);

        // Update summary stats
        lblTotalCount.setText("Total Parts: " + HelloApplication.inventoryManager.getTotalCount());
        lblTotalValue.setText("Total Value: Rs." + HelloApplication.inventoryManager.getTotalValue());

        checkLowStock();
    }

    private void checkLowStock() {
        int threshold = HelloApplication.inventoryManager.getLowStockThreshold();
        ArrayList<Part> lowStock = HelloApplication.inventoryManager.getLowStockParts(threshold);

        if (lowStock.size() == 0) {
            lblLowStock.setText("No low stock items");
        } else {
            String warning = "Low Stock: ";
            for (int i = 0; i < lowStock.size(); i++) {
                Part p = lowStock.get(i);
                warning = warning + p.getCode() + " - " + p.getName() + " (Qty: " + p.getQuantity() + ")";
                if (i < lowStock.size() - 1) {
                    warning = warning + " | ";
                }
            }
            lblLowStock.setText(warning);
        }
    }

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-part-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add New Part");
            stage.setScene(new Scene(loader.load(), 400, 400));
            stage.showAndWait();

            // Refresh table after window closes
            loadTable();
        } catch (Exception e) {
            System.out.println("Error opening add window: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        selectedPart = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            System.out.println("Please select a part first!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-part-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Part");
            stage.setScene(new Scene(loader.load(), 400, 400));
            stage.showAndWait();

            // Refresh table after window closes
            loadTable();
        } catch (Exception e) {
            System.out.println("Error opening edit window: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("no part selected");
            return;
        }

        // show confirmation dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Delete " + selected.getName() + "?");

        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            HelloApplication.inventoryManager.deletePart(selected.getCode());
            HelloApplication.auditLogger.logDeletePart(selected.getCode(), selected.getName());
            loadTable();
        }
    }

    @FXML
    private void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Search Parts");
            stage.setScene(new Scene(loader.load(), 700, 500));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Error opening search window: " + e.getMessage());
        }
    }

    @FXML
    private void handleCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Cart / POS");
            stage.setScene(new Scene(loader.load(), 700, 500));
            stage.showAndWait();

            loadTable();
        } catch (Exception e) {
            System.out.println("Error opening cart window: " + e.getMessage());
        }
    }

    @FXML
    private void handleDealers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dealer-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Dealer Selection");
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Error opening dealer window: " + e.getMessage());
        }
    }
}
