package su.kartushin.pritunlTelegramBot.pritunl.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import su.kartushin.pritunlTelegramBot.config.Config;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Organization;
import su.kartushin.pritunlTelegramBot.utils.LogUtil;

import java.io.IOException;
import java.util.Optional;

@Log4j2
class OrganizationServiceTest {
    OrganizationService service;
    String id;

    @BeforeEach
    void setUp() throws Exception {
        service = new OrganizationService(Config.getPritunlTestServerURL(),
                Config.getPritunlTestServerLogin(), Config.getPritunlTestServerPassword());
        id = service.create(Organization.builder().name("Test").build()).getId();
    }

    @AfterEach
    void tearDown() {
        service.delete(id);
    }

    @Test
    public void create() throws Exception {
        Organization organization = null;
        try {
            organization = service.create(Organization.builder().name("Test").build());
        } catch (Exception e) {
            LogUtil.logError(log, e);
        }

        Assertions.assertEquals(organization,
                service.get(organization.getId()).get());

        service.delete(organization.getId());
    }

    @Test
    public void update() throws Exception {
        Optional<Organization> optional = service.get(id);
        Assertions.assertTrue(optional.isPresent());
        Organization organization = optional.get();

        organization.setName("NewName");
        service.update(organization);

        Optional<Organization> optional1 = service.get(id);
        Assertions.assertTrue(optional1.isPresent());
        Organization organization1 = optional1.get();

        Assertions.assertEquals(organization, organization1);
    }

    @Test
    public void delete() throws Exception {
        service.delete(id);
        Optional<Organization> optional = service.get(id);
        Assertions.assertFalse(optional.isPresent());
    }
}