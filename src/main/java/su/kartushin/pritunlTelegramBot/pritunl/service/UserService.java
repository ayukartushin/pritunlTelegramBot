package su.kartushin.pritunlTelegramBot.pritunl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.Order;
import su.kartushin.pritunlTelegramBot.pritunl.objects.AuthData;
import su.kartushin.pritunlTelegramBot.pritunl.objects.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
public class UserService extends BaseService<User> {

    {
        this.clazz = User.class;
    }

    public UserService(String baseUrl, String login, String password, String organizationId) {
        super(baseUrl, login, password);
        this.uri = String.format("/user/%s",organizationId);
    }

    public UserService(String baseUrl, AuthData authData, String organizationId) {
        super(baseUrl, authData);
        this.uri = String.format("/user/%s",organizationId);
    }

    @Override
    public List parseJsonArray(String jsonArrayString) {
        try {
            return objectMapper.readValue(jsonArrayString, new TypeReference<List<User>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке JSON{}: {}", jsonArrayString, e.getMessage());
            return Collections.emptyList();
        }
    }
}
