package com.unigrade.app.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerHelper {
    private static final int READ_TIMEOUT = 20000;
    private static final int CONNECTION_TIMEOUT = 20000;
    private String url;
    private String result;

    public ServerHelper(String url){
        this.url = url;
    }

    public String get() {
        result = null;
        while (result == null) {
            try {
                URL myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                //Set configuration to connection
                connection.setRequestMethod("GET");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String inputLine;
                //Check if it's reading a null lline
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);

                }

                reader.close();
                streamReader.close();

                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public String post(String postData){
        result = null;

        while (result == null){
            try {
                URL myUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

                //Set configuration to connection
                connection.setRequestMethod("POST");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setDoOutput(true);
                connection.connect();

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(postData);
                wr.flush();

                int number = connection.getResponseCode();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String inputLine;
                //Check if it's reading a null lline
                while((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);

                }

                reader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

                //SubjectsController subjectsController = new SubjectsController();
                //String subjects = subjectsController.(result);

            } catch(Exception e){
                e.printStackTrace();
            }
        }

        return result;

    }
}