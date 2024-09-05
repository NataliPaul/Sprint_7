package Client;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ScooterServiceClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log()
                .all();

    }

    public ValidatableResponse loginCourier(Credentials credentials) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log()
                .all();
    }

    public ValidatableResponse deleteCourier(String courierId) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + courierId)
                .then()
                .log()
                .all();
    }

     public ValidatableResponse createOrder (Orders orders) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .post("/api/v1/orders")
                .then()
                .log()
                .all();
    }

    public ValidatableResponse listOrder (OrderList orderList) {
        return given()
                .log()
                .all()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .queryParam("courierId", orderList.getCourierId() != null ? orderList.getCourierId() : "")
                .queryParam("nearestStation", orderList.getNearestStation() != null ? orderList.getNearestStation() : "")
                .queryParam("limit", orderList.getLimit() != null ? orderList.getLimit() : "")
                .queryParam("page", orderList.getPage() != null ? orderList.getPage() : "")
                .get("/api/v1/orders")
                .then()
                .log()
                .all();
    }
}
