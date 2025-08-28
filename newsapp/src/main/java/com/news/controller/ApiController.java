package com.news.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiController {

    // Get sources for categories (existing functionality)
    public JSONArray getNews(String category) {
        try {
            String apikey = "6fc9655e32424d9ab2f11d55401ed636";
            String urlStr = "https://newsapi.org/v2/top-headlines/sources?category=" + category + "&apiKey=" + apikey;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray sources = jsonResponse.getJSONArray("sources");
            return sources;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // NEW METHOD: Get actual headlines for news with images
    public JSONArray getHeadlines(String category, String country) {
        try {
            String apikey = "6fc9655e32424d9ab2f11d55401ed636";
            String urlStr = "https://newsapi.org/v2/top-headlines?";
            
            if (category != null && !category.isEmpty()) {
                urlStr += "category=" + category + "&";
            }
            if (country != null && !country.isEmpty()) {
                urlStr += "country=" + country + "&";
            }
            urlStr += "apiKey=" + apikey;
            
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");
            return articles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
