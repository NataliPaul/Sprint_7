import Client.Courier;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateCourierWithoutRequiredFieldsTest {

    private final ScooterServiceClient client = new ScooterServiceClient();

    // Поля для параметров теста
    private final String login;
    private final String password;
    private final String firstName;

    // Конструктор тестового класса, который принимает параметры
    public CreateCourierWithoutRequiredFieldsTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, ConstantsDate.VALID_PASSWORD, ConstantsDate.VALID_NAME},      // Пропущен логин
                {ConstantsDate.VALID_LOGIN, null, ConstantsDate.VALID_NAME},         // Пропущен пароль

        });
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    @Test
    @Step("Проверка ошибки на отсутствие обязательных полей")
    public void createDuplicateCourier_shouldReturnError() {

        Courier courier = new Courier(login, password, firstName);
        ValidatableResponse response = client.createCourier(courier);  // Второй запрос с такими же данными
        response.assertThat().statusCode(400);  // Код ответа 400 - если не все обязательные поля переданы
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")); // Сообщение об ошибке
    }
}

