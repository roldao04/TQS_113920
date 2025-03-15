import tqs.connection.TqsBasicHttpClient;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import tqs.Product;
import tqs.ProductFinderService;

public class ProductFinderServiceIT {

    // Use real HTTP client (no mocks)
    private final ProductFinderService productFinderService = new ProductFinderService(new TqsBasicHttpClient());

    @Test
    public void testFindProductDetails_RealAPI_ValidProduct() {
        // Fetch product ID 3 from real API
        Optional<Product> result = productFinderService.findProductDetails(3);

        // Assertions to verify the real API response
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getId(), is(3));
        assertThat(result.get().getTitle(), is(not(emptyOrNullString())));
        assertThat(result.get().getPrice(), is(greaterThan(0.0)));
    }

    @Test
    public void testFindProductDetails_RealAPI_InvalidProduct() {
        // Fetch product ID 300 (which should not exist)
        Optional<Product> result = productFinderService.findProductDetails(300);

        // Assertions: API should return an empty Optional
        assertThat(result.isPresent(), is(false));
    }
}
