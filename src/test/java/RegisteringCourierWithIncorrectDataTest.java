import Client.Courier;
import Client.Credentials;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class RegisteringCourierWithIncorrectDataTest {


    private static ScooterServiceClient client = new ScooterServiceClient();
    private static Courier courier;
    private static String courierId;

    // Поля для параметров теста
    private final String login;
    private final String password;

    // Конструктор тестового класса, который принимает параметры
    public RegisteringCourierWithIncorrectDataTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ConstantsDate.VALID_LOGIN, ConstantsDate.INVALID_PASSWORD},      // не верный пароль
                {ConstantsDate.INVALID_LOGIN, ConstantsDate.VALID_PASSWORD},      // не верный логин

        });
    }

    @BeforeClass
    @Step("Создание курьера с корректными данными перед тестом")
    public static void setup() {
        courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
        ValidatableResponse createResponse = client.createCourier(courier);
        createResponse.assertThat().statusCode(201).body("ok", is(true));
        // Получение courierId для удаления после теста
        courierId = client.loginCourier(new Credentials(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD))
                          .extract().jsonPath().getString("id");
    }

    //система вернёт ошибку, если неправильно указать логин или пароль;
    @Test
    @Step("Проверка логина курьера с некорректными данными: login={0}, password={1}")
    public void loginCourier_withWrongCredentials_shouldReturnError() {
        // Используем константы для невалидных данных
        Credentials wrongCredentials = new Credentials(login, password);
        ValidatableResponse response = client.loginCourier(wrongCredentials);
        response.assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterClass
    @Step("Удаление созданного курьера после теста")
    public static void tearDown() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                    .statusCode(200)
                    .body("ok", is(true));
        }
    }
}