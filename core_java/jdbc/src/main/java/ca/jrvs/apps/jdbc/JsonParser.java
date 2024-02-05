package ca.jrvs.apps.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ca.jrvs.apps.jdbc", "ca.jrvs.apps.jdbc.DatabaseConfig"})
public class JsonParser {
  private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
  public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
      throws JsonProcessingException {
    ObjectMapper m = new ObjectMapper();
    if (!includeNullValues) {
      m.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
    }
    if (prettyJson) {
      m.enable(SerializationFeature.INDENT_OUTPUT);
    }
    return m.writeValueAsString(object);
  }

  public static <T> T toObjectFromJson(String json, Class<T> clazz) throws IOException {
    ObjectMapper m = new ObjectMapper();
    //m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return m.readValue(json, clazz);
  }

  public static void main(String[] args) throws IOException {
    SpringApplication.run(JsonParser.class, args);
    //String companyStr = null;


    String companyStr = "{\n"
        + "   \"symbol\":\"AAPL\",\n"
        + "   \"companyName\":\"Apple Inc.\",\n"
        + "   \"exchange\":\"Nasdaq Global Select\",\n"
        + "   \"description\":\"Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers, and sells a variety of related software, services, accessories, networking solutions and third-party digital content and applications.\",\n"
        + "   \"CEO\":\"Timothy D. Cook\",\n"
        + "   \"sector\":\"Technology\",\n"
        + "   \"financials\":[\n"
        + "      {\n"
        + "         \"reportDate\":\"2018-12-31\",\n"
        + "         \"grossProfit\":32031000000,\n"
        + "         \"costOfRevenue\":52279000000,\n"
        + "         \"operatingRevenue\":84310000000,\n"
        + "         \"totalRevenue\":84310000000,\n"
        + "         \"operatingIncome\":23346000000,\n"
        + "         \"netIncome\":19965000000\n"
        + "      },\n"
        + "      {\n"
        + "         \"reportDate\":\"2018-09-30\",\n"
        + "         \"grossProfit\":24084000000,\n"
        + "         \"costOfRevenue\":38816000000,\n"
        + "         \"operatingRevenue\":62900000000,\n"
        + "         \"totalRevenue\":62900000000,\n"
        + "         \"operatingIncome\":16118000000,\n"
        + "         \"netIncome\":14125000000\n"
        + "      }\n"
        + "   ],\n"
        + "   \"dividends\":[\n"
        + "      {\n"
        + "         \"exDate\":\"2018-02-09\",\n"
        + "         \"paymentDate\":\"2018-02-15\",\n"
        + "         \"recordDate\":\"2018-02-12\",\n"
        + "         \"declaredDate\":\"2018-02-01\",\n"
        + "         \"amount\":0.63\n"
        + "      },\n"
        + "      {\n"
        + "         \"exDate\":\"2017-11-10\",\n"
        + "         \"paymentDate\":\"2017-11-16\",\n"
        + "         \"recordDate\":\"2017-11-13\",\n"
        + "         \"declaredDate\":\"2017-11-02\",\n"
        + "         \"amount\":0.63\n"
        + "      }\n"
        + "   ]\n"
        + "}";

    Company company = toObjectFromJson(companyStr, Company.class);
    logger.info(toJson(company, true, false));

  }


}

