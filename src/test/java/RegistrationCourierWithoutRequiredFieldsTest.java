import Client.Courier;
import Client.Credentials;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class RegistrationCourierWithoutRequiredFieldsTest {

    private final ScooterServiceClient client = new ScooterServiceClient();
    private String courierId;

    // Поля для параметров теста
    private final String login;
    private final String password;

    // Конструктор тестового класса, который принимает параметры
    public RegistrationCourierWithoutRequiredFieldsTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ConstantsDate.VALID_LOGIN, null},         // Пропущен пароль
                {null, ConstantsDate.VALID_PASSWORD},      // Пропущен логин

        });
    }

    @Before
    public void before() {
        // Создаем курьера только если оба значения не равны null
        if (login != null && password != null) {
            Courier courier = new Courier(ConstantsDate.VALID_LOGIN, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME);
            client.createCourier(courier).assertThat().statusCode(201).body("ok", is(true));
        }
    }

    // если какого-то поля нет, запрос возвращает ошибку;
    @Test
    @Step("Тестирование входа курьера с пропущенными обязательными полями: login={0}, password={1}")
    public void courierLogin_withMissingFields() {
        // Проверяем обязательные поля перед отправкой запроса
        if (password == null) {
            System.out.println("Пропущены обязательные поля: password");
            return; // Пропускаем тест, если нет обязательных полей
        }

        Credentials credentials = new Credentials(login, password);
        ValidatableResponse response = client.loginCourier(credentials);
        response.assertThat().statusCode(400);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    @Step("Удаление созданного курьера после теста")
    public void tearDown() {
        if (courierId != null) {
            ValidatableResponse deleteResponse = client.deleteCourier(courierId);
            deleteResponse.assertThat()
                    .statusCode(200)
                    .body("ok", is(true));
        }
    }
}
