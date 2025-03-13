package test;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class OldReqResTest extends TestBase{
    @Tag("Test")
    @Owner(value = "Genkel Veronika")
    
    @Test
    void successfulGetSingleUserTest() {
        given()
               // .when()
              //  .log().uri()
                .get("/users/4")
                .then()
                //.log().status()
               // .log().body()
                .statusCode(200)
                .body("data.id", is(4))
                .body("data.first_name", is("Eve"))
                .body("data.last_name", is("Holt"));
    }

    @Test
    public void getSingleUserIdAndAvatarTest(){
        String expectedAvatarUrl = "https://reqres.in/img/faces/5-image.jpg";
        int userId = 5;
        given()
                .when()
                .log().uri()
                .get("/users/"+ userId)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id",is(userId))
                .body("data.avatar",is(expectedAvatarUrl));
    }

    @Test
    void singleUserNotFoundTest() {
        given()
                .when()
                .log().uri()
                .get("/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    public void SuccessfulRegisterTest() {
        String data = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";
        given()
                .body(data)
                .contentType(JSON)
                .when()
                .log().all()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unsuccessfulRegistrationWithOutPasswordTest() {
        String data = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"\"}";
        given()
                .body(data)
                .contentType(JSON)
                .when()
                .log().uri()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}