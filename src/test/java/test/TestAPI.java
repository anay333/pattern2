package test;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.Data;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static ru.netology.Data.*;
import static ru.netology.Data.Registration.getRegisteredUser;
import static ru.netology.Data.Registration.getUser;


public class TestAPI {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        given().spec(requestSpec).
                when().get("/api/system/users", registeredUser).

                then().assertThat().statusCode(200).and().body("login", is(registeredUser.getLogin()),"password", is(registeredUser.getPassword()),"status", is(registeredUser.getStatus()));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        given().spec(requestSpec).
        when().get("/api/system/users", notRegisteredUser).

                then().statusCode(400);

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        given().spec(requestSpec).
        when().get("/api/system/users", blockedUser).

                then().statusCode(400);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        given().spec(requestSpec).
        when().get("/api/system/users", new Data.RegistrationDto(wrongLogin,registeredUser.getPassword(),"active") ).

                then().statusCode(400); }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        given().spec(requestSpec).
        when().get("/api/system/users", new Data.RegistrationDto(registeredUser.getLogin(),wrongPassword,"active") ).

                then().statusCode(400);
    }


}


