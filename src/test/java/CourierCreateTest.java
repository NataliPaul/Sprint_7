import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import Client.Courier;
import Client.Credentials;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CourierCreateTest {

    private final ScooterServiceClient client = new ScooterServiceClient();
    private String courierId;
    private Courier courier;

    @Before
    @Step("Подготовка данных перед тестом: создание объекта курьера")
    public void before() {
        courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
    }

    //Проверка создания курьера и что запрос успешный (возвращает ok: true)
    @Test
    @Step("Проверка создания курьера")
    public void createUser_withClient_success() {
        ValidatableResponse response = client.createCourier(courier);

        response.assertThat()
                .statusCode(201) //курьера можно создать;
                .body("ok", is(true));  //успешный запрос возвращает ok: true;

        //Сохраняем ID созданного курьера для последующего удаления
        courierId = client.loginCourier(Credentials.fromCourier(courier)).extract().jsonPath().getString("id");
    }

    @After
    @Step("Удаление курьера после теста по ID: {courierId}")
    public void after() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                .statusCode(200) // Проверяем, что статус код 200 OK
                .body("ok", is(true)); // Проверяем, что тело ответа содержит "ok": true
        }
    }
}
