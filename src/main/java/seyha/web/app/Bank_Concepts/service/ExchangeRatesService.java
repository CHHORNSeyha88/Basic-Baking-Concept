package seyha.web.app.Bank_Concepts.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Getter

public class ExchangeRatesService {

  private final RestTemplate restTemplate;

  private final Map<String,Double> rates = new HashMap<String,Double>();

  private final Set<String> CURRENCIES = Set.of(
          "USD",
          "KHR",
          "CNY",
          "EUR",
          "GBP",
          "JPY",
          "CHF",
          "CAD",
          "AUD",
          "NZD"
  );
//  https://api.currencyapi.com/v3/latest?apikey=
  @Value("${currencyApiKey}")
  private String apiKey;

  public void getExchangeService(){
    String CURRENCIES_API ="https://api.currencyapi.com/v3/latest?apikey=";
    var response = restTemplate.getForEntity(CURRENCIES_API+apiKey, JsonNode.class);
    var data = Objects.requireNonNull(response.getBody()).get("data");

    for(var currency:CURRENCIES){
      rates.put(currency,data.get(currency).get("value").doubleValue());
    }
    System.out.println(rates);
  }
}

//    data.forEach(currency -> {
//      var currencyName = currency.get("currency_code").asText();
//      var rate = currency.get("rates").get("USD").asDouble();
//      rates.put(currencyName, rate);
//    });