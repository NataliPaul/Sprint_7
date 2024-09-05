import Client.OrderList;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderEntireListTest {

    private final ScooterServiceClient client = new ScooterServiceClient();

    private final Integer courierId;
    private final String nearestStation;
    private final Integer limit;
    private final Integer page;

    // Конструктор тестового класса, который принимает параметры
    public OrderEntireListTest(Integer courierId, String nearestStation, Integer limit, Integer page) {
        this.courierId = courierId;
        this.nearestStation = nearestStation;
        this.limit = limit;
        this.page = page;
    }

    // Метод, который предоставляет данные для параметризации
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //{null, null, null, null},  // Все параметры null
                //{1, null, null, null},     // Только courierId
                {null, "1", null, null},   // Только nearestStation
                {null, "2", null, null},   // Только nearestStation
                {null, null, 10, 0},       // Только limit и page
                {null, "110", 10, 0},      // Кроме id курьера
                {422, "2", 10, 0},         // Все параметры указаны
                {9999999, null, null, null},
        });
    }

    @Test
    @Step("Получение списка заказов с параметрами: courierId={0}, nearestStation={1}, limit={2}, page={3}")
    public void getOrdersList_withParams() {
        OrderList orderList = new OrderList(courierId, nearestStation, limit, page);
        ValidatableResponse response = client.listOrder(orderList);

        int statusCode = response.extract().statusCode();

        if (statusCode == 404) {
            handleNotFoundResponse(courierId, response);
        } else if (statusCode == 200) {
            handleSuccessfulResponse(response);
        } else {
            throw new AssertionError("Unexpected status code: " + statusCode);
        }
    }

    @Step("Обработка ответа с кодом 404. Проверка сообщения об ошибке")
    private void handleNotFoundResponse(Integer courierId, ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
    }

    @Step("Обработка успешного ответа. Проверка содержимого списка заказов")
    private void handleSuccessfulResponse(ValidatableResponse response) {
        response.assertThat()
                .body("orders", notNullValue()) // Проверяем, что поле orders существует и не равно null
                .body("orders.size()", greaterThan(0)); // Проверяем, что размер списка заказов больше 0, если заказы есть
    }
}


