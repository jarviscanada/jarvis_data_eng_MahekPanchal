package ca.jrvs.apps.jdbc;

public class Position {
  private String ticker;
  private int numOfShares;
  private double marketValue;

  // Constructor
  public Position(String ticker, int numOfShares, double marketValue) {
    this.ticker = ticker;
    this.numOfShares = numOfShares;
    this.marketValue = marketValue;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public int getNumOfShares() {
    return numOfShares;
  }

  public void setNumOfShares(int numOfShares) {
    this.numOfShares = numOfShares;
  }

  public double getMarketValue() {
    return marketValue;
  }

  public void setMarketValue(double marketValue) {
    this.marketValue = marketValue;
  }

  public double getValuePaid() {
    return 0;
  }
}
