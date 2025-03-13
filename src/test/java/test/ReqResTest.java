package test;

import io.qameta.allure.Owner;
import models.SingleUserNotFound;
import models.SuccessfulGetSingleUser;
import models.SuccessfulRegister;
import models.UnsuccessfulRegistration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Spec.*;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("Test")
@Owner(value = "Genkel Veronika")
public class ReqResTest extends TestBase {
    @Test
    void successfulGetSingleUserTest() {
        SuccessfulGetSingleUser response = step ("Make request", ()->
                given()
                .spec(userSpec)
                .when()
                .get("/users/4")
                .then()
                .spec(successfulGetSingleUserResponseSpec)
                .extract().as(SuccessfulGetSingleUser.class)
        );
        step("Check response", ()-> {
            assertThat(response.getData().getId()).isEqualTo(4);
            assertThat(response.getData().getEmail()).isEqualTo("eve.holt@reqres.in");
            assertThat(response.getData().getFirst_name()).isEqualTo("Eve");
            assertThat(response.getData().getLast_name()).isEqualTo("Holt");
            assertThat(response.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/4-image.jpg");
            assertThat(response.getSupport().getUrl()).isEqualTo("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral");
            assertThat(response.getSupport().getText()).isEqualTo("Tired of writing endless social media content? Let Content Caddy generate it for you.");
        });
    }

    @Test
    void singleUserNotFoundTest() {
        SingleUserNotFound response = step("Make request", ()-> 
                given()
                .when()
                .spec(userSpec)
                .get("/users/23")
                .then()
                .spec(singleUserNotFoundSpec)
                .extract().as(SingleUserNotFound.class)
        );
        step("Check response", ()->
                assertNotNull(response, "Response should not be null")
        );
    }

    @Test
    public void successfulRegisterTest() {
        SuccessfulRegister data = new SuccessfulRegister();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("pistol");
        SuccessfulRegister response = step("Make register request", () -> 
                given()
                .spec(userSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(successfulGetSingleUserResponseSpec)
                .extract().as(SuccessfulRegister.class)
        );
        step("Check response", () -> {
            assertNotNull(response, "Response should not be null");
            assertEquals(4, response.getId(), "Expected id to be 4");
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken(), "Expected token to be 'QpwL5tke4Pnpja7X4'");
        });
    }

    @Test
    void unsuccessfulRegistrationWithOutPasswordTest() {
        SuccessfulRegister data = new SuccessfulRegister();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("");
        UnsuccessfulRegistration response = step("Make register request", () -> 
                given()
                .spec(userSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationSpec)
                .extract().as(UnsuccessfulRegistration.class)
        );
        step("Check response", () -> {
            assertEquals("Missing password", response.getError());
        });
    }

    @Test
    void unsuccessfulRegistrationWithOutEmailTest() {
        SuccessfulRegister data = new SuccessfulRegister();
        data.setEmail("");
        data.setPassword("pistol");
        UnsuccessfulRegistration response = step("Make register request", () -> 
                given()
                .spec(userSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationSpec)
                .extract().as(UnsuccessfulRegistration.class)
        );
        step("Check response", () -> {
            assertEquals("Missing email or username", response.getError());
        });
    }

}