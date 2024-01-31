package ca.jrvs.apps.jdbc;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import ca.jrvs.apps.jdbc.QuoteDao;

public class PositionService_IntTest {

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
  public void testBuy() throws SQLException {
    // Given
    String ticker = "AAPL";
    int numberOfShares = 10;
    double price = 150.0;

    // Mock behavior for quoteService
    when(quoteService.isValidSymbol(eq(ticker))).thenReturn(true);
    when(quoteService.getLatestQuote(eq(ticker))).thenReturn(new Quote(ticker, price, 0.0, 0.0, 0.0, 100,
        null, 0.0, 0.0, null, null));

    // Mock behavior for positionDao
    when(positionDao.findById(eq(ticker))).thenReturn(Optional.empty());

    // When
    Position result = positionService.buy(ticker, numberOfShares, price);

    // Then
    verify(quoteService, times(1)).isValidSymbol(eq(ticker));
    verify(quoteService, times(1)).getLatestQuote(eq(ticker));
    verify(positionDao, times(1)).findById(eq(ticker));
    verify(positionDao, times(1)).save(any(Position.class));

    assertEquals(ticker, result.getTicker());
    assertEquals(numberOfShares, result.getNumOfShares());
    assertEquals(numberOfShares * price, result.getMarketValue(), 0.001);
  }

  @Test
  public void testSell() throws SQLException {
    // Given
    String ticker = "AAPL";

    // Mock behavior for quoteService
    when(quoteService.isValidSymbol(eq(ticker))).thenReturn(true);
    when(quoteService.getLatestQuote(eq(ticker))).thenReturn(new Quote(ticker, 150.0, 0.0, 0.0, 0.0, 100,
        null, 0.0, 0.0, null, null));

    // Mock behavior for positionDao
    when(positionDao.findById(eq(ticker))).thenReturn(Optional.of(new Position(ticker, 10, 1500.0)));

    // When
    positionService.sell(ticker);

    // Then
    verify(quoteService, times(1)).isValidSymbol(eq(ticker));
    verify(quoteService, times(1)).getLatestQuote(eq(ticker));
    verify(positionDao, times(1)).findById(eq(ticker));
    verify(positionDao, times(1)).delete(any(Position.class));

  }
}
