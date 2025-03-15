package tqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import tqs.connection.ISimpleHttpClient;

import java.io.IOException;
import java.util.Optional;

public class ProductFinderService {
    private static final String API_PRODUCTS = "https://fakestoreapi.com/products/";
    private ISimpleHttpClient httpClient;
    private ObjectMapper objectMapper;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public Optional<Product> findProductDetails(Integer productId) {
        String url = API_PRODUCTS + productId;
        try {
            String jsonResponse = httpClient.doHttpGet(url);
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                return Optional.empty();
            }

            // Convert JSON to Product object
            Product product = objectMapper.readValue(jsonResponse, Product.class);
            return Optional.of(product);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
