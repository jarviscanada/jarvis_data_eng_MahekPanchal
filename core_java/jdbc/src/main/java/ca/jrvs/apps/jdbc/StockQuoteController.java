package ca.jrvs.apps.jdbc;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockQuoteController {
  private static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
  private QuoteService quoteService;
  private PositionService positionService;

  // Constructor to inject services
  public StockQuoteController(QuoteService quoteService, PositionService positionService) {
    this.quoteService = quoteService;
    this.positionService = positionService;
  }

  public void initClient() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      logger.info("1. Buy Stock");
      logger.info("2. Sell Stock");
      logger.info("3. Exit");

      logger.info("Enter your choice: ");
      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1:
          buyStock(scanner);
          break;
        case 2:
          sellStock(scanner);
          break;
        case 3:
          logger.info("Exiting the application");
          System.exit(0);
        default:
          logger.error("Invalid choice. Try again");
      }
    }
  }

  private void buyStock(Scanner scanner) {
    logger.info("Enter stock symbol to buy: ");
    String symbol = scanner.nextLine();

    logger.info("Enter the number of shares to buy: ");
    int volume = scanner.nextInt();

    logger.info("Enter the price per share: ");
    double price = scanner.nextDouble();

    Position position = positionService.buy(symbol, volume, price);
    logger.info("Successfully bought {} shares of {} at ${} each.", volume, symbol, price);
  }


  private void sellStock(Scanner scanner) {
    logger.info("Enter stock symbol to sell: ");
    String symbol = scanner.nextLine();

    positionService.sell(symbol);
    logger.info("Successfully sold all shares of {}.", symbol);
  }

//  private void viewPortfolio() {
//    Scanner scanner = new Scanner(System.in);
//
//    logger.info("Enter stock symbol to view portfolio: ");
//    String symbol = scanner.nextLine();
//
//    if (quoteService.isValidSymbol(symbol)) {
//      Quote latestQuote = quoteService.getLatestQuote(symbol);
//      Position position = positionService.getPosition(symbol);
//      logger.info("Symbol: {}", symbol);
//      logger.info("Latest Price: ${}", latestQuote.getPrice());
//      logger.info("Volume: {}", position.getNumOfShares());
//      logger.info("Market Value: ${}", position.getMarketValue());
//
//    } else {
//      logger.info("Invalid symbol. Please enter a valid stock symbol.");
//    }
//  }

}
