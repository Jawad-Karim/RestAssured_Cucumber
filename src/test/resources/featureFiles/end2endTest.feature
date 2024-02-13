Feature: End to End Tests for ToolsQA bookstore API
  Description the purpose of these tests are to cover End to End Test happy flows.

  Background: User generates token for Authorization
    Given I am an authorized user

  Scenario: Authorized user is able to add and remove a book.
    Given A list of books are available
    When I add a book to my reading list
    Then The book is added
    When I remove a book from my reading list
    Then The book is removed
