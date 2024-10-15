package su.kartushin.pritunlTelegramBot.pritunl.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.kartushin.pritunlTelegramBot.config.Config;
import su.kartushin.pritunlTelegramBot.pritunl.objects.AuthData;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Organization;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Server;
import su.kartushin.pritunlTelegramBot.pritunl.objects.User;
import su.kartushin.pritunlTelegramBot.utils.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class UserServiceTest {
    private UserService service;
    private ServerService serService;
    private OrganizationService orgService;

    private User user;
    private Server server;
    private Organization organization;

    private AuthData authData;

    @BeforeEach
    void setUp() throws Exception {
        serService = new ServerService(Config.getPritunlTestServerURL(),
                Config.getPritunlTestServerLogin(), Config.getPritunlTestServerPassword());
        authData = serService.getAuthData();
        orgService = new OrganizationService(Config.getPritunlTestServerURL(), authData);

        organization = orgService.create(Organization.builder().name("Test").build());

        server = Server.builder()
                .name("Test")
                .network("192.168.8.0/24")
                .port(14704)
                .protocol("udp")
                .dhParamBits(2048)
                .ipv6Firewall(true)
                .dnsServers(List.of("8.8.8.8"))
                .cipher("aes128")
                .hash("sha1")
                .interClient(true)
                .restrictRoutes(true)
                .vxlan(true)
                .networkWg("")
                .groups(List.of())
                .dynamicFirewall(false)
                .routeDns(false)
                .deviceAuth(false)
                .ipv6(false)
                .networkMode("tunnel")
                .networkStart("")
                .networkEnd("")
                .wg(false)
                .multiDevice(false)
                .otpAuth(false)
                .ssoAuth(false)
                .blockOutsideDns(false)
                .replicaCount(1)
                .dnsMapping(false)
                .debug(false)
                .multihome(false)
                .build();
        server = serService.create(server);
        serService.attachOrganization(server.getId(), organization.getId());
        serService.startServer(server.getId());
        service = new UserService(Config.getPritunlTestServerURL(), authData, organization.getId());

        user = User.builder()
                .organization(organization.getId())
                .name("test")
                .authType("local")
                .build();

        user = service.create(user);
        Thread.sleep(1000);
        user = service.get(user.getId()).get();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        service.delete(user.getId());
        serService.delete(server.getId());
        orgService.delete(organization.getId());
    }

    @Test
    void create() throws Exception {
        User user1 = User.builder()
                .organization(organization.getId())
                .name("test2")
                .authType("local")
                .build();

        user1 = service.create(user1);

        User actual = service.get(user1.getId()).get();

        Assertions.assertEquals(user1, actual);
        service.delete(user1.getId());
    }

    @Test
    void delete() throws IOException {
        service.delete(user.getId());
        Optional<User> optional = service.get(user.getId());
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    void update() throws Exception {
        user.setName("NewName");
        service.update(user);

        Optional<User> optional1 = service.get(user.getId());
        Assertions.assertTrue(optional1.isPresent());
        User user1 = optional1.get();

        Assertions.assertEquals(user, user1);
    }
}
