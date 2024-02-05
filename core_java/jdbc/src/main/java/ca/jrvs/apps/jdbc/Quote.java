package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

public class Quote {

  @JsonProperty("01. symbol")
  private String ticker; // id

  @JsonProperty("02. open")
  private double open;

  @JsonProperty("03. high")
  private double high;

  @JsonProperty("04. low")
  private double low;

  @JsonProperty("05. price")
  private double price;

  @JsonProperty("06. volume")
  private int volume;

  @JsonProperty("07. latest trading day")
  private Date latestTradingDay;

  @JsonProperty("08. previous close")
  private double previousClose;

  @JsonProperty("09. change")
  private double change;

  @JsonProperty("10. change percent")
  private String changePercent;

  @JsonProperty("11. timestamp")
  private Timestamp timestamp;

  @JsonProperty("companyName")
  private String companyName;

  public Quote() {
  }

  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Quote(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

//  public Quote(String ticker, double open, double high, double low, double price, int volume,
//      Date latestTradingDay, double previousClose, double change, String changePercent,
//      Timestamp timestamp) {
//    this.ticker = ticker;
//    this.open = open;
//    this.high = high;
//    this.low = low;
//    this.price = price;
//    this.volume = volume;
//    this.latestTradingDay = latestTradingDay;
//    this.previousClose = previousClose;
//    this.change = change;
//    this.changePercent = changePercent;
//    this.timestamp = timestamp;
//  }

  public Quote(String ticker, double open, double high, double low, double price, int volume,
      Date latestTradingDay, double previousClose, double change, String changePercent,
      Timestamp timestamp) {
    this.ticker = ticker;
    this.open = open;
    this.high = high;
    this.low = low;
    this.price = price;
    this.volume = volume;
    this.latestTradingDay = latestTradingDay;
    this.previousClose = previousClose;
    this.change = change;
    this.changePercent = changePercent;
    this.timestamp = (timestamp != null) ? timestamp : new Timestamp(System.currentTimeMillis());
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public double getOpen() {
    return open;
  }

  public void setOpen(double open) {
    this.open = open;
  }

  public double getHigh() {
    return high;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public double getLow() {
    return low;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  public Date getLatestTradingDay() {
    return latestTradingDay;
  }

  public void setLatestTradingDay(Date latestTradingDay) {
    this.latestTradingDay = latestTradingDay;
  }

  public double getPreviousClose() {
    return previousClose;
  }

  public void setPreviousClose(double previousClose) {
    this.previousClose = previousClose;
  }

  public double getChange() {
    return change;
  }

  public void setChange(double change) {
    this.change = change;
  }

  public String getChangePercent() {
    return changePercent;
  }

  public void setChangePercent(String changePercent) {
    this.changePercent = changePercent;
  }

  public Timestamp getTimestamp() {
    if (latestTradingDay != null) {
      return new Timestamp(latestTradingDay.getTime());
    }
    return null; // or return a default Timestamp if appropriate
  }


//  public Timestamp getTimestamp() {
//    return timestamp;
//  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
}

