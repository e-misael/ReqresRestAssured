package in.regres.tests;

import in.regres.config.Configuration;
import in.regres.factory.UserDataFactory;
import in.regres.pojo.UserDto;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserContractTest {
    private static final String create_user_endpoint = "/users";
    private static final String list_users_endpoint = "/users/";

    @Before
    public void setUp() {

        // RestAssured configuration
        Configuration configuration = ConfigFactory.create(Configuration.class);
        baseURI = configuration.baseURI();
        basePath = configuration.basePath();

        enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @Test
    public void testShouldCreateAnUserContract() throws IOException {

        // Instantiating an object UserDto using the previously data setted.
        UserDto user = UserDataFactory.createAValidUser();

        // Creating an user
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(create_user_endpoint)
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(matchesJsonSchemaInClasspath("schemas/postUser-ValidUser.json"));
    }

    @Test
    public void testShouldNotAllowUnnamedUserContract() throws IOException {

        // Instantiating an object UserDto using the previously data setted.
        UserDto user = UserDataFactory.createAnUnnamedUser();

        // Creating an unnamed user. Considering name as required field, this test should fail.
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(create_user_endpoint)
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(matchesJsonSchemaInClasspath("schemas/postUser-ValidUser.json"));
    }

    @Test
    public void testShouldGetAnUniqueUserContract() {

        // Getting user data
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(list_users_endpoint+"1")
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("schemas/getUser-UniqueUser.json"));
    }
}