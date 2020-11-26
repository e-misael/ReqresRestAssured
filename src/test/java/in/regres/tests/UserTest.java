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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class UserTest {
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
    public void testShouldCreateAnUserSuccessfully() throws IOException {

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
                    .body("name", equalToIgnoringCase(user.getName()))
                    .body("job", equalTo(user.getJob()));
    }

    @Test
    public void testShouldNotAllowUnnamedUser() throws IOException {

        // Instantiating an UserDto object using the previously data setted.
        UserDto user = UserDataFactory.createAnUnnamedUser();

        // Creating an unnamed user. Considering name as required field, this test should fail.
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(create_user_endpoint)
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void testShouldGetAnUniqueUser() throws IOException {

        UserDto user = UserDataFactory.createAnUserToGet();

        // Creating an user and storing its ID to get later. This step could be done by database script.
        String id =
            given()
                .contentType(ContentType.JSON)
                .body(user)
            .when()
                .post(create_user_endpoint)
            .then()
                .assertThat()
                    .statusCode(HttpStatus.SC_CREATED)
                    .extract()
                    .path("id");

        // Getting the created user. Nowadays this test is failing.
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(list_users_endpoint + id)
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(id))
                .body("name", equalTo(user.getName()))
                .body("job", equalTo(user.getJob()));
    }

    @Test
    public void testShouldDeleteAnUserSuccessfully() {

        // Deleting an user
        given()
        .when()
            .delete(list_users_endpoint + "1")
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        // Searching for deleted user. It shouldn't be found, but it was. This test fails.
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(list_users_endpoint + "1")
        .then()
            .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}