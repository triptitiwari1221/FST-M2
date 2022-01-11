package activities;


import static io.restassured.RestAssured.given;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestAssuredProject {
	
	RequestSpecification requestSpec;

	ResponseSpecification responseSpec;
	
	String gitHubAccessToken = "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";
	String sshKey;
	int sshId;
	String BASE_URI = "https://api.github.com";

	@BeforeClass
	public void setUp() {

		// Create request specification
		requestSpec = new RequestSpecBuilder()
				// Set content type
				.setContentType(ContentType.JSON)
				// set header as githubaccesstoken
				.addHeader("Authorization", "token " + gitHubAccessToken)
				// Set base URL
				.setBaseUri(BASE_URI)
				// Build request specification
				.build();

		responseSpec = new ResponseSpecBuilder()
				// Check response content type
				.expectContentType("application/json")
				// Check if response contains name property
				// Build response specification
				.build();
	}

	// post request
	@Test(priority = 1)
	public void addSSHKey() {
		String reqBody = "{\"title\": \"TestAPIKey\", \"key\":\"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQC05wwkOMGBMobDW5MyBfTBZj5KoemKDCO1xkydAyeEVejVJvI+2vnr9+wWZ3BfUQiOCjgEdAscO0OMqQLhLWjBzKLYwkNj9eOZyVMVxDMvnxG0GEHsQ9x6zlK0rNC4EPwy0kZgz+tUO+A6F8hJ6KS6x4OeN+jCmEK1OwYCQ6LgwnK0PgiKg6O9/bmJU+AiPCzrR4Xt1hWnMWmwJk8m2MQNgrw7thYauvRZ9vELSAosSjlrfuExdTHdw9wevCxpMWE11wLFEx3yNo9seb4R9fQIsIM8sVyh7dLGVKboC0duDculRlE3sEgJt5Vmc4EkB7Qxi3e/4s7czojOyJKBJrgBX4yG8mKypAIjLOcvz0e/GEL5g1eGXtxmbbQ1mDOmVSp+9BTQdkGiYBoF3+vzwr8WRPzspg4xjztEfqE5PkYwD7OWjgVkAOLYDqelfxf3jFQIzgPQ5KdUyNaI/189qZAMxNggnloyXyfoSKCkNzFjslz/Mt3TzuYpMusrGgC5jjM="
				+ "\"}";
		Response response = given().spec(requestSpec) // Use requestSpec
				.body(reqBody) // Send request body
				.when().post(BASE_URI + "/user/keys"); // Send POST request

		System.out.println(response.asPrettyString());
		// Additional Assertion Use responseSpec
		response.then().spec(responseSpec).statusCode(201);

		// save sshId
		sshId = response.then().extract().path("id");

	}

	// get request
	@Test(priority = 2)
	public void getSSHKeys() {
		Response response = given().spec(requestSpec) // Use requestSpec
				.when().get(BASE_URI + "/user/keys"); // Send GET request

		// Print response
		Reporter.log(response.asString());

		// Assertions
		response.then().spec(responseSpec) // Use responseSpec
				.statusCode(200); // Additional Assertion
	}

	
	  //delete request	  
	  @Test(priority=3)
	  public void deleteSSHKeys(){
		  Response response = given().spec(requestSpec) // Use requestSpec
	                .pathParam("keyId", sshId) // Add path parameter
	                .when().get(BASE_URI +"/user/keys/{keyId}"); // Send GET request
	  
			Reporter.log(response.asString());
			
	  // Assertions 
		  response.then().spec(responseSpec) // Use responseSpec
			.statusCode(200); 
		  
	  }
	  
	 
}