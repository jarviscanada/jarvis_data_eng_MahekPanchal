package ca.jrvs.apps.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuoteService_IntTest {

  private QuoteDao quoteDao;
  private QuoteHttpHelper quoteHttpHelper;
  private QuoteService quoteService;

  @Before
  public void setup() {
    // Mock dependencies
    quoteDao = mock(QuoteDao.class);
    quoteHttpHelper = mock(QuoteHttpHelper.class);
    quoteService = new QuoteService(quoteDao, quoteHttpHelper);
  }

  @Test
  public void testSaveQuote() throws IOException {
    // Given
    String symbol = "AAPL";
    Quote fetchedQuote = new Quote();

    // Mock the behavior of quoteDao.save to return the given quote
    doReturn(fetchedQuote).when(quoteDao).save(any(Quote.class));

    // Mock the behavior of quoteHttpHelper.fetchQuoteInfo to return the fetched quote
    when(quoteHttpHelper.fetchQuoteInfo(eq(symbol))).thenReturn(fetchedQuote);

    // When
    Quote savedQuote = quoteService.saveQuote(symbol);

    // Then
    assertEquals(fetchedQuote, savedQuote);
    // Verify that quoteDao.save was called with any instance of Quote
    verify(quoteDao, atLeastOnce()).save(any(Quote.class));
  }


  @Test
  public void testIsValidSymbol() {
    // Given
    String symbol = "AAPL";
    Quote existingQuote = new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 1000,
        new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior
    when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(existingQuote));

    // When
    boolean isValid = quoteService.isValidSymbol(symbol);

    // Then
    assertTrue(isValid);
    verify(quoteDao, times(1)).findById(eq(symbol));
  }

  @Test
  public void testBuyShares_Successful() {
    // Given
    String symbol = "AAPL";
    int requestedVolume = 10;
    Quote existingQuote = new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 20,
        new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior
    when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(existingQuote));

    // Use doNothing() or doAnswer() since update doesn't return a value
    doNothing().when(quoteDao).update(any(Quote.class));

    // When
    boolean purchaseSuccessful = quoteService.buyShares(symbol, requestedVolume);

    // Then
    assertTrue(purchaseSuccessful);
    assertEquals(10, existingQuote.getVolume());
    verify(quoteDao, times(1)).findById(eq(symbol));
    verify(quoteDao, times(1)).update(any(Quote.class));
  }


  @Test
  public void testBuyShares_NotEnoughVolume() {
    // Given
    String symbol = "AAPL";
    int requestedVolume = 30;
    Quote existingQuote = new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 20,
        new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior
    when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(existingQuote));

    // When
    boolean purchaseSuccessful = quoteService.buyShares(symbol, requestedVolume);

    // Then
    assertFalse(purchaseSuccessful);
    assertEquals(20, existingQuote.getVolume());
    verify(quoteDao, times(1)).findById(eq(symbol));
    verify(quoteDao, never()).update(any(Quote.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLatestQuote_InvalidSymbol() {
    // Given
    String invalidSymbol = "INVALID";
    when(quoteDao.findById(eq(invalidSymbol))).thenReturn(Optional.empty());

    // When
    quoteService.getLatestQuote(invalidSymbol);

    // Then: expect IllegalArgumentException
  }


  @Test
  public void testSellShares_Successful() {
    // Given
    String symbol = "AAPL";
    int numberOfShares = 10;
    Quote existingQuote = new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 30,
        new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior
    when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(existingQuote));

    // When
    quoteService.sellShares(symbol, numberOfShares);

    // Then
    verify(quoteDao, times(1)).findById(eq(symbol));
    verify(quoteDao, times(1)).update(any(Quote.class));
  }

  @Test
  public void testSellShares_InvalidSymbol() {
    // Given
    String invalidSymbol = "INVALID";
    when(quoteDao.findById(eq(invalidSymbol))).thenReturn(Optional.empty());

    // When
    try {
      quoteService.sellShares(invalidSymbol, 10);
      // The above line should throw IllegalArgumentException
    } catch (IllegalArgumentException e) {
      // Then: expect IllegalArgumentException
      assertEquals("Invalid symbol: " + invalidSymbol, e.getMessage());

    }
  }


//  @Test(expected = IllegalArgumentException.class)
//  public void testSellShares_NotEnoughShares() {
//    // Given
//    String symbol = "AAPL";
//   when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 5,
//       new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()))));
//
//    try {
//      quoteService.sellShares(symbol, 1000);
//      // The above line should throw IllegalArgumentException
//    } catch (IllegalArgumentException e) {
//      // Then: expect IllegalArgumentException
//      assertEquals("Not Enough Shares: " + symbol, e.getMessage());
//
//    }
//  }

  @Test
  public void testSellShares_NotEnoughShares() {
    // Given
    String symbol = "AAPL";
    Quote existingQuote = new Quote(symbol, 150.0, 160.0, 140.0, 155.0, 5,
        new Date(), 145.0, 10.0, "5%", new java.sql.Timestamp(System.currentTimeMillis()));

    // Mock behavior
    when(quoteDao.findById(eq(symbol))).thenReturn(Optional.of(existingQuote));

    // When and Then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      quoteService.sellShares(symbol, 1000);
    });

    // Verify that findById is called
    verify(quoteDao, times(1)).findById(eq(symbol));
    // Verify that update is never called
    verify(quoteDao, never()).update(any(Quote.class));

    // Assert the exception message if needed
    assertEquals("Not Enough Shares: " + symbol, exception.getMessage());
  }



}
