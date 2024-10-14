package su.kartushin.pritunlTelegramBot.pritunl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import su.kartushin.pritunlTelegramBot.pritunl.objects.AuthData;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Server;

import java.util.Collections;
import java.util.List;

@Log4j2
public class ServerService extends BaseService<Server>{

    {
        this.uri = "/server";
        this.clazz = Server.class;
    }

    public ServerService(String baseUrl, String login, String password) {
        super(baseUrl, login, password);
    }

    public ServerService(String baseUrl, AuthData authData) {
        super(baseUrl, authData);
    }

    @Override
    public List<Server> parseJsonArray(String jsonArrayString) {
        try {
            return objectMapper.readValue(jsonArrayString, new TypeReference<List<Server>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке JSON{}: {}", jsonArrayString, e.getMessage());
            return Collections.emptyList();
        }
    }


    public Server stopServer(String id) throws Exception {
        var optional = get(id);
        if (!optional.isPresent())
            throw new Exception("Остановка сервера. Не найден сервер с id=" + id);
        Server server = optional.get();
        server.setOperation("stop");

        Response response = putRequest(gson.toJson(server), "/server/" + id + "/operation/stop");
        String json = null;
        if (response.body() != null) {
            json = response.body().string();
        }
        var result = parseJson(json);

        return result;
    }

    public Server startServer(String id) throws Exception {
        var optional = get(id);
        if (!optional.isPresent())
            throw new Exception("Остановка сервера. Не найден сервер с id=" + id);
        Server server = optional.get();
        server.setOperation("start");

        Response response = putRequest(gson.toJson(server), "/server/" + id + "/operation/start");
        String json = null;
        if (response.body() != null) {
            json = response.body().string();
        }
        var result = parseJson(json);

        return result;
    }

    public Server restartServer(String id) throws Exception {
        var optional = get(id);
        if (!optional.isPresent())
            throw new Exception("Остановка сервера. Не найден сервер с id=" + id);
        Server server = optional.get();
        server.setOperation("restart");
        Response response = putRequest(gson.toJson(server), "/server/" + id + "/operation/restart");
        String json = null;
        if (response.body() != null) {
            json = response.body().string();
        }
        var result = parseJson(json);

        return result;
    }

    public String statusServer(String id) throws Exception {
        var optional = get(id);
        if (!optional.isPresent())
            throw new Exception("Остановка сервера. Не найден сервер с id=" + id);
        Server server = optional.get();
        return server.getStatus();
    }

    public void attachOrganization(String serverId, String organizationId){
        putRequest(
                String.format("{" +
                        "\"id\":\"%s\"," +
                        "\"server\":\"%s\"," +
                        "\"name\":null" +
                        "}", organizationId, serverId),
                String.format("/server/%s/organization/%s", serverId, organizationId));
    }
}
