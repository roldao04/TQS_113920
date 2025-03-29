package tqs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;

public class BookSearchSteps {
    Library library = new Library();
    List<Book> result = new ArrayList<>();

    // Using Cucumber Expressions
    @Given("a book with the title {string}, written by {string}, published in {iso8601Date}")
    public void addNewBook(final String title, final String author, final Date published) {
        Book book = new Book(title, author, published);
        library.addBook(book);
    }

    // Searching for books within a date range
    @When("the customer searches for books published between {iso8601Date} and {iso8601Date}")
    public void setSearchParameters(final Date from, final Date to) {
        result = library.findBooks(from, to);
    }

    @Then("{int} books should have been found")
    public void verifyAmountOfBooksFound(final int booksFound) {
        assertThat(result.size(), equalTo(booksFound));
    }

    @And("Book {int} should have the title {string}")
    public void verifyBookAtPosition(final int position, final String title) {
        assertThat(result.get(position - 1).getTitle(), equalTo(title));
    }
}
