package ca.jrvs.apps.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PositionService_UnitTest {

  private PositionDao positionDao;
  private QuoteService quoteService;
  private PositionService positionService;

  @Before
  public void setup() {
    positionDao = mock(PositionDao.class);
    quoteService = mock(QuoteService.class);
    positionService = new PositionService(positionDao, quoteService);
  }

  @Test
  public void testBuy() {
    // Given
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    Quote latestQuote = new Quote();
    latestQuote.setVolume(20);

    Position existingPosition = new Position(ticker, 5, 750.0);

    when(quoteService.isValidSymbol(ticker)).thenReturn(true);
    when(quoteService.getLatestQuote(ticker)).thenReturn(latestQuote);
    when(positionDao.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // When
    Position resultPosition = positionService.buy(ticker, numberOfShares, price);

    // Then
    assertEquals(ticker, resultPosition.getTicker());
    assertEquals(15, resultPosition.getNumOfShares());
    assertEquals(2250.0, resultPosition.getMarketValue(), 0.001);

    // Verify that methods were called
    verify(positionDao, times(1)).findById(ticker);
    verify(positionDao, times(1)).update(any(Position.class));
    verify(positionDao, never()).save(any(Position.class));
    verify(quoteService, times(1)).buyShares(ticker, numberOfShares);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuy_InvalidSymbol() {
    // Given
    String invalidTicker = "INVALID";
    when(quoteService.isValidSymbol(invalidTicker)).thenReturn(false);

    // When
    positionService.buy(invalidTicker, 10, 150.0);

  }

  @Test
  public void testBuy_NotEnoughVolume() {
    // Given
    String ticker = "AAPL";
    int numberOfShares = 30;
    double price = 150.0;

    Quote latestQuote = new Quote();
    latestQuote.setVolume(20);

    when(quoteService.isValidSymbol(ticker)).thenReturn(true);
    when(quoteService.getLatestQuote(ticker)).thenReturn(latestQuote);

    // When and Then: expect IllegalArgumentException with the correct message
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      positionService.buy(ticker, numberOfShares, price);
    });
    assertEquals("Not enough available volume for purchase", exception.getMessage());
//    assertEquals("Not enough volume to buy " + numberOfShares + " shares of " + ticker, exception.getMessage());
  }

//  @Test
//  public void testBuy_NotEnoughVolume() {
//    // Given
//    String ticker = "AAPL";
//    int numberOfShares = 30;
//    double price = 150.0;
//
//    Quote latestQuote = new Quote();
//    latestQuote.setVolume(20);
//
//    when(quoteService.isValidSymbol(ticker)).thenReturn(true);
//    when(quoteService.getLatestQuote(ticker)).thenReturn(latestQuote);
//
//    // When and Then: expect IllegalArgumentException with the correct message
//    try {
//      positionService.buy(ticker, numberOfShares, price);
//      fail("Expected IllegalArgumentException was not thrown");
//    } catch (IllegalArgumentException e) {
//      assertEquals("Not enough volume to buy " + numberOfShares + " shares of " + ticker, e.getMessage());
//    }
//  }

  @Test
  public void testSell() throws SQLException {
    // Given
    String ticker = "AAPL";
    Position existingPosition = new Position(ticker, 10, 1500.0);

    when(quoteService.isValidSymbol(ticker)).thenReturn(true);
    when(quoteService.getLatestQuote(ticker)).thenReturn(new Quote());
    when(positionDao.findById(ticker)).thenReturn(Optional.of(existingPosition));

    // When
    positionService.sell(ticker);

    // Then
    // Verify that methods were called
    verify(positionDao, times(1)).findById(ticker);
    verify(positionDao, times(1)).delete(any(Position.class));
    verify(quoteService, times(1)).sellShares(ticker, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSell_InvalidSymbol() {
    // Given
    String invalidTicker = "INVALID";
    when(quoteService.isValidSymbol(invalidTicker)).thenReturn(false);

    // When
    positionService.sell(invalidTicker);

    // Then: expect IllegalArgumentException
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSell_NoSharesToSell() {
    // Given
    String ticker = "AAPL";
    when(quoteService.isValidSymbol(ticker)).thenReturn(true);
    when(positionDao.findById(ticker)).thenReturn(Optional.empty());

    // When
    positionService.sell(ticker);

    // Then: expect IllegalArgumentException
  }
}
