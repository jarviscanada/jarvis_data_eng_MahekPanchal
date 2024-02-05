package ca.jrvs.apps.jdbc;

import java.sql.SQLException;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

  private PositionDao positionDao;
  private QuoteService quoteService;

  public PositionService(PositionDao positionDao, QuoteService quoteService) {
    this.positionDao = positionDao;
    this.quoteService = quoteService;
  }

  public Position buy(String ticker, int numberOfShares, double price) {
    if (!quoteService.isValidSymbol(ticker)) {
      throw new IllegalArgumentException("Invalid symbol: " + ticker);
    }

    Quote latestQuote = quoteService.getLatestQuote(ticker);

    if (latestQuote.getVolume() < numberOfShares) {
      throw new IllegalArgumentException("Not enough available volume for purchase");  // RECENT CHANGE
    }

    double marketValue = numberOfShares * price;
    Position position = positionDao.findById(ticker)
        .orElse(new Position(ticker, 0, 0.0));

    position.setNumOfShares(position.getNumOfShares() + numberOfShares);
    position.setMarketValue(position.getMarketValue() + marketValue);

    // Use the existing save and update methods
    if (position.getNumOfShares() == numberOfShares) {
      // Save the new position
      positionDao.save(position);
    } else {
      // Update the existing position
      positionDao.update(position);
    }

    quoteService.buyShares(ticker, numberOfShares);

    return position;
  }

  public void sell(String ticker) {
    if (!quoteService.isValidSymbol(ticker)) {
      throw new IllegalArgumentException("Invalid symbol: " + ticker);
    }

    Quote latestQuote = quoteService.getLatestQuote(ticker);

    Position position = positionDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException("Position not found for symbol: " + ticker));

    if (position.getNumOfShares() > 0) {
      double marketValue = position.getNumOfShares() * latestQuote.getPrice();

      // Use the existing delete method
      try {
        positionDao.delete(position);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      quoteService.sellShares(ticker, position.getNumOfShares());
    } else {
      throw new IllegalArgumentException("No shares to sell for symbol: " + ticker);
    }
  }


}
