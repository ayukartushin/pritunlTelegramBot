package su.kartushin.pritunlTelegramBot.pritunl.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize
@JsonDeserialize
@EqualsAndHashCode(callSuper = false)
public class Server extends haveId {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("uptime")
    private Long uptime;

    @JsonProperty("users_online")
    private Integer usersOnline;

    @JsonProperty("devices_online")
    private Integer devicesOnline;

    @JsonProperty("user_count")
    private Integer userCount;

    @JsonProperty("network")
    private String network;

    @JsonProperty("network_wg")
    private String networkWg;

    @JsonProperty("bind_address")
    private String bindAddress;

    @JsonProperty("port")
    private Integer port;

    @JsonProperty("port_wg")
    private Integer portWg;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("dh_param_bits")
    private Integer dhParamBits;

    @JsonProperty("groups")
    private List<String> groups;

    @JsonProperty("wg")
    private Boolean wg;

    @JsonProperty("ipv6")
    private Boolean ipv6;

    @JsonProperty("ipv6_firewall")
    private Boolean ipv6Firewall;

    @JsonProperty("network_mode")
    private String networkMode;

    @JsonProperty("network_start")
    private String networkStart;

    @JsonProperty("network_end")
    private String networkEnd;

    @JsonProperty("dynamic_firewall")
    private Boolean dynamicFirewall;

    @JsonProperty("route_dns")
    private Boolean routeDns;

    @JsonProperty("device_auth")
    private Boolean deviceAuth;

    @JsonProperty("restrict_routes")
    private Boolean restrictRoutes;

    @JsonProperty("multi_device")
    private Boolean multiDevice;

    @JsonProperty("dns_servers")
    private List<String> dnsServers;

    @JsonProperty("search_domain")
    private String searchDomain;

    @JsonProperty("otp_auth")
    private Boolean otpAuth;

    @JsonProperty("sso_auth")
    private Boolean ssoAuth;

    @JsonProperty("cipher")
    private String cipher;

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("block_outside_dns")
    private Boolean blockOutsideDns;

    @JsonProperty("jumbo_frames")
    private Boolean jumboFrames;

    @JsonProperty("lzo_compression")
    private Boolean lzoCompression;

    @JsonProperty("inter_client")
    private Boolean interClient;

    @JsonProperty("ping_interval")
    private Integer pingInterval;

    @JsonProperty("ping_timeout")
    private Integer pingTimeout;

    @JsonProperty("ping_timeout_wg")
    private Integer pingTimeoutWg;

    @JsonProperty("link_ping_interval")
    private Integer linkPingInterval;

    @JsonProperty("link_ping_timeout")
    private Integer linkPingTimeout;

    @JsonProperty("inactive_timeout")
    private Integer inactiveTimeout;

    @JsonProperty("session_timeout")
    private Integer sessionTimeout;

    @JsonProperty("allowed_devices")
    private Integer allowedDevices;

    @JsonProperty("max_clients")
    private Integer maxClients;

    @JsonProperty("max_devices")
    private Integer maxDevices;

    @JsonProperty("replica_count")
    private Integer replicaCount;

    @JsonProperty("vxlan")
    private Boolean vxlan;

    @JsonProperty("dns_mapping")
    private Boolean dnsMapping;

    @JsonProperty("debug")
    private Boolean debug;

    @JsonProperty("pre_connect_msg")
    private String preConnectMsg;

    @JsonProperty("mss_fix")
    private String mssFix;

    @JsonProperty("multihome")
    private Boolean multihome;

    @JsonProperty("operation")
    private String operation;
}
