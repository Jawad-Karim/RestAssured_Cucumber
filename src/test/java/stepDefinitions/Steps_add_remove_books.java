package stepDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.testng.Assert;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Steps_add_remove_books {

	private static final String USER_ID = "3473b282-689d-4144-9450-5503059edcc4";
	private static final String USERNAME = "EndToEndTest";
	private static final String PASSWORD = "Test@1234";
	private static final String BASE_URL = "https://bookstore.toolsqa.com";
	
	private static String token;
	private static Response response;
	private static String jsonString;
	private static String bookId;


	@Given("I am an authorized user")
	public void iAmAnAuthorizedUser() {

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Content-Type", "application/json");
		response = request.body("{ \"userName\":\" "+USERNAME+" \", \"password\":\" "+PASSWORD+" \"}")
				.post("/Account/v1/GenerateToken");

		String jsonString = response.asString();
		token = JsonPath.from(jsonString).get("token");
		System.out.println("token: "+token);

	}

	@Given("A list of books are available")
	public void listOfBooksAreAvailable() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		response = request.get("/BookStore/v1/Books");

		jsonString = response.asString();
		List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
		Assert.assertTrue(books.size() > 0);
		
		System.out.println(books);

		bookId = books.get(0).get("isbn");
		System.out.println("book id: "+bookId);
	}

	@When("I add a book to my reading list")
	public void addBookInList() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");
		
		
		List<String> isbnList = new ArrayList<String>();
		isbnList.add(bookId);
		
		JSONObject book = new JSONObject();
		book.put("userId", USER_ID);
		book.put("collectionOfIsbns", isbnList);
			

		response = request.body(book)
				.post("/BookStore/v1/Books");
	}

	@Then("The book is added")
	public void bookIsAdded() {
		Assert.assertEquals(response.statusCode(), 201);
	}

	@When("I remove a book from my reading list")
	public void removeBookFromList() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.body("{ \"isbn\": \" "+bookId+" \", \"userId\": \" "+USER_ID+" \"}")
				.delete("/BookStore/v1/Book");


	}

	@Then("The book is removed")
	public void bookIsRemoved() {
		Assert.assertEquals(204, response.getStatusCode());

		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();

		request.header("Authorization", "Bearer " + token)
		.header("Content-Type", "application/json");

		response = request.get("/Account/v1/User/" + USER_ID);
		Assert.assertEquals(200, response.getStatusCode());

		jsonString = response.asString();
		List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
		Assert.assertEquals(0, booksOfUser.size());
	}
}
