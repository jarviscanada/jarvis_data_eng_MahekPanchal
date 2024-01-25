package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.Quote;
import ca.jrvs.apps.jdbc.QuoteHttpHelper;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class QuoteHttpHelperTest {

  private final String apiKey = "11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d";

  @Test
  void testFetchQuoteInfo() {
    QuoteHttpHelper httpHelper = new QuoteHttpHelper(apiKey);

    assertDoesNotThrow(() -> {
      Quote quote = httpHelper.fetchQuoteInfo("AAPL");
      assertNotNull(quote);
      assertNotNull(quote.getTicker());
      assertNotNull(quote.getOpen());
      assertNotNull(quote.getHigh());
      assertNotNull(quote.getLow());
      assertNotNull(quote.getPrice());
      assertNotNull(quote.getVolume());
      assertNotNull(quote.getLatestTradingDay());
      assertNotNull(quote.getPreviousClose());
      assertNotNull(quote.getChange());
      assertNotNull(quote.getChangePercent());
    });
  }
}
