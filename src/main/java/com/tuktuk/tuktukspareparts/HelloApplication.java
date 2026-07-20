package com.tuktuk.tuktukspareparts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Created these here so the whole app can use them
    public static InventoryManager inventoryManager;
    public static DealerManager dealerManager;
    public static Cart cart;
    public static AuditLogger auditLogger;

    @Override
    public void start(Stage stage) throws IOException {

        // set up all the main objects when app starts
        inventoryManager = new InventoryManager();
        inventoryManager.loadParts();

        dealerManager = new DealerManager();

        cart = new Cart();

        auditLogger = new AuditLogger();

        // loads the main screen
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // made the window bigger so everything fits
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        // changed title to match the actual app
        stage.setTitle("Malabe Tuk-Tuk & Three-Wheeler Spares Depot");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}