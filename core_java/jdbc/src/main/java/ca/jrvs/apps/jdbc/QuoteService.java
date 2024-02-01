package ca.jrvs.apps.jdbc;

import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private final QuoteDao quoteDao;
  private final QuoteHttpHelper quoteHttpHelper;


  public QuoteService(QuoteDao quoteDao, QuoteHttpHelper quoteHttpHelper) {
    this.quoteDao = quoteDao;
    this.quoteHttpHelper = quoteHttpHelper;
  }

  /**
   * Fetch the latest quote data for a given symbol and save it to the database.
   *
   * @param symbol Stock symbol
   * @return Saved Quote object
   * @throws IOException If an error occurs during the HTTP request
   */
  public Quote saveQuote(String symbol) throws IOException {
    Quote quote = quoteHttpHelper.fetchQuoteInfo(symbol);
    if (quote != null) {
      // Ensure that the save method signature is correct in QuoteDao
      quoteDao.save(quote);
    }
    return quote;
  }

  /**
   * Validate if the given symbol is valid by checking if it exists in the database.
   *
   * @param symbol Stock symbol
   * @return True if the symbol is valid, false otherwise
   */
  public boolean isValidSymbol(String symbol) {
    Optional<Quote> quote = quoteDao.findById(symbol);
    return quote.isPresent();
  }

  /**
   * Buy shares for a given symbol, checking if the requested volume is available.
   *
   * @param symbol Stock symbol
   * @param volume Number of shares to buy
   * @return True if the purchase is successful, false otherwise
   * @throws IllegalArgumentException If the symbol is not valid
   */
  public boolean buyShares(String symbol, int volume) {
    Optional<Quote> quoteOptional = quoteDao.findById(symbol);
    if (quoteOptional.isPresent()) {
      Quote quote = quoteOptional.get();
      int availableVolume = quote.getVolume();
      if (volume <= availableVolume) {
        // Update the volume and save the quote
        quote.setVolume(availableVolume - volume);
        quoteDao.update(quote);
        return true;
      }
    }
    return false;
  }


  /**
   * Get the latest quote for a given symbol.
   *
   * @param symbol Stock symbol
   * @return Latest Quote object
   * @throws IllegalArgumentException If the symbol is not valid
   */
  public Quote getLatestQuote(String symbol) {
    Optional<Quote> quoteOptional = quoteDao.findById(symbol);
    return quoteOptional.orElseThrow(() -> new IllegalArgumentException("Invalid symbol: " + symbol));
  }

  /******************************************************************
   * Updated method
   * */
  public boolean sellShares(String symbol, int volume) {
    Optional<Quote> quoteOptional = quoteDao.findById(symbol);
    if (quoteOptional.isPresent()) {
      Quote quote = quoteOptional.get();
      int availableVolume = quote.getVolume();
      // You might want to add additional checks here based on your requirements
      // For example, check if the user has enough shares to sell
      if (volume <= availableVolume) {
        // Update the volume and save the quote
        quote.setVolume(availableVolume + volume);
        quoteDao.update(quote);
        return true;
      }
    }
    return false;
  }

}
