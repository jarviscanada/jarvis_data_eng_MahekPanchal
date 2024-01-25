package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteWrapper {

  @JsonProperty("Global Quote")
  private Quote quote;

  public Quote getQuote() {
    return quote;
  }

  public void setQuote(Quote quote) {
    this.quote = quote;
  }
}
