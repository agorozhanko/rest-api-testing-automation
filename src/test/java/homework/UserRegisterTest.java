package homework;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.assertions.Assertions;
import lib.constant.Constant;
import lib.constant.ResponseConstant;
import lib.constant.URL;
import lib.core.ApiCoreRequests;
import lib.utility.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Create user with existing email")
    public void testCreateUserWithExistingEmail() {
        String email = Constant.EMAIL_VINKOTOV;

        Map<String, String> userData = new HashMap<>();
        userData.put(ResponseConstant.EMAIL, email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        String expectedResponseCreateAuth = String.format("Users with email '%s' already exists", email);

        Assertions.assertResponseCode400(responseCreateAuth);
        Assertions.assertResponseTextEquals(responseCreateAuth, expectedResponseCreateAuth);
    }

    @Test
    @DisplayName("Create user successfully")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        Assertions.assertResponseCode200(responseCreateAuth);
        Assertions.assertJsonHasField(responseCreateAuth, ResponseConstant.ID);
    }

    @Test
    @DisplayName("Create user with invalid email - without @")
    public void testCreateUserWithInvalidEmail() {
        Map<String, String> userData = new HashMap<>();
        userData.put(ResponseConstant.EMAIL, Constant.INVALID_EMAIL);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        Assertions.assertResponseCode400(responseCreateAuth);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    @DisplayName("Create user without one of the required fields")
    public void testCreateUserWithoutRequiredField(String missingField) {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.remove(missingField);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        String expectedResponseCreateAuth = String.format("The following required params are missed: %s", missingField);

        Assertions.assertResponseCode400(responseCreateAuth);
        Assertions.assertResponseTextEquals(responseCreateAuth, expectedResponseCreateAuth);
    }

    @Test
    @DisplayName("Create user with short username")
    public void testCreateUserWithShortUsername() {
        String shortUsername = DataGenerator.generateRandomString(1);

        Map<String, String> userData = new HashMap<>();
        userData.put(ResponseConstant.USERNAME, shortUsername);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        Assertions.assertResponseCode400(responseCreateAuth);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @DisplayName("Create user with long username")
    public void testCreateUserWithLongUsername() {
        String longUsername = DataGenerator.generateRandomString(251);

        Map<String, String> userData = new HashMap<>();
        userData.put(ResponseConstant.USERNAME, longUsername);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(URL.API_USER, userData);

        Assertions.assertResponseCode400(responseCreateAuth);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
