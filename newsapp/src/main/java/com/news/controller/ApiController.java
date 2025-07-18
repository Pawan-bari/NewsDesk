package com.news.controller;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiController {

    public JSONArray getNews(String category) {
        try {
            String apikey = "6fc9655e32424d9ab2f11d55401ed636"; 
            String urlStr = 
                "https://newsapi.org/v2/top-headlines/sources?category=" + category + 
                "&apiKey=" + apikey; 

            URL url = new URL(urlStr); 
            HttpURLConnection conn = (HttpURLConnection) 
                url.openConnection(); 
            conn.setRequestMethod("GET"); 

            BufferedReader in = new BufferedReader(new 
                InputStreamReader(conn.getInputStream())); 
            
            StringBuilder response = new StringBuilder(); 
            String inputLine; 
            while ((inputLine = in.readLine()) != null) 
                response.append(inputLine); 

            in.close(); 

            JSONObject jsonResponse = new 
                JSONObject(response.toString()); 
            JSONArray sources = jsonResponse.getJSONArray("sources"); 
         
            return sources;
        } catch (Exception e) {
            
        }
return null;
}
}