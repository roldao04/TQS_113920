import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import tqs.IStockmarketService;
import tqs.Stock;
import tqs.StocksPortfolio;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {

    @Mock
    private IStockmarketService stockmarketService;

    @InjectMocks
    private StocksPortfolio portfolio;

    @BeforeEach
    void setUp() {
        portfolio = new StocksPortfolio(stockmarketService);
    }

    @Test
    public void testMostValuableStocks() {
        // Define stock market values
        when(stockmarketService.lookUpPrice("AAPL")).thenReturn(150.0);
        when(stockmarketService.lookUpPrice("GOOGL")).thenReturn(2800.0);
        when(stockmarketService.lookUpPrice("MSFT")).thenReturn(300.0);
        when(stockmarketService.lookUpPrice("TSLA")).thenReturn(700.0);
        when(stockmarketService.lookUpPrice("AMZN")).thenReturn(3500.0);

        // Add stocks to portfolio
        portfolio.addStock(new Stock("AAPL", 5));   // 5 * 150 = 750
        portfolio.addStock(new Stock("GOOGL", 1));  // 1 * 2800 = 2800
        portfolio.addStock(new Stock("MSFT", 10));  // 10 * 300 = 3000
        portfolio.addStock(new Stock("TSLA", 3));   // 3 * 700 = 2100
        portfolio.addStock(new Stock("AMZN", 2));   // 2 * 3500 = 7000

        // Execute test: Get top 3 most valuable stocks
        List<Stock> topStocks = portfolio.mostValuableStocks(3);

        // Expected order: AMZN (7000), MSFT (3000), GOOGL (2800)
        assertThat(topStocks, hasSize(3));
        assertThat(topStocks.get(0).getLabel(), is("AMZN"));
        assertThat(topStocks.get(1).getLabel(), is("MSFT"));
        assertThat(topStocks.get(2).getLabel(), is("GOOGL"));
    }

    @Test
    public void testMostValuableStocksEdgeCase_EmptyPortfolio() {
        // Execute test on an empty portfolio
        List<Stock> topStocks = portfolio.mostValuableStocks(3);

        // Should return an empty list
        assertThat(topStocks, is(empty()));
    }

    @Test
    public void testMostValuableStocksEdgeCase_LessThanRequested() {
        // Define stock market values
        when(stockmarketService.lookUpPrice("AAPL")).thenReturn(150.0);
        when(stockmarketService.lookUpPrice("GOOGL")).thenReturn(2800.0);

        // Add only two stocks to portfolio
        portfolio.addStock(new Stock("AAPL", 5));   // 5 * 150 = 750
        portfolio.addStock(new Stock("GOOGL", 1));  // 1 * 2800 = 2800

        // Execute test: Get top 3 most valuable stocks (only 2 exist)
        List<Stock> topStocks = portfolio.mostValuableStocks(3);

        // Should return only 2 stocks
        assertThat(topStocks, hasSize(2));
        assertThat(topStocks.get(0).getLabel(), is("GOOGL"));
        assertThat(topStocks.get(1).getLabel(), is("AAPL"));
    }
}