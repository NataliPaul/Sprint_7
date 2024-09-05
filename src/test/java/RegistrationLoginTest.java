import Client.Courier;
import Client.Credentials;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RegistrationLoginTest {

    private final ScooterServiceClient client = new ScooterServiceClient();
    private Courier courier;
    private String courierId;

    @Before
    @Step("Создание курьера перед тестом")
    public void before() {
        courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
        ValidatableResponse createResponse = client.createCourier(courier);
        createResponse.assertThat().statusCode(201).body("ok", is(true));
    }

    // курьер может авторизоваться; успешный запрос возвращает id.
    @Test
    @Step("Авторизация курьера и проверка ответа")
    public void courierLogin_success() {
        ValidatableResponse response = client.loginCourier(Credentials.fromCourier(courier));
        response.assertThat()
                .statusCode(200)  // проверяем статус 200
                .body("id", notNullValue());  // определяем запрос возвращает id
        courierId = response.extract().jsonPath().getString("id");
    }

    @After
    @Step("Удаление курьера после теста")
    public void tearDown() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                .statusCode(200) // Проверяем, что статус код 200 OK
                .body("ok", is(true)); // Проверяем, что тело ответа содержит "ok": true
        }
    }

}
