package tqs;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class Product {
    private Integer id;
    private String image;
    private String description;
    private Double price;
    private String title;
    private String category;

    // Constructor
    public Product(Integer id, String image, String description, Double price, String title, String category) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
    }

    // Default constructor
    public Product() {}

    // Getters and setters
    public Integer getId() { return id; }
    public String getImage() { return image; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }

    public void setId(Integer id) { this.id = id; }
    public void setImage(String image) { this.image = image; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }

    // Equals and hashCode (for object comparison in tests)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
               Objects.equals(image, product.image) &&
               Objects.equals(description, product.description) &&
               Objects.equals(price, product.price) &&
               Objects.equals(title, product.title) &&
               Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, description, price, title, category);
    }
}
