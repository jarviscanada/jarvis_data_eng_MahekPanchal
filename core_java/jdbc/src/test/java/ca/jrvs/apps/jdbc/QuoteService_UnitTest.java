package ca.jrvs.apps.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class QuoteService_UnitTest {

  private QuoteDaoStub quoteDaoStub;
  private QuoteHttpHelperStub quoteHttpHelperStub;
  private QuoteService quoteService;

  @Before
  public void setup() {
    quoteDaoStub = new QuoteDaoStub(null);  // Pass a mock Connection or null
    quoteHttpHelperStub = new QuoteHttpHelperStub("11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d");
    quoteService = new QuoteService(quoteDaoStub, quoteHttpHelperStub);
  }

  @Test
  public void testSaveQuote() throws IOException {
    // Given
    String symbol = "AAPL";
    Quote fetchedQuote = new Quote();

    // Stub the behavior of quoteDaoStub.save to return the given quote
    quoteDaoStub.setSaveResult(fetchedQuote);

    // Stub the behavior of quoteHttpHelperStub.fetchQuoteInfo to return the fetched quote
    quoteHttpHelperStub.setFetchResult(fetchedQuote);

    // When
    Quote savedQuote = quoteService.saveQuote(symbol);

    // Then
    assertEquals(fetchedQuote, savedQuote);
    // Verify that quoteDaoStub.save was called with the expected quote
    assertTrue(quoteDaoStub.isSaveCalled());
  }

  @Test
  public void testIsValidSymbol() {
    // Given
    String symbol = "AAPL";
    quoteDaoStub.setFindByIdResult(Optional.of(new Quote()));

    // When
    boolean isValid = quoteService.isValidSymbol(symbol);

    // Then
    assertTrue(isValid);
  }

  @Test
  public void testBuySharesSuccess() {
    // Given
    String symbol = "AAPL";
    int requestedVolume = 10;
    Quote existingQuote = new Quote();
    existingQuote.setVolume(20);
    quoteDaoStub.setFindByIdResult(Optional.of(existingQuote));

    // When
    boolean purchaseSuccessful = quoteService.buyShares(symbol, requestedVolume);

    // Then
    assertTrue(purchaseSuccessful);
    assertEquals(10, existingQuote.getVolume());
    // Verify that quoteDaoStub.update was called
    assertTrue(quoteDaoStub.isUpdateCalled());
  }


  @Test
  public void testBuySharesNotEnoughVolume() {
    // Given
    String symbol = "AAPL";
    int requestedVolume = 30;
    Quote existingQuote = new Quote();
    existingQuote.setVolume(20);
    quoteDaoStub.setFindByIdResult(Optional.of(existingQuote));

    // When
    boolean purchaseSuccessful = quoteService.buyShares(symbol, requestedVolume);

    // Then
    assertFalse(purchaseSuccessful);
    assertEquals(20, existingQuote.getVolume());
    // Verify that quoteDaoStub.update was not called
    assertFalse(quoteDaoStub.isUpdateCalled());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLatestQuoteInvalidSymbol() {
    // Given
    String invalidSymbol = "INVALID";
    quoteDaoStub.setFindByIdResult(Optional.empty());

    // When
    quoteService.getLatestQuote(invalidSymbol);
  }

  // Stub implementation for QuoteDao
  private static class QuoteDaoStub extends QuoteDao implements CrudDao<Quote, String> {

    private Map<String, Quote> quotes = new HashMap<>();
    private boolean isSaveCalled = false;
    private boolean isUpdateCalled = false;
    private Optional<Quote> findByIdResult;

    public QuoteDaoStub(Connection connection) {
      super(connection);
    }

    public void setSaveResult(Quote quote) {
      this.quotes.put(quote.getTicker(), quote);
    }

    public void setFindByIdResult(Optional<Quote> result) {
      this.findByIdResult = result;
    }

    public boolean isSaveCalled() {
      return isSaveCalled;
    }

    public boolean isUpdateCalled() {
      return isUpdateCalled;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
      isSaveCalled = true;
      quotes.put(entity.getTicker(), entity);
      return entity;
    }

    @Override
    public Optional<Quote> findById(String symbol) throws IllegalArgumentException {
      return findByIdResult;
      //return Optional.ofNullable(quotes.get(symbol));
    }

    @Override
    public List<Quote> findAll() {

      return new ArrayList<>(quotes.values());
    }

    @Override
    public void update(Quote entity) throws IllegalArgumentException {
      isUpdateCalled = true;
      quotes.put(entity.getTicker(), entity);
    }

    @Override
    public void deleteById(String symbol) throws IllegalArgumentException {

    }

    @Override
    public void deleteAll() {

    }
  }


  private static class QuoteHttpHelperStub extends QuoteHttpHelper {

    private Quote fetchResult;
    public QuoteHttpHelperStub(String apiKey) {
      super(apiKey);
    }

    public void setFetchResult(Quote fetchResult) {
      this.fetchResult = fetchResult;
    }

    @Override
    public Quote fetchQuoteInfo(String symbol) {
      // Stubbed implementation for testing
      return fetchResult;
    }
  }
}
