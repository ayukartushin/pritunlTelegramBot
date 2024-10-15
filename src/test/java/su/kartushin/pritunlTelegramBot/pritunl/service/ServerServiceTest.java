package su.kartushin.pritunlTelegramBot.pritunl.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.kartushin.pritunlTelegramBot.config.Config;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Organization;
import su.kartushin.pritunlTelegramBot.pritunl.objects.Server;
import su.kartushin.pritunlTelegramBot.utils.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class ServerServiceTest {
    private ServerService service;
    private String id;
    private Server server;
    OrganizationService orgService;
    Organization organization;

    @BeforeEach
    void setUp() throws Exception {
        service = new ServerService(Config.getPritunlTestServerURL(),
                Config.getPritunlTestServerLogin(), Config.getPritunlTestServerPassword());
        orgService = new OrganizationService(Config.getPritunlTestServerURL(), service.getAuthData());

        organization = orgService.create(Organization.builder().name("Test").build());

        server = Server.builder()
                .name("Test")
                .network("192.168.4.0/24")
                .port(14701)
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
        server = service.create(server);
        id = server.getId();
        log.trace("id={}", id);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        service.delete(id);
        orgService.delete(organization.getId());
    }

    @Test
    void create() throws IOException {
        Server serverInTest = server;
        serverInTest.setNetwork("192.168.5.0/24");
        serverInTest.setPort(14702);
        try {
            serverInTest = service.create(serverInTest);
        } catch (Exception e) {
            LogUtil.logError(log, e);
        }

        assert serverInTest != null;
        Assertions.assertEquals(serverInTest,
                service.get(serverInTest.getId()).get());

        service.delete(serverInTest.getId());
    }

    @Test
    void delete() throws IOException {
        service.delete(id);
        Optional<Server> optional = service.get(id);
        Assertions.assertFalse(optional.isPresent());
    }

    @Test
    void update() throws Exception {
        Optional<Server> optional = service.get(id);
        Assertions.assertTrue(optional.isPresent());
        Server server = optional.get();

        server.setName("NewName");
        service.update(server);

        Optional<Server> optional1 = service.get(id);
        Assertions.assertTrue(optional1.isPresent());
        Server server1 = optional1.get();

        Assertions.assertEquals(server, server1);
    }

    @Test
    void stopServer() throws Exception {
        service.attachOrganization(server.getId(), organization.getId());

        server = service.get(server.getId()).get();
        service.startServer(server.getId());

        server = service.get(server.getId()).get();
        var status = server.getStatus();
        Assertions.assertEquals("online", status);

        server = service.stopServer(server.getId());
        server = service.get(server.getId()).get();
        Assertions.assertEquals("offline", server.getStatus());
    }

    @Test
    void startServer() throws Exception {
        service.attachOrganization(server.getId(), organization.getId());

        server = service.get(server.getId()).get();
        Thread.sleep(2000);
        server = service.startServer(server.getId());

        var status = server.getStatus();
        Assertions.assertEquals("online", status);
    }

    @Test
    void restartServer() throws Exception {
        service.attachOrganization(server.getId(), organization.getId());

        server = service.get(server.getId()).get();
        service.startServer(server.getId());

        server = service.get(server.getId()).get();
        var status = server.getStatus();
        Assertions.assertEquals("online", status);

        server = service.restartServer(server.getId());
        server = service.get(server.getId()).get();
        Assertions.assertEquals("online", server.getStatus());
    }

    @Test
    void attachOrganization() throws Exception {
        service.attachOrganization(server.getId(), organization.getId());
        server = service.get(server.getId()).get();
    }
}