package com.tuktuk.tuktukspareparts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class DealerController {

    @FXML private TableView<Dealer> dealerTable;
    @FXML private TableColumn<Dealer, String> colCode;
    @FXML private TableColumn<Dealer, String> colName;
    @FXML private TableColumn<Dealer, String> colPhone;
    @FXML private TableColumn<Dealer, String> colLocation;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }

    @FXML
    private void handleSelectDealers() {
        ArrayList<Dealer> selected = HelloApplication.dealerManager
                .getRandomDealersSortedByLocation();

        ObservableList<Dealer> dealerList = FXCollections.observableArrayList(selected);
        dealerTable.setItems(dealerList);

        lblMessage.setText("showing 4 randomly selected dealers sorted by location");
    }
}