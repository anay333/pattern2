package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.Data;

import java.nio.channels.Selector;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.when;
import static ru.netology.Data.Registration.getRegisteredUser;
import static ru.netology.Data.Registration.getUser;
import static ru.netology.Data.getRandomLogin;
import static ru.netology.Data.getRandomPassword;


public class TestAPI {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

             when().post("/api/system/users", registeredUser).

                then().statusCode(200);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        when().post("/api/system/users", notRegisteredUser).

                then().statusCode(400);

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        when().post("/api/system/users", blockedUser).

                then().statusCode(400);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        when().post("/api/system/users", new Data.RegistrationDto(wrongLogin,registeredUser.getPassword(),"active") ).

                then().statusCode(400); }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        when().post("/api/system/users", new Data.RegistrationDto(registeredUser.getLogin(),wrongPassword,"active") ).

                then().statusCode(400);
    }


}


