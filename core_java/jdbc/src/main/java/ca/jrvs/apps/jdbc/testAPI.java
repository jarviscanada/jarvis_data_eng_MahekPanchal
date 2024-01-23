package ca.jrvs.apps.jdbc;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class testAPI {

  public static void main(String[] args) {
    String apiKey = "11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d";
    String symbol = "TSLA";

    HttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json");
    httpGet.setHeader("X-RapidAPI-Key", apiKey);
    httpGet.setHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com");

    try {
      HttpResponse response = httpClient.execute(httpGet);
      BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }

      System.out.println(result.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}



