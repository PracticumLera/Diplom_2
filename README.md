# 🍔 Stellar Burgers — API-автотесты

![Java](https://img.shields.io/badge/Java-11-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![REST Assured](https://img.shields.io/badge/REST%20Assured-5.5.5-4B8BBE?style=flat-square)
![JUnit](https://img.shields.io/badge/JUnit-4.13.2-25A162?style=flat-square&logo=junit5&logoColor=white)
![Allure](https://img.shields.io/badge/Allure-2.27.0-FF6A00?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![Tests](https://img.shields.io/badge/tests-11%20passed%2C%202%20failed-orange?style=flat-square)

Проект автоматизации тестирования **REST API** приложения
[Stellar Burgers](https://qa-stellarburgers.education-services.ru/) на Java 11
с использованием **REST Assured**, **JUnit 4** и **Allure**.

13 тестов покрывают три домена API — пользователи, авторизация и заказы.
11 тестов проходят; 2 падают **намеренно**: они фиксируют дефект бэкенда
(500 вместо 400 на невалидный ингредиент), подробности — в разделе
[«Известные дефекты API»](#-известные-дефекты-api).

---

## 📑 Содержание

- [Стек технологий](#-стек-технологий)
- [Структура проекта](#-структура-проекта)
- [Тесты (13 штук)](#-тесты-13-штук)
- [Известные дефекты API](#-известные-дефекты-api)
- [Быстрый старт](#-быстрый-старт)
- [Запуск тестов](#-запуск-тестов)
- [Allure-отчёты](#-allure-отчёты)
- [Скриншоты](#-скриншоты)
- [Особенности реализации](#-особенности-реализации)
- [Основные компоненты](#-основные-компоненты)
- [Пример теста](#-пример-теста)
- [Конфигурация](#-конфигурация)

---

## 🛠 Стек технологий

| Технология | Версия | Назначение |
|---|---|---|
| ☕ **Java** | 11 | Язык программирования |
| 🧪 **REST Assured** | 5.5.5 | Тестирование REST API |
| ✅ **JUnit 4** | 4.13.2 | Фреймворк для тестирования |
| 📊 **Allure** | 2.27.0 | Отчётность |
| 📦 **Jackson** | 2.17.1 | Работа с JSON |
| 🧩 **Lombok** | 1.18.34 | Генерация кода (модели) |
| 🏗 **Maven** | 3.6+ | Сборка проекта и запуск тестов |

---

## 📂 Структура проекта

```text
src/test/java/
├── base/
│   └── BaseApiTest.java                — базовый класс для API-тестов
├── constants/
│   ├── Config.java                     — URL-адреса эндпоинтов
│   └── TestConstants.java              — константы (статусы, тексты ошибок, ключи JSON)
├── models/
│   ├── Order.java                      — модель заказа
│   └── User.java                       — модель пользователя
├── steps/
│   ├── IngredientSteps.java            — шаги для ингредиентов
│   ├── OrderSteps.java                 — шаги для заказов
│   └── UserSteps.java                  — шаги для пользователей
└── tests/
    ├── OrderCreationTest.java          — 5 тестов создания заказа
    ├── UserCreationTest.java           — 5 тестов создания пользователя
    └── UserLoginTest.java              — 3 теста логина
```

---

## ✅ Тесты (13 штук)

**Результат прогона: 11 passed, 2 failed — 84.61%.**
Оба падения — дефекты сервера, а не автотестов: проверки написаны на корректный
`400 Bad Request` и останутся в наборе, пока баг не будет исправлен.

### Создание пользователя — `UserCreationTest` (5 тестов)

| # | Тест | Эндпоинт | Ожидаемый ответ |
|---|---|---|---|
| 1 | ✅ Создание уникального пользователя | `POST /api/auth/register` | `200`, `success: true`, `accessToken` и `refreshToken` не пустые |
| 2 | ✅ Создание уже существующего пользователя | `POST /api/auth/register` | `403`, `"User already exists"` |
| 3 | ✅ Создание без обязательного поля `email` | `POST /api/auth/register` | `403`, `"Email, password and name are required fields"` |
| 4 | ✅ Создание без обязательного поля `password` | `POST /api/auth/register` | `403`, `"Email, password and name are required fields"` |
| 5 | ✅ Создание без обязательного поля `name` | `POST /api/auth/register` | `403`, `"Email, password and name are required fields"` |

### Логин пользователя — `UserLoginTest` (3 теста)

| # | Тест | Эндпоинт | Ожидаемый ответ |
|---|---|---|---|
| 6 | ✅ Вход под существующим пользователем | `POST /api/auth/login` | `200`, `success: true`, токены не пустые |
| 7 | ✅ Вход с неверным паролем | `POST /api/auth/login` | `401`, `"email or password are incorrect"` |
| 8 | ✅ Вход с неверным email | `POST /api/auth/login` | `401`, `"email or password are incorrect"` |

### Создание заказа — `OrderCreationTest` (5 тестов)

| # | Тест | Эндпоинт | Ожидаемый ответ |
|---|---|---|---|
| 9 | ✅ Создание заказа с авторизацией и ингредиентами | `POST /api/orders` | `200`, `success: true`, `order.number` не пустой |
| 10 | ✅ Создание заказа без авторизации | `POST /api/orders` | `200`, `success: true` — эндпоинт не требует токена |
| 11 | ✅ Создание заказа без ингредиентов | `POST /api/orders` | `400`, `"Ingredient ids must be provided"` |
| 12 | ❌ Создание заказа с неверным хешем ингредиента | `POST /api/orders` | ожидается `400`, приходит `500` — [баг API](#-известные-дефекты-api) |
| 13 | ❌ Создание заказа с несуществующим ID ингредиента | `POST /api/orders` | ожидается `400`, приходит `500` — [баг API](#-известные-дефекты-api) |

---

## 🐞 Известные дефекты API

### `POST /api/orders` — `500` вместо `400` на невалидный ингредиент

При передаче невалидного или несуществующего `_id` ингредиента сервер возвращает
`500 Internal Server Error` с HTML-телом вместо ожидаемого `400 Bad Request`
с JSON-описанием ошибки.

| Параметр | Значение |
|---|---|
| **Эндпоинт** | `POST /api/orders` |
| **Заголовки** | `Authorization: Bearer <token>`, `Content-Type: application/json` |
| **Тело запроса** | `{ "ingredients": ["invalid_hash_123"] }` |
| **Ожидается** | `400 Bad Request`, `{"success": false, "message": "..."}` |
| **Фактически** | `500 Internal Server Error`, тело — HTML `<pre>Internal Server Error</pre>` |

**Воспроизведение:**

```http
POST /api/orders HTTP/1.1
Host: qa-stellarburgers.education-services.ru
Authorization: Bearer <accessToken>
Content-Type: application/json

{ "ingredients": ["invalid_hash_123"] }
```

**Как это отражено в тестах.** Методы `createOrderWithInvalidHashTest` и
`createOrderWithNonExistentIdTest` проверяют именно корректное поведение —
`statusCode(400)`:

```java
@Test
@DisplayName("Создание заказа с неверным хешем ингредиентов (ожидаем 400, придёт 500)")
@Description("По заданию тест должен падать — сервер возвращает 500 вместо 400")
public void createOrderWithInvalidHashTest() {
    Order order = Order.builder()
            .ingredients(new String[]{Ingredient.INVALID_HASH})
            .build();

    Response response = orderSteps.createOrderWithAuth(order, accessToken);

    response.then()
            .statusCode(HttpStatus.BAD_REQUEST);
}
```

Пока дефект не исправлен, эти два теста падают — это ожидаемый результат прогона
(11 passed, 2 failed). После фикса бэкенда они станут зелёными **без изменения кода
тестов**.

---

## 🚀 Быстрый старт

### Требования

- **Java 11+** — [скачать](https://adoptium.net/)
- **Maven 3.6+** — [скачать](https://maven.apache.org/download.cgi)

Проверить окружение:

```bash
java -version
mvn -version
```

Тестовый стенд: <https://qa-stellarburgers.education-services.ru>

> 💡 Браузер и WebDriver не нужны: тесты работают только с HTTP API.

---

## 🏃 Запуск тестов

| Команда | Что делает |
|---|---|
| `mvn clean test` | Все 13 тестов |
| `mvn clean test -Dtest=UserCreationTest` | Только создание пользователя (5) |
| `mvn clean test -Dtest=UserLoginTest` | Только логин (3) |
| `mvn clean test -Dtest=OrderCreationTest` | Только заказы (5) |

### Примеры

```bash
# Все тесты
mvn clean test

# Конкретный тест-класс
mvn clean test -Dtest=UserCreationTest
mvn clean test -Dtest=UserLoginTest
mvn clean test -Dtest=OrderCreationTest

# Один тест-метод
mvn clean test -Dtest=UserCreationTest#createUniqueUserTest
```

---

## 📊 Allure-отчёты

### Генерация отчёта

```bash
mvn allure:report
```

### Просмотр отчёта в браузере

```bash
mvn allure:serve
```

После выполнения команды отчёт автоматически откроется в браузере.

### Что внутри отчёта

- **Overview** — статистика выполнения тестов
- **Suites** — разбиение тестов по классам
- **Graphs** — графики результатов
- **Timeline** — временная шкала выполнения
- **Behaviors** — тесты, сгруппированные по Epic/Feature

В отчёт также попадают полные запросы и ответы каждого вызова — благодаря
`AllureRestAssured` в `BaseApiTest`.

---

## 📸 Скриншоты

![Allure Overview](screenshots/allure-overview.png)

*Overview: 13 тестов, 84.61% успеха — 2 падения из-за бага API*

![Allure Suites](screenshots/allure-suites.png)

*Suites: детализация по классам, в `OrderCreationTest` видны упавшие тесты*

---

## 💡 Особенности реализации

- ✅ **Все API-запросы через Steps** — `UserSteps`, `OrderSteps`, `IngredientSteps` инкапсулируют вызовы REST Assured
- ✅ **Тестовые данные создаются и удаляются** — в `@Before` создаётся пользователь, в `@After` удаляется
- ✅ **Проверка тела и кода ответа** — каждый тест проверяет `statusCode` + `body` через Hamcrest-матчеры
- ✅ **Allure-интеграция** — `@Epic`, `@Feature`, `@DisplayName`, `@Description`
- ✅ **Уникальные данные** — email и имя генерируются через `UUID`
- ✅ **Jackson + Lombok** — модели сериализуются и десериализуются автоматически
- ✅ **Логирование запросов и ответов** — `RequestLoggingFilter` и `ResponseLoggingFilter` в `BaseApiTest`

---

## 🧩 Основные компоненты

### `BaseApiTest`

Базовый класс для всех API-тестов. Автоматически выполняет:

1. Устанавливает `RestAssured.baseURI` из `Config`
2. Подключает фильтры логирования (`RequestLoggingFilter`, `ResponseLoggingFilter`)
3. Подключает `AllureRestAssured` для генерации отчёта

### `UserSteps`

Класс-шаг для работы с пользователями:

| Метод | Назначение |
|---|---|
| `createUser(User user)` | Регистрация пользователя |
| `loginUser(User user)` | Авторизация |
| `deleteUser(String accessToken)` | Удаление пользователя |

### `OrderSteps`

Класс-шаг для работы с заказами:

| Метод | Назначение |
|---|---|
| `createOrderWithAuth(Order order, String accessToken)` | Создание заказа с токеном |
| `createOrderWithoutAuth(Order order)` | Создание заказа без токена |

### `IngredientSteps`

Класс-шаг для работы с ингредиентами:

| Метод | Назначение |
|---|---|
| `getIngredients()` | Получение списка ингредиентов |
| `getFirstIngredientsIds(int count)` | Получение первых N ID ингредиентов |

### `User`

Модель пользователя с Lombok:

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
}
```

### `TestConstants`

Константы, избавляющие тесты от «магических» значений:

| Вложенный класс | Что содержит |
|---|---|
| `HttpStatus` | `OK = 200`, `BAD_REQUEST = 400`, `UNAUTHORIZED = 401`, `FORBIDDEN = 403`, `INTERNAL_SERVER_ERROR = 500` |
| `Error` | Тексты ошибок API (`"User already exists"`, `"Ingredient ids must be provided"` и т.д.) |
| `JsonKeys` | Ключи JSON (`success`, `user`, `accessToken`, `order.number` и т.д.) |
| `User`, `Ingredient` | Тестовые данные: префикс email, пароль по умолчанию, невалидные ID ингредиентов |

---

## 📝 Пример теста

```java
@Epic("Stellar Burgers API")
@Feature("Создание пользователя")
public class UserCreationTest extends BaseApiTest {

    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = createTestUser();
        accessToken = null;
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
            accessToken = null;
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового пользователя")
    public void createUniqueUserTest() {
        Response response = userSteps.createUser(user);
        accessToken = response.then().extract().path(JsonKeys.ACCESS_TOKEN);

        response.then()
                .statusCode(HttpStatus.OK)
                .body(JsonKeys.SUCCESS, is(true))
                .body(JsonKeys.USER + "." + JsonKeys.EMAIL, equalTo(user.getEmail()))
                .body(JsonKeys.USER + "." + JsonKeys.NAME, equalTo(user.getName()))
                .body(JsonKeys.ACCESS_TOKEN, notNullValue())
                .body(JsonKeys.REFRESH_TOKEN, notNullValue());
    }
}
```

---

## 🔧 Конфигурация

Файл `Config.java` содержит адреса эндпоинтов:

```java
public class Config {

    public static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";
    private static final String API_PREFIX = BASE_URL + "/api";

    public static final String REGISTER_URL = API_PREFIX + "/auth/register";
    public static final String LOGIN_URL = API_PREFIX + "/auth/login";
    public static final String USER_URL = API_PREFIX + "/auth/user";
    public static final String ORDER_URL = API_PREFIX + "/orders";
    public static final String INGREDIENT_URL = API_PREFIX + "/ingredients";

    public static final String AUTH_HEADER = "Authorization";
}
```

| Эндпоинт | Назначение |
|---|---|
| `POST /api/auth/register` | Регистрация пользователя |
| `POST /api/auth/login` | Авторизация |
| `DELETE /api/auth/user` | Удаление пользователя |
| `POST /api/orders` | Создание заказа |
| `GET /api/ingredients` | Получение списка ингредиентов |

---

<div align="center">

**Проект создан как финальный диплом Яндекс.Практикума по QA-автоматизации.**

Java 11 · REST Assured · JUnit 4 · Allure

</div>
