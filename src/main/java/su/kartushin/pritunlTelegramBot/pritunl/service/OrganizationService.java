package su.kartushin.pritunlTelegramBot.pritunl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;
import su.kartushin.pritunlTelegramBot.pritunl.objects.AuthData;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Organization;

import java.util.Collections;
import java.util.List;

@Log4j2
public class OrganizationService extends BaseService<Organization> {

    {
        this.uri = "/organization";
        this.clazz = Organization.class;
    }

    public OrganizationService(String baseUrl, String login, String password) {
        super(baseUrl, login, password);
    }

    public OrganizationService(String baseUrl, AuthData authData) {
        super(baseUrl, authData);
    }

    @Override
    public List<Organization> parseJsonArray(String jsonArrayString) {
        try {
            return objectMapper.readValue(jsonArrayString, new TypeReference<List<Organization>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке JSON{}: {}", jsonArrayString, e.getMessage());
            return Collections.emptyList();
        }
    }
}
