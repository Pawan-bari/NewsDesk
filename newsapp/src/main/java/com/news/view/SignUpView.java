package com.news.view;

import com.news.controller.LoginController;
import com.news.controller.Signupcontroller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignUpView extends Application {

    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;

    public SignUpView() {}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openAuthWindow();
    }

    public void openAuthWindow() {
        buildAuthUI(primaryStage);
        primaryStage.show();
    }

    public void openMainAppWindow() {
        Newsivew newsView = new Newsivew(this, primaryStage);
        newsView.show();
    }

    private void buildAuthUI(Stage stage) {
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setFocusTraversable(false);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFocusTraversable(false);

        Button signUpButton = new Button("Sign Up");
        Button loginButton = new Button("Login");

        String buttonStyle = "-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-size: 16; "
                + "-fx-background-radius: 24; -fx-padding: 8 32 8 32;";

        signUpButton.setStyle(buttonStyle);
        loginButton.setStyle(buttonStyle);

        signUpButton.setOnMouseEntered(e -> signUpButton.setStyle("-fx-background-color: #1769aa; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32;"));
        signUpButton.setOnMouseExited(e -> signUpButton.setStyle(buttonStyle));

        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #1769aa; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24; -fx-padding: 8 32 8 32;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(buttonStyle));

        Signupcontroller signupController = new Signupcontroller();

        signUpButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter both email and password.");
                return;
            }
            
            Signupcontroller.Result result = signupController.signUp(email, password);
            if (result.success) {
                clearFields();
                openMainAppWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, result.message);
            }
        });

        loginButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please enter both email and password.");
                return;
            }
            
            LoginController loginController = new LoginController();
            LoginController.Result result = loginController.login(email, password);
            if (result.success) {
                clearFields();
                openMainAppWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, result.message);
            }
        });

        // FIXED IMAGE LOADING - Try multiple methods to load background image
        Image backgroundImage = null;
        
        // Method 1: Load from classpath resources
        try {
            backgroundImage = new Image(getClass().getResourceAsStream("/Designer.png"));
            System.out.println("✅ Background image loaded from classpath");
        } catch (Exception e) {
            System.out.println("❌ Failed to load from classpath: " + e.getMessage());
            
            // Method 2: Try loading from file system
            try {
                backgroundImage = new Image("file:src/main/resources/Designer.png");
                System.out.println("✅ Background image loaded from file system");
            } catch (Exception e2) {
                System.out.println("❌ Failed to load from file system: " + e2.getMessage());
                
                // Method 3: Try loading from current directory
                try {
                    backgroundImage = new Image("file:Designer.png");
                    System.out.println("✅ Background image loaded from current directory");
                } catch (Exception e3) {
                    System.out.println("❌ Failed to load from current directory: " + e3.getMessage());
                }
            }
        }

        Pane pane = new Pane();

        if (backgroundImage != null && !backgroundImage.isError()) {
            BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
            );
            pane.setBackground(new Background(background));
            System.out.println("✅ Background image set successfully");
        } else {
            // Fallback: Beautiful gradient background
            String gradientStyle = "-fx-background-color: linear-gradient(to bottom right, " +
                                   "#667eea 0%, #764ba2 50%, #f093fb 100%);";
            pane.setStyle(gradientStyle);
            System.out.println("⚠️ Using gradient fallback background");
        }

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(420);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, #2196f3, 20, 0.3, 0, 4);"
        );

        Label titleLabel = new Label("📰 News App");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #1769aa; -fx-padding: 0 0 20 0;");

        Label welcomeLabel = new Label("Welcome! Please sign in or create an account");
        welcomeLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555; -fx-text-alignment: center;");
        welcomeLabel.setWrapText(true);

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Enhanced input field styling
        emailField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");
        passwordField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");

        // Add focus effects
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                emailField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #2196f3; -fx-border-width: 2;");
            } else {
                emailField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");
            }
        });

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #2196f3; -fx-border-width: 2;");
            } else {
                passwordField.setStyle("-fx-font-size: 14; -fx-padding: 12; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1;");
            }
        });

        // Add spacing between button sections
        Region spacer = new Region();
        spacer.setPrefHeight(10);

        layout.getChildren().addAll(
            titleLabel,
            welcomeLabel,
            emailLabel, emailField,
            passwordLabel, passwordField,
            spacer,
            loginButton, signUpButton
        );

        pane.getChildren().add(layout);

        // Center layout within pane
        layout.layoutXProperty().bind(pane.widthProperty().subtract(layout.widthProperty()).divide(2));
        layout.layoutYProperty().bind(pane.heightProperty().subtract(layout.heightProperty()).divide(2));

        Scene scene = new Scene(pane, 545, 800);
        stage.setScene(scene);
        stage.setTitle("News App - Sign In");
        stage.setResizable(false);
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : 
                      type == Alert.AlertType.WARNING ? "Warning" : "Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
