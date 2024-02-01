package ca.jrvs.apps.jdbc;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;


@Entity
@Table(name = "position")
public class Position {
  @Id
  @Column(name = "ticker")
  private String ticker;
  @Column(name = "num_of_shares")
  private int numOfShares;
  @Column(name = "market_value")
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
