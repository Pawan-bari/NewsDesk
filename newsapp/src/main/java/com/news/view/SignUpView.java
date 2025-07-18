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

        signUpButton.setOnMouseEntered(e -> signUpButton.setStyle("-fx-background-color: #1769aa; -fx-text-fill: white;"));
        signUpButton.setOnMouseExited(e -> signUpButton.setStyle(buttonStyle));
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #1769aa; -fx-text-fill: white;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(buttonStyle));

        Signupcontroller signupController = new Signupcontroller();
        signUpButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            Signupcontroller.Result result = signupController.signUp(email, password);
            if (result.success) {
                openMainAppWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, result.message);
            }
        });

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            LoginController.Result result = new LoginController().login(email, password);
            if (result.success) {
                openMainAppWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, result.message);
            }
        });

        Image backgroundImage = new Image("file:src/main/resources/Designer.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        Pane pane = new Pane();
        pane.setBackground(new Background(background));

        VBox layout = new VBox(18);
        layout.setPadding(new Insets(60));
        layout.setMaxWidth(400);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 24;"
                        + "-fx-effect: dropshadow(gaussian, #2196f3, 18, 0.2, 0, 4);"
        );

        Label titleLabel = new Label("News on NewsApp");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #1769aa;");

        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");

        layout.getChildren().addAll(
                titleLabel,
                emailLabel, emailField,
                passwordLabel, passwordField,
                loginButton, signUpButton
        );

        pane.getChildren().add(layout);
        layout.layoutXProperty().bind(pane.widthProperty().subtract(layout.widthProperty()).divide(2));
        layout.layoutYProperty().bind(pane.heightProperty().subtract(layout.heightProperty()).divide(2));

        Scene scene = new Scene(pane, 545, 800);
        stage.setScene(scene);
        stage.setTitle("News App");
        stage.setResizable(false);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
