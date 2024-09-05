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

public class CreateDuplicateCourierTest {

    private ScooterServiceClient client = new ScooterServiceClient();
    private Courier courier;
    private String courierId;

    @Before
    @Step("Создание курьера перед тестом")
    public void before() {
        courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
        client.createCourier(courier);
    }

    //нельзя создать двух одинаковых курьеров;
    @Test
    @Step("Проверка ошибки создания дубликата курьера")
    public void createDuplicateCourier_shouldReturnError() {

        ValidatableResponse response = client.createCourier(courier);  // Второй запрос с такими же данными
        response.assertThat().statusCode(409);  // Код ответа 409 - Conflict (Конфликт данных)
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")); // Сообщение об ошибке

        courierId = client.loginCourier(Credentials.fromCourier(courier)).extract().jsonPath().getString("id");
    }

    @After
    @Step("Удаление курьера")
    public void after() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                .statusCode(200) // Проверяем, что статус код 200 OK
                .body("ok", is(true)); // Проверяем, что тело ответа содержит "ok": true
        }
    }

}
