package ca.jrvs.apps.jdbc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuoteHttpHelperTest {

  private static final String API_KEY = "11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d";

  @Test
  public void testFetchQuoteInfo() {
    QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper(API_KEY);

    // Test with a valid ticker symbol
    assertDoesNotThrow(() -> {
      Company company = quoteHttpHelper.fetchQuoteInfo("AAPL");
      assertNotNull(company);
      assertNotNull(company.getSymbol());
      assertNotNull(company.getCompanyName());
    });

    // Test with an invalid ticker symbol
    assertThrows(IOException.class, () -> quoteHttpHelper.fetchQuoteInfo("INVALID"));
  }
}
