package com.tuktuk.tuktukspareparts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class SearchController {

    @FXML
    private TextField txtKeyword;
    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private TextField txtMinPrice;
    @FXML
    private TextField txtMaxPrice;
    @FXML
    private TableView<Part> searchResultsTable;
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
    private Label lblResults;

    @FXML
    public void initialize() {

        cmbCategory.setItems(FXCollections.observableArrayList(
                "", "Engine", "Electrical", "Bodywork", "Brakes"));

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        handleSearch();
    }

    @FXML
    private void handleSearch() {

        String keyword = txtKeyword.getText().trim();
        String category = cmbCategory.getValue();
        if (category == null) {
            category = "";
        }

        double minPrice = 0;
        double maxPrice = 0;

        try {
            if (!txtMinPrice.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(txtMinPrice.getText().trim());
            }
        } catch (Exception e) {
            lblResults.setText("min price must be a number");
            return;
        }

        try {
            if (!txtMaxPrice.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(txtMaxPrice.getText().trim());
            }
        } catch (Exception e) {
            lblResults.setText("max price must be a number");
            return;
        }

        if (minPrice > 0 && maxPrice > 0 && minPrice > maxPrice) {
            lblResults.setText("min price cannot be greater than max price");
            return;
        }

        ArrayList<Part> allParts = HelloApplication.inventoryManager.getAllParts();
        ArrayList<Part> results = SearchManager.search(allParts, category, keyword, minPrice, maxPrice);

        ObservableList<Part> resultList = FXCollections.observableArrayList(results);
        searchResultsTable.setItems(resultList);

        lblResults.setText("found " + results.size() + " parts");
    }

    @FXML
    private void handleClear() {
        txtKeyword.clear();
        cmbCategory.setValue(null);
        txtMinPrice.clear();
        txtMaxPrice.clear();
        handleSearch();
    }
}