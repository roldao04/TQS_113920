import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import tqs.ISimpleHttpClient;
import tqs.Product;
import tqs.ProductFinderService;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {

    @Mock
    private ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService productFinderService;

    @BeforeEach
    void setUp() {
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    public void testFindProductDetails_ValidProduct() {
        // Mock API response for product ID 3
        String jsonResponse = """
        {
            "id": 3,
            "title": "Mens Cotton Jacket",
            "price": 55.99,
            "description": "A great cotton jacket",
            "category": "Men's Clothing",
            "image": "https://example.com/jacket.jpg"
        }
        """;

        // Mock HTTP GET response
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(jsonResponse);

        // Execute method under test
        Optional<Product> result = productFinderService.findProductDetails(3);

        // Assertions
        assertThat(result.isPresent(), is(true));
        Product product = result.get();
        assertThat(product.getId(), is(3));
        assertThat(product.getTitle(), is("Mens Cotton Jacket"));
        assertThat(product.getPrice(), is(55.99));
        assertThat(product.getDescription(), is("A great cotton jacket"));
        assertThat(product.getCategory(), is("Men's Clothing"));
        assertThat(product.getImage(), is("https://example.com/jacket.jpg"));

        // Verify HTTP client call
        verify(httpClient, times(1)).doHttpGet("https://fakestoreapi.com/products/3");
    }

    @Test
    public void testFindProductDetails_InvalidProduct() {
        // Mock API response for non-existing product (ID 300)
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/300")).thenReturn("");

        // Execute method under test
        Optional<Product> result = productFinderService.findProductDetails(300);

        // Assertions: should return an empty Optional
        assertThat(result.isPresent(), is(false));

        // Verify HTTP client call
        verify(httpClient, times(1)).doHttpGet("https://fakestoreapi.com/products/300");
    }
}