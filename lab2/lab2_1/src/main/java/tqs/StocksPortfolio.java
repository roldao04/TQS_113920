package tqs;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class StocksPortfolio {
    private IStockmarketService stockmarket;
    private List<Stock> stocks;

    public StocksPortfolio(IStockmarketService stockmarket) {
        this.stockmarket = stockmarket;
        this.stocks = new ArrayList<>();
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public double totalValue() {
        return stocks.stream()
                .mapToDouble(stock -> stock.getQuantity() * stockmarket.lookUpPrice(stock.getLabel()))
                .sum();
    }

    public List<Stock> mostValuableStocks(int topN) {
        return stocks.stream()
                .sorted((s1, s2) -> Double.compare(
                        stockmarket.lookUpPrice(s2.getLabel()) * s2.getQuantity(),
                        stockmarket.lookUpPrice(s1.getLabel()) * s1.getQuantity()))
                .limit(topN)
                .collect(Collectors.toList());
    }
}

