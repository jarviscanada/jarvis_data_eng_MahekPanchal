package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.annotation.JsonProperty;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.io.IOException;

class AlphaVantageClient {

  public static class Company {
    @JsonProperty("symbol")
    private String symbol;

    // Add other fields as needed

    // Getter and Setter methods
  }

  public interface AlphaVantageService {

    @Headers({
        "X-RapidAPI-Key: 11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d",
        "X-RapidAPI-Host:alpha-vantage.p.rapidapi.com"
    })
    @GET("query")
    Call<Company> getCompanyInfo(
        @Query("function") String function,
        @Query("symbol") String symbol,
        @Query("datatype") String datatype
    );

    @Headers({
        "X-RapidAPI-Key: 11d6aab02dmsh6322f8714c7dc29p1c79b7jsn5cab91b2ec6d",
        "X-RapidAPI-Host:alpha-vantage.p.rapidapi.com"
    })
    @GET("query")
    Call<Company> getQuoteInfo(
        @Query("function") String function,
        @Query("symbol") String symbol,
        @Query("datatype") String datatype
    );
  }

  public static class QuoteHttpHelper {

    private final String apiKey;
    private final AlphaVantageService alphaVantageService;

    public QuoteHttpHelper(String apiKey) {
      this.apiKey = apiKey;
      this.alphaVantageService = createAlphaVantageService();
    }

    public Company fetchCompanyInfo(String ticker) throws IOException {
      Call<Company> call = alphaVantageService.getCompanyInfo("GLOBAL_QUOTE", ticker, "json");
      return call.execute().body();
    }

    public Company fetchQuoteInfo(String symbol) throws IOException {
      Call<Company> call = alphaVantageService.getQuoteInfo("GLOBAL_QUOTE", symbol, "json");
      return call.execute().body();
    }

    private AlphaVantageService createAlphaVantageService() {
      return new Retrofit.Builder()
          .baseUrl("https://alpha-vantage.p.rapidapi.com/")
          .addConverterFactory(JacksonConverterFactory.create())
          .build()
          .create(AlphaVantageService.class);
    }
  }

  public static void main(String[] args) throws IOException {
    String apiKey = "your-api-key";
    QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper(apiKey);

    // Example usage
    Company companyInfo = quoteHttpHelper.fetchCompanyInfo("AAPL");
    //System.out.println("Company Symbol: " + companyInfo.getSymbol());

    Company quoteInfo = quoteHttpHelper.fetchQuoteInfo("AAPL");
    //System.out.println("Quote Symbol: " + quoteInfo.getSymbol());
  }
}


/*package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

public class QuoteHttpHelper {

  private final String apiKey;
  private final ObjectMapper objectMapper;

  public QuoteHttpHelper(String apiKey) {
    this.apiKey = apiKey;
    this.objectMapper = new ObjectMapper();
  }

  public Company fetchCompanyInfo(String ticker) throws IOException {
    String url = buildUrl(ticker);
    Response response = Request.Get(url).execute();
    String responseBody = response.returnContent().asString();
    return objectMapper.readValue(responseBody, Company.class);
  }

  public Company fetchQuoteInfo(String symbol) throws IOException {
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
    Response response = Request.Get(url)
        .addHeader("X-RapidAPI-Key", apiKey)
        .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .execute();
    String responseBody = response.returnContent().asString();
    return objectMapper.readValue(responseBody, Company.class);
  }

  private String buildUrl(String ticker) {
    return "https://some-api.com/company?symbol=" + ticker + "&apikey=" + apiKey;
  }


}*/
