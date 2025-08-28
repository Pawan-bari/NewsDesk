package com.news.view;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private String currentCategory = "";

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
        currentCategory = category;
        showCategoryNews(category);
    }

    // UNIFIED METHOD for all categories with images
    private void showCategoryNews(String category) {
        primaryStage.setTitle("C2W News App - " + capitalize(category) + " News");
        newsContainer = new VBox(15);
        newsContainer.setPadding(new Insets(10));
        newsContainer.setAlignment(Pos.TOP_CENTER);
        
        ScrollPane scrollPane = new ScrollPane(newsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #FFFAF5;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Button loadNewsButton = new Button("Load " + capitalize(category) + " News");
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
            loadCategoryNewsAsync(category, loader);
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

        // Auto-load news on page load
        loader.setVisible(true);
        loadCategoryNewsAsync(category, loader);
    }

    // UNIFIED METHOD to load news for any category with images
    private void loadCategoryNewsAsync(String category, ProgressIndicator loader) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Get actual headlines with images for any category
                JSONArray newsArray = apiController.getHeadlines(category, "us");
                
                Platform.runLater(() -> {
                    loader.setVisible(false);
                    newsContainer.getChildren().clear();
                    
                    if (newsArray != null && newsArray.length() > 0) {
                        for (int i = 0; i < Math.min(newsArray.length(), 20); i++) { // Limit to 20 articles
                            JSONObject article = newsArray.getJSONObject(i);
                            
                            String title = article.optString("title", "No Title");
                            String description = article.optString("description", "No description available");
                            String url = article.optString("url", "");
                            String imageUrl = article.optString("urlToImage", "");
                            String source = "Unknown Source";
                            String publishedAt = article.optString("publishedAt", "");
                            
                            // Get source name
                            if (article.has("source") && !article.isNull("source")) {
                                JSONObject sourceObj = article.getJSONObject("source");
                                source = sourceObj.optString("name", "Unknown Source");
                            }
                            
                            // Create news card with category-specific styling
                            VBox newsCard = createNewsCard(title, description, source, url, imageUrl, publishedAt, category);
                            newsContainer.getChildren().add(newsCard);
                        }
                    } else {
                        Label noNewsLabel = new Label("No " + category + " news available at the moment.");
                        noNewsLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #666;");
                        newsContainer.getChildren().add(noNewsLabel);
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    // Enhanced news card with category-specific emojis and colors
    private VBox createNewsCard(String title, String description, String source, String url, String imageUrl, String publishedAt, String category) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        
        // Category-specific border colors
        String borderColor = getCategoryColor(category);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: " + borderColor + "; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setMaxWidth(500);
        card.setMinWidth(480);
        
        // Category header with emoji
        Label categoryLabel = new Label(getCategoryEmoji(category) + " " + capitalize(category).toUpperCase());
        categoryLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + borderColor + "; -fx-padding: 0 0 5 0;");
        card.getChildren().add(categoryLabel);
        
        // Add image if available
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl, 460, 250, true, true, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(460);
                imageView.setFitHeight(250);
                imageView.setPreserveRatio(true);
                imageView.setStyle("-fx-background-radius: 8;");
                card.getChildren().add(imageView);
            } catch (Exception e) {
                // If image fails to load, add a category-specific placeholder
                Label imagePlaceholder = new Label(getCategoryEmoji(category) + " Image not available");
                imagePlaceholder.setStyle("-fx-font-size: 14; -fx-text-fill: #999; -fx-padding: 20; -fx-background-color: #f5f5f5; -fx-background-radius: 8;");
                imagePlaceholder.setAlignment(Pos.CENTER);
                imagePlaceholder.setMaxWidth(460);
                imagePlaceholder.setPrefHeight(100);
                card.getChildren().add(imagePlaceholder);
            }
        } else {
            // Add category-specific placeholder when no image URL
            Label imagePlaceholder = new Label(getCategoryEmoji(category) + " " + capitalize(category) + " News");
            imagePlaceholder.setStyle("-fx-font-size: 16; -fx-text-fill: " + borderColor + "; -fx-font-weight: bold; -fx-padding: 20; -fx-background-color: #f9f9f9; -fx-background-radius: 8;");
            imagePlaceholder.setAlignment(Pos.CENTER);
            imagePlaceholder.setMaxWidth(460);
            imagePlaceholder.setPrefHeight(80);
            card.getChildren().add(imagePlaceholder);
        }
        
        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(450);
        
        // Source and date info
        HBox infoBox = new HBox(10);
        Label sourceLabel = new Label("📰 " + source);
        sourceLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666; -fx-font-style: italic;");
        
        if (publishedAt != null && !publishedAt.isEmpty()) {
            String formattedDate = formatDate(publishedAt);
            Label dateLabel = new Label("🕒 " + formattedDate);
            dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");
            infoBox.getChildren().addAll(sourceLabel, dateLabel);
        } else {
            infoBox.getChildren().add(sourceLabel);
        }
        
        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #555; -fx-line-spacing: 2px;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(450);
        
        card.getChildren().addAll(titleLabel, infoBox, descLabel);
        
        // Read more link
        if (!url.isEmpty()) {
            Hyperlink readMoreLink = new Hyperlink("📖 Read Full Article");
            readMoreLink.setStyle("-fx-text-fill: " + borderColor + "; -fx-font-size: 14; -fx-font-weight: bold;");
            readMoreLink.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            card.getChildren().add(readMoreLink);
        }
        
        return card;
    }

    // Get category-specific colors
    private String getCategoryColor(String category) {
        switch (category.toLowerCase()) {
            case "business": return "#753a88";
            case "entertainment": return "#f8628a";
            case "health": return "#a8e063";
            case "science": return "#516395";
            case "sports": return "#f9d423";
            case "general":
            default: return "#56c7e3";
        }
    }

    // Get category-specific emojis
    private String getCategoryEmoji(String category) {
        switch (category.toLowerCase()) {
            case "business": return "💼";
            case "entertainment": return "🎬";
            case "health": return "🏥";
            case "science": return "🔬";
            case "sports": return "⚽";
            case "general":
            default: return "📰";
        }
    }

    private String formatDate(String publishedAt) {
        try {
            String[] parts = publishedAt.split("T");
            if (parts.length > 0) {
                return parts[0]; // Return just the date part
            }
        } catch (Exception e) {
            // Ignore formatting errors
        }
        return publishedAt;
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
