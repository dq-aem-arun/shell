package com.shell.core.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = JsonAPIDetails.class)
public class JsonAPIDetailsImpl implements JsonAPIDetails {

    @Override
    public JSONObject fetchJsonDetailsById(String id) {
        String apiUrl = "https://jsonplaceholder.typicode.com/posts/" + id;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            return new JSONObject(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return new JSONObject().put("error", "Unable to fetch data");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
