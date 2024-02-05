package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Service
public class QuoteHttpHelper {
  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);

  @Value("${alpha.vantage.api.key}")
  private String alphaVantageApiKey;
  final String apiKey;
  private final OkHttpClient client;
  private final ObjectMapper objectMapper;

  public QuoteHttpHelper(@Value("${alpha.vantage.api.key}") String apiKey) {
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

  public Quote fetchQuoteInfo(String symbol) {
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol;

    try {
      if (apiKey == null) {
        throw new IllegalArgumentException("API key is null");
      }

      Request request = new Request.Builder()
          .url(url)
          .header("Content-Type", "application/json")
          .header("Accept", "application/json")
          .header("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
          .header("x-rapidapi-key", apiKey)
          .build();

      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        String responseBody = response.body().string();
        return parseJsonToQuote(responseBody, Quote.class);
      } else {
        logger.error("Failed to fetch quote information for {}: {}", symbol, response.message());
      }
    } catch (IOException e) {
      logger.error("Failed to fetch quote information for {}: {}", symbol, e.getMessage());
    } catch (Exception e) {
      logger.error("Error processing quote information for {}: {}", symbol, e.getMessage());
    }

    return null;
  }

  private <T> T parseJsonToQuote(String json, Class<T> clazz) throws IOException {
    return JsonParser.toObjectFromJson(json, clazz);
  }




//  public Quote fetchQuoteInfo(String symbol) throws IOException, IllegalArgumentException {
//    String apiUrl = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
//    logger.info("API URL: " + apiUrl);
//
//    Request request = new Request.Builder()
//        .url(apiUrl)
//        .header("X-RapidAPI-Key", alphaVantageApiKey)
//        .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
//        .build();
//
//    try (Response response = client.newCall(request).execute()) {
//      if (!response.isSuccessful()) {
//        throw new IllegalArgumentException("Failed to fetch quote data for symbol: " + symbol);
//      }
//
//      String responseBody = response.body().string();
//
//      QuoteWrapper wrapper = objectMapper.readValue(responseBody, QuoteWrapper.class);
//      Quote quote = wrapper.getQuote();
//
//      // Return the Quote object
//      return quote;
//    }
//  }



}

