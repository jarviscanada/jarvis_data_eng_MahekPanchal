package ca.jrvs.apps.jdbc;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class SchedulerConfig {

  private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

  private final QuoteHttpHelper quoteHttpHelper;
  private final QuoteDao quoteDao;

  @Value("${alpha.vantage.api.key}")
  private String apiKey;

  private final OkHttpClient client = new OkHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public SchedulerConfig(QuoteHttpHelper quoteHttpHelper, QuoteDao quoteDao) {
    this.quoteHttpHelper = quoteHttpHelper;
    this.quoteDao = quoteDao;
    this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Scheduled(fixedRate = 60000) // adjust the rate as needed
  public void fetchDataAndPersist() {
    logger.info("Scheduler executing...");

    // Add logic to fetch data from Alpha Vantage
    String symbol = "AAPL";
    String apiUrl = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
    logger.info("API URL: {}", apiUrl);

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
      logger.info("API Response: {}", responseBody);

      QuoteWrapper wrapper = objectMapper.readValue(responseBody, QuoteWrapper.class);
      Quote fetchedQuote = wrapper.getQuote();

      // Update or save the fetched quote in the database
      // ...

      logger.info("Scheduler execution complete.");
    } catch (IOException e) {
      logger.error("Error during scheduler execution", e);
    }
  }
}



//package ca.jrvs.apps.jdbc;
//
//import java.io.IOException;
//import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SchedulerConfig {
//
//  private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);
//
//  private final QuoteHttpHelper quoteHttpHelper;
//  private final QuoteDao quoteDao;
//
//  @Autowired
//  public SchedulerConfig(QuoteHttpHelper quoteHttpHelper, QuoteDao quoteDao) {
//    this.quoteHttpHelper = quoteHttpHelper;
//    this.quoteDao = quoteDao;
//  }
//
//  @Scheduled(fixedRate = 60000) // adjust the rate as needed
//  public void fetchDataAndPersist() {
//    logger.info("Scheduler executing...");
//
//    // Add logic to fetch data from Alpha Vantage
//    // For example, fetching data for a specific symbol like "AAPL"
//    String symbol = "AAPL";
//    Quote fetchedQuote = null;
//    try {
//      fetchedQuote = quoteHttpHelper.fetchQuoteInfo(symbol);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//
//    // Update or save the fetched quote in the database
//    if (fetchedQuote != null) {
//      Optional<Quote> existingQuote = quoteDao.findById(symbol);
//      if (existingQuote.isPresent()) {
//        // If the quote already exists, update it
//        Quote existing = existingQuote.get();
//        existing.setPrice(fetchedQuote.getPrice()); // Update other fields as needed
//        quoteDao.update(existing);
//      } else {
//        // If the quote doesn't exist, save it
//        quoteDao.save(fetchedQuote);
//      }
//    }
//
//    logger.info("Scheduler execution complete.");
//  }
//}
