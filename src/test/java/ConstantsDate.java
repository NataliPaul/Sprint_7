public class ConstantsDate {

   public static final String VALID_LOGIN = "TestCourierLogin9999";
    public static final String VALID_PASSWORD = "password123";
    public static final String VALID_NAME = "TestNameFirst";

    public static final String VALID_PASSWORD_D = "password1234";
    public static final String VALID_NAME_D = "TestNameSecond";

    public static final String INVALID_LOGIN = "InvalidCourierLogin";
    public static final String INVALID_PASSWORD = "invalidPassword";

    public static final String FIRST_NAME = "Ivan";
    public static final String LAST_NAME = "Ivanov";
    public static final String ADDRESS = "Kirov";
    public static final String METRO_STATION = "4";
    public static final String PHONE = "+7 901 145 23 65";
    public static final int RENT_TIME = 5;
    public static final String DELIVERY_DATE = "2024-09-06";
    public static final String COMMENT = "Saske, come back to Konoha";

    public static final String COLOR_BLACK = "black";
    public static final String COLOR_GREY = "grey";

    public static final int LIMIT = 30;
    public static final int PAGE = 0;

    // Приватный конструктор, чтобы предотвратить создание экземпляров этого класса
    private ConstantsDate() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
