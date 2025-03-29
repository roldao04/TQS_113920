Feature: Book search
  To allow a customer to find their favorite books quickly, the library must offer multiple ways to search for books.

  Scenario: Search books by publication year
    Given a book with the title "One good book", written by "Anonymous", published in "2013-03-14"
    And a book with the title "Some other book", written by "Tim Tomson", published in "2014-08-23"
    And a book with the title "How to cook a dino", written by "Fred Flintstone", published in "2012-01-01"
    When the customer searches for books published between "2013-01-01" and "2014-12-31"
    Then 2 books should have been found
    And Book 1 should have the title "Some other book"
    And Book 2 should have the title "One good book"

  Scenario: Search books by author
    Given the following books exist in the library:
      | title                | author          | published   |
      | Book 1               | Author A        | 2012-01-01  |
      | Book 2               | Author B        | 2014-05-06  |
      | Book 3               | Author A        | 2015-07-09  |
    When the customer searches for books written by "Author A"
    Then 2 books should have been found
    And Book 1 should have the title "Book 1"
    And Book 2 should have the title "Book 3"
