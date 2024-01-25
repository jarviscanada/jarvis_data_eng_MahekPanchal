package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;



public class QuoteHttpHelper {

  private final String apiKey;
  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  public QuoteHttpHelper(String apiKey) {
    this.apiKey = apiKey;
    this.client = new OkHttpClient();
    this.objectMapper = new ObjectMapper();
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   * @param symbol Stock symbol
   * @return Quote with latest data
   * @throws IOException If an error occurs during the HTTP request
   * @throws IllegalArgumentException If no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IOException, IllegalArgumentException {
    String apiUrl = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
    Request request = new Request.Builder()
        .url(apiUrl)
        .header("X-RapidAPI-Key", apiKey)
        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IllegalArgumentException("Failed to fetch quote data for symbol: " + symbol);
      }

      String responseBody = response.body().string();

      QuoteWrapper wrapper = objectMapper.readValue(responseBody, QuoteWrapper.class);
      Quote quote = wrapper.getQuote();

      // Parse the JSON response and create a Quote object using Jackson
     // Quote quote = objectMapper.readValue(responseBody, Quote.class);

      // Return the Quote object
      return quote;
    }
  }
}

