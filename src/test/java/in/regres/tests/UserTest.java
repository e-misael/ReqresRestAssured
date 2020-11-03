package in.regres.tests;

import in.regres.config.Configuration;
import in.regres.factory.UserDataFactory;
import in.regres.pojo.UserDto;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class UserTest {
    @Before
    public void setUp() {

        // RestAssured configuration
        Configuration configuration = ConfigFactory.create(Configuration.class);
        baseURI = configuration.baseURI();
        basePath = configuration.basePath();

    }

    @Test
    public void testShouldCreateAnUserSuccessfully() throws IOException {

        // Instanciating an object UserDto using the previously data setted.
        UserDto user = UserDataFactory.createAValidUser();

        // Creating an user
        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(201)
                .body("name", equalToIgnoringCase(user.getName()))
                .body("job", equalTo(user.getJob()))
                .log().body();
    }

    @Test
    public void testShouldNotAllowUnnamedUser() throws IOException {

        // Instanciating an object UserDto using the previously data setted.
        UserDto user = UserDataFactory.createAnUnnamedUser();

        // Creating an unnamed user. Considering name as required field, this test should fail.
        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(400)
                .log().body();
    }

    @Test
    public void testShouldGetAnUniqueUser() {

        UserDto user = UserDataFactory.createAnUserToGet();

        // Creating an user and storing its ID to get later. This step could be done by database script.
        String id =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when()
                        .post("/users")
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .path("id");

        // Getting the created user
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(user.getName()))
                .body("job", equalTo(user.getJob()))
                .log().body();
    }

    @Test
    public void testShouldDeleteAnUserSuccessfully() {

        // Deleting an user
        given()
                .when()
                .delete("/users/1")
                .then()
                .assertThat()
                .statusCode(204)
                .log().body();

        // Searching for deleted user. It shouldn't be found, but it was. This test fails.
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/users/1")
                .then()
                .assertThat()
                .statusCode(404)
                .log().body();
    }
}