import Client.Orders;
import Client.ScooterServiceClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatingOrderChoosingColorTest {
    private static ScooterServiceClient client;
    private Orders orders;

    // Параметр для цветов
    private final String[] colors;

    public CreatingOrderChoosingColorTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{ConstantsDate.COLOR_BLACK}},  // Один цвет - BLACK
                {new String[]{ConstantsDate.COLOR_GREY}},   // Один цвет - GREY
                {new String[]{ConstantsDate.COLOR_BLACK, ConstantsDate.COLOR_GREY}},  // Оба цвета
                {new String[]{}},  // Без указания цвета
        });
    }

    @BeforeClass
    @Step("Инициализация клиента для ScooterService")
    public static void setUp() {
        client = new ScooterServiceClient();
    }

    @Test
    @Step("Проверка успешного создания заказа")
    public void createOrder_withSpecificDetails() {
       orders = new Orders(ConstantsDate.FIRST_NAME, ConstantsDate.LAST_NAME, ConstantsDate.ADDRESS,
               ConstantsDate.METRO_STATION, ConstantsDate.PHONE, ConstantsDate.RENT_TIME,
               ConstantsDate.DELIVERY_DATE, ConstantsDate.COMMENT, List.of(colors));

       ValidatableResponse response = client.createOrder(orders);
        response.assertThat()
                .statusCode(201)  // Проверяем, что заказ успешно создан
                .body("track", is(notNullValue()));  // Проверяем, что ответ содержит поле "track"
    }

}
