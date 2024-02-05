package ca.jrvs.apps.jdbc;

import java.io.IOException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteDataUpdater {

  private final QuoteService quoteService;

  public QuoteDataUpdater(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @Scheduled(fixedRate = 3600000) // Update every hour (adjust as needed)
  public void updateQuoteData() {
    try {
      // Specify the symbols for which you want to update data
      String[] symbols = {"AAPL", "GOOGL", "MSFT"};

      for (String symbol : symbols) {
        quoteService.saveQuote(symbol);
      }
    } catch (IOException e) {
      // Handle the exception appropriately (log or throw)
      e.printStackTrace();
    }
  }
}
