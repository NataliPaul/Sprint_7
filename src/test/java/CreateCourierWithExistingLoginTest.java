import Client.Courier;
import Client.Credentials;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierWithExistingLoginTest {
    private final ScooterServiceClient client = new ScooterServiceClient();
    private Courier courier;
    private String courierId;

    @Before
    @Step("Удаление созданного курьера после теста")
    public void before() {
        courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
        client.createCourier(courier);
    }

    //нельзя создать курьера с одинаковым логином, но разными паролем;
    @Test
    @Step("Проверка ошибки создания курьера с уже существующим логином")
    public void createDuplicateCourier_shouldReturnError() {

        Courier courierWithSameLogin = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD_D, ConstantsDate.VALID_NAME_D);
        ValidatableResponse response = client.createCourier(courierWithSameLogin);  // Второй запрос с такими же логином, но разными паролем;
        response.assertThat().statusCode(409);  // Код ответа 409 - Conflict (Конфликт данных)
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")); // Сообщение об ошибке

        courierId = client.loginCourier(Credentials.fromCourier(courier)).extract().jsonPath().getString("id");
    }

    @After
    @Step("Удаление созданного курьера после теста с ID: {courierId}")
    public void after() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                .statusCode(200) // Проверяем, что статус код 200 OK
                .body("ok", is(true)); // Проверяем, что тело ответа содержит "ok": true
        }
    }

}
