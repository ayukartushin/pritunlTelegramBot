package su.kartushin.pritunlTelegramBot.pritunl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.json.JSONObject;
import su.kartushin.pritunlTelegramBot.pritunl.objects.AuthData;
import su.kartushin.pritunlTelegramBot.pritunl.objects.haveId;
import su.kartushin.pritunlTelegramBot.utils.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
public abstract class BaseService<T extends haveId> {
    private final String baseUrl;
    @Getter
    AuthData authData;
    protected String uri;
    Class<T> clazz;
    private final OkHttpClient client;
    protected final ObjectMapper objectMapper;
    protected static final Gson gson = new Gson();

    public BaseService(String baseUrl, String login, String password) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        try {
            this.authData = auth(login, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseService(String baseUrl, AuthData authData) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.authData = authData;
    }

    protected Response postRequest(String bodyContent, String uri) {
        return executeRequest("POST", bodyContent, uri);
    }

    protected Response putRequest(String bodyContent, String uri) {
        return executeRequest("PUT", bodyContent, uri);
    }

    protected Response getRequest(String uri) {
        return executeRequest("GET", null, uri);
    }

    protected Response deleteRequest(String bodyContent, String uri) {
        return executeRequest("DELETE", bodyContent, uri);
    }

    private Response executeRequest(String method, String bodyContent, String uri) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = null;
        if (bodyContent != null)
            body = RequestBody.create(mediaType, bodyContent);

        Request request = new Request.Builder()
                .url(baseUrl + uri)
                .method(method, body)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0")
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .addHeader("Content-Type", "application/json")
                .addHeader("Origin", baseUrl)
                .addHeader("Connection", "keep-alive")
                .addHeader("Referer", baseUrl + "/login")
                .addHeader("Csrf-Token", authData.getCsrf())
                .addHeader("Cookie", authData.getCookie())
                .build();

        log.trace("Запрос {}", request.toString());
        try {
            Response response = client.newCall(request).execute();
            log.trace("Ответ {}", response.peekBody(Long.MAX_VALUE).string());
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response;
        } catch (IOException e) {
            LogUtil.logError(log, e);
            return null; // Или выбросить исключение
        }
    }

    // Метод для авторизации и получения куки и csrf
    private AuthData auth(String login, String password) throws IOException {
        // 1. Отправляем запрос на авторизацию
        RequestBody authBody = RequestBody.create(
                MediaType.parse("application/json"),
                new JSONObject()
                        .put("username", login)
                        .put("password", password)
                        .toString()
        );

        Request authRequest = new Request.Builder()
                .url(baseUrl + "/auth/session")
                .post(authBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0")
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .addHeader("Content-Type", "application/json")
                .addHeader("Origin", baseUrl)
                .addHeader("Referer", baseUrl + "/login")
                .build();

        Response authResponse = null;
        try {
            authResponse = client.newCall(authRequest).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!authResponse.isSuccessful()) {

            throw new IOException("Ошибка при авторизации: " + authResponse);

        }

        // 2. Сохраняем куки из ответа
        String cookies = authResponse.header("Set-Cookie");
        log.trace("Куки сохранены: {}", cookies);

        // 3. Отправляем запрос для получения состояния и CSRF-токена
        Request stateRequest = new Request.Builder()
                .url(baseUrl + "/state")
                .get()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:131.0) Gecko/20100101 Firefox/131.0")
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", baseUrl)
                .addHeader("Cookie", cookies) // Используем сохраненные куки
                .build();

        Response stateResponse = null;
        try {
            stateResponse = client.newCall(stateRequest).execute();
        } catch (IOException e) {
            LogUtil.logError(log, e);
        }
        if (!stateResponse.isSuccessful()) {

            throw new IOException("Ошибка при получении CSRF-токена: " + stateResponse);

        }

        // 4. Извлекаем CSRF-токен из тела ответа
        String responseBody = null;
        try {
            responseBody = stateResponse.body().string();
        } catch (IOException e) {
            LogUtil.logError(log, e);
        }
        JSONObject jsonResponse = new JSONObject(responseBody);
        String csrfToken = jsonResponse.getString("csrf_token");

        // 5. Сохраняем CSRF-токен
        log.trace("CSRF-токен получен: {}", csrfToken);

        return new AuthData(cookies, csrfToken);
    }

    // Метод получения списка сущностей
    public List<T> getAll() throws IOException {
        Response response = getRequest(uri);
        return parseJsonArray(response.body().string());
    }

    // Метод получения сущности по id
    public Optional<T> get(String id) throws IOException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID не должен быть null или пустым");
        }

        return getAll().stream()
                .filter(el -> el.getId().equals(id))
                .findFirst();
    }

    // Метод создания сущности по id
    public T create(T object) throws Exception {
        Response response = null;
        try {
            response = postRequest(gson.toJson(object), uri);
        } catch (Exception e) {
            throw new Exception(e);
        }

        String json = null;
        if (response.body() != null) {
            json = response.body().string();
        }
        T result = null;
        assert json != null;
        if (json.startsWith("[{"))
            result = parseJsonArray(json).getFirst();
        else
            result = parseJson(json);

        return result;
    }

    // Метод удаления сущности по id
    public boolean delete(String id) {
        Response response = deleteRequest("", String.format("%s/%s", uri, id));
        if (response != null)
            return response.isSuccessful();
        else
            return false;
    }

    // Метод обновления сущности по id
    public boolean update(T object) throws Exception {
        Response response = null;
        try {
            response = putRequest(gson.toJson(object), String.format("%s/%s", uri, object.getId()));
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (response != null)
            return response.isSuccessful();
        else
            return false;
    }

    public abstract List<T> parseJsonArray(String jsonArrayString);

    public T parseJson(String jsonObject) {
        try {
            T result = objectMapper.readValue(jsonObject, clazz);
            return result;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке JSON{}: {}", jsonObject, e.getMessage());
            return null;
        }
    }
}
