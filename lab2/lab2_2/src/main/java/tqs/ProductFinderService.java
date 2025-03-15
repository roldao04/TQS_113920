package tqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;

public class ProductFinderService {
    private static final String API_PRODUCTS = "https://fakestoreapi.com/products/";
    private ISimpleHttpClient httpClient;
    private ObjectMapper objectMapper; // JSON parser

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper(); // Initialize Jackson ObjectMapper
    }

    public Optional<Product> findProductDetails(Integer productId) {
        String url = API_PRODUCTS + productId;
        try {
            String jsonResponse = httpClient.doHttpGet(url);
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                return Optional.empty();
            }

            // Convert JSON response to Product object
            Product product = objectMapper.readValue(jsonResponse, Product.class);
            return Optional.of(product);
        } catch (IOException e) {
            e.printStackTrace(); // Log the error 
            return Optional.empty();
        }
    }
}
