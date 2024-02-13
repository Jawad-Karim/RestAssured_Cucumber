package end2end_API_test;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class End2EndTests {
	
	@Test
    public void RegistrationSuccessful() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        /*I have put a unique username and password as below,
        you can enter any as per your liking. */
        requestParams.put("UserName", "TOOLSQA-Test");
        requestParams.put("Password", "Test@@123");

        request.body(requestParams.toJSONString());
        Response response = request.post("/Account/v1/User");

        Assert.assertEquals(response.getStatusCode(), 201);
        // We will need the userID in the response body for our tests, please save it in a local variable
        String userID = response.getBody().jsonPath().getString("userID");
    }

	
	/*
	 1. Test will start from generating Token for Authorization 
	 2. Get List of available books in the library
	 3. Add a book from the list to the user
	 4. Delete the added book from the list of books 
	 5. Confirm if the book removal happens successfully 
	 */
	public static void main(String[] args) {
		String userID = "3473b282-689d-4144-9450-5503059edcc4";
        String userName = "EndToEndTest";
        String password = "Test@1234";
        String baseUrl = "https://bookstore.toolsqa.com";

        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();


        //Step - 1
        //Test will start from generating Token for Authorization
        request.header("Content-Type", "application/json");

        Response response = request.body("{ \"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}")
                .post("/Account/v1/GenerateToken");

        Assert.assertEquals(response.getStatusCode(), 200);

        String jsonString = response.asString();
        Assert.assertTrue(jsonString.contains("token"));

        //This token will be used in later requests
        String token = JsonPath.from(jsonString).get("token");
        //generated token - "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6IkVuZFRvRW5kVGVzdCIsInBhc3N3b3JkIjoiVGVzdEAxMjM0IiwiaWF0IjoxNzA3ODI5ODg4fQ.OsQ4zaYZrFY3gjW1sUrv5fvHhp7Nm4W_hw45jfUFoco";


        //Step - 2
        // Get Books - No Auth is required for this.
        response = request.get("/BookStore/v1/Books");

        Assert.assertEquals(response.getStatusCode(), 200);

        jsonString = response.asString();
        List<Map<String, String>> books = JsonPath.from(jsonString).get("books");
        Assert.assertTrue(books.size() > 0);

         //This bookId will be used in later requests, to add the book with respective isbn
        String bookId = books.get(0).get("isbn");


        //Step - 3
        // Add a book - with Auth
        //The token we had saved in the variable before from response in Step 1, 
        //we will be passing in the headers for each of the succeeding request
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.body("{ \"userId\": \"" + userID + "\", " +
                "\"collectionOfIsbns\": [ { \"isbn\": \"" + bookId + "\" } ]}")
                .post("/BookStore/v1/Books");

        Assert.assertEquals( 201, response.getStatusCode());


        //Step - 4
        // Delete a book - with Auth
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.body("{ \"isbn\": \"" + bookId + "\", \"userId\": \"" + userID + "\"}")
                .delete("/BookStore/v1/Book");

        Assert.assertEquals(204, response.getStatusCode());

        //Step - 5
        // Get User
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        response = request.get("/Account/v1/User/" + userID);
        Assert.assertEquals(200, response.getStatusCode());

        jsonString = response.asString();
        List<Map<String, String>> booksOfUser = JsonPath.from(jsonString).get("books");
        Assert.assertEquals(0, booksOfUser.size());

	}

}
