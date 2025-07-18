package com.news.view;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;
import com.news.controller.ApiController;

public class Newsivew {
    private SignUpView signUpView;
    private Stage primaryStage;
    private VBox newsContainer;
    private ApiController apiController = new ApiController();

    public Newsivew(SignUpView signUpView, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.signUpView = signUpView;
    }

    public void show() {
        showCategorySelection();
    }

    private void showCategorySelection() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(40));
        grid.setStyle("-fx-background-color: #FFFAF5;");

        String[] categories = {"general", "business", "entertainment", "health", "science", "sports"};
        String[] gradients = {
                "-fx-background-color: linear-gradient(to right,rgb(86, 199,227), #6dd5ed);",
                "-fx-background-color: linear-gradient(to right, #cc2b5e, #753a88);",
                "-fx-background-color: linear-gradient(to right, #ee9ca7,rgb(248, 138, 150));",
                "-fx-background-color: linear-gradient(to right, #56ab2f, #a8e063);",
                "-fx-background-color: linear-gradient(to right, #614385, #516395);",
                "-fx-background-color: linear-gradient(to right, #e65c00, #f9d423);"
        };

        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                if (index < categories.length) {
                    String category = categories[index];
                    Button btn = new Button(capitalize(category));
                    btn.setPrefWidth(190);
                    btn.setPrefHeight(200);
                    btn.setStyle(gradients[index] +
                            " -fx-text-fill: white; -fx-font-size: 22; -fx-font-weight: bold; -fx-background-radius: 18;");
                    btn.setOnAction(e -> handleCategorySelection(category));

                    ScaleTransition stEnlarge = new ScaleTransition(Duration.millis(180), btn);
                    stEnlarge.setToX(1.08);
                    stEnlarge.setToY(1.08);
                    ScaleTransition stNormal = new ScaleTransition(Duration.millis(180), btn);
                    stNormal.setToX(1.0);
                    stNormal.setToY(1.0);
                    btn.setOnMouseEntered(e -> stEnlarge.playFromStart());
                    btn.setOnMouseExited(e -> stNormal.playFromStart());

                    grid.add(btn, col, row);
                    index++;
                }
            }
        }

        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(120);
        logoutButton.setPrefHeight(50);
        logoutButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 18; -fx-background-radius: 24;"));
        logoutButton.setOnAction(e -> signUpView.openAuthWindow());

        grid.add(logoutButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 545, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("C2W News App");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleCategorySelection(String category) {
        if (category.equals("general")) {
            showGeneralNews();
        } else {
            showCategorySources(category);
        }
    }

    private void showGeneralNews() {
        primaryStage.setTitle("C2W News App");
        newsContainer = new VBox(10);
        newsContainer.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(newsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #FFFAF5;");

        Button loadNewsButton = new Button("Load News");
        loadNewsButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24;");
        loadNewsButton.setOnMouseEntered(e -> loadNewsButton.setStyle("-fx-background-color:rgb(23, 170, 33); -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24;"));
        loadNewsButton.setOnMouseExited(e -> loadNewsButton.setStyle("-fx-background-color:#2196f3; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24;"));

        ProgressIndicator loader = new ProgressIndicator();
        loader.setVisible(false);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24;");
        backButton.setOnAction(e -> showCategorySelection());

        loadNewsButton.setOnAction(e -> {
            loader.setVisible(true);
            loadNewsAsync(loader);
        });

        HBox bottomBox = new HBox(16, loadNewsButton, loader, backButton);
        bottomBox.setPadding(new Insets(20, 0, 40, 0));
        bottomBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 545, 800);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        loader.setVisible(true);
        loadNewsAsync(loader);
    }

    private void showCategorySources(String category) {
        VBox sourcesContainer = new VBox(10);
        sourcesContainer.setPadding(new Insets(10));
        sourcesContainer.setAlignment(Pos.TOP_CENTER);

        ProgressIndicator loader = new ProgressIndicator();
        loader.setVisible(true);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16; -fx-background-radius: 24;");
        backButton.setOnAction(e -> showCategorySelection());

        BorderPane root = new BorderPane();
        ScrollPane scrollPane = new ScrollPane(sourcesContainer);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        HBox bottomBox = new HBox(16, loader, backButton);
        bottomBox.setPadding(new Insets(20, 0, 40, 0));
        bottomBox.setAlignment(Pos.CENTER);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 545, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONArray sources = apiController.getNews(category);
                Platform.runLater(() -> {
                    loader.setVisible(false);
                    sourcesContainer.getChildren().clear();
                    if (sources.length() == 0) {
                        sourcesContainer.getChildren().add(new Label("No sources found for this category."));
                    }
                    for (int i = 0; i < sources.length(); i++) {
                        JSONObject source = sources.getJSONObject(i);
                        String name = source.getString("name");
                        String description = source.optString("description", "");
                        String urlSource = source.getString("url");
                        HBox card = createSourceCard(name, description, urlSource);
                        sourcesContainer.getChildren().add(card);
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void loadNewsAsync(ProgressIndicator loader) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                JSONArray newsArray = apiController.getNews("general");
                Platform.runLater(() -> {
                    loader.setVisible(false);
                    newsContainer.getChildren().clear();
                    for (int i = 0; i < newsArray.length(); i++) {
                        JSONObject news = newsArray.getJSONObject(i);
                        String title = news.optString("title", "No Title");
                        String url = news.optString("url", "");
                        Label label = new Label(title);
                        if (!url.isEmpty()) {
                            Hyperlink link = new Hyperlink("Read more");
                            link.setOnAction(e -> {
                                try {
                                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                            VBox vbox = new VBox(label, link);
                            vbox.setPadding(new Insets(10));
                            newsContainer.getChildren().add(vbox);
                        } else {
                            newsContainer.getChildren().add(label);
                        }
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private HBox createSourceCard(String name, String description, String urlSource) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #ddd;");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox textContainer = new VBox(5);
        Label title = new Label(name);
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        Label desc = new Label(description);
        desc.setWrapText(true);

        Hyperlink link = new Hyperlink(urlSource);
        link.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(urlSource));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        textContainer.getChildren().addAll(title, desc, link);
        card.getChildren().add(textContainer);

        return card;
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
