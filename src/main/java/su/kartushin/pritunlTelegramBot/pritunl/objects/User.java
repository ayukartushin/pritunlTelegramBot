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
public class User extends haveId {

    @JsonProperty("id")
    private String id;

    @JsonProperty("organization")
    private String organization;

    @JsonProperty("organization_name")
    private String organizationName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("groups")
    private List<String> groups;

    @JsonProperty("last_active")
    private Long lastActive;

    @JsonProperty("pin")
    private Boolean pin;

    @JsonProperty("type")
    private String type;

    @JsonProperty("auth_type")
    private String authType;

    @JsonProperty("yubico_id")
    private String yubicoId;

    @JsonProperty("otp_secret")
    private String otpSecret;

    @JsonProperty("disabled")
    private Boolean disabled;

    @JsonProperty("bypass_secondary")
    private Boolean bypassSecondary;

    @JsonProperty("client_to_client")
    private Boolean clientToClient;

    @JsonProperty("mac_addresses")
    private List<String> macAddresses;

    @JsonProperty("dns_servers")
    private List<String> dnsServers;

    @JsonProperty("dns_suffix")
    private String dnsSuffix;

    @JsonProperty("port_forwarding")
    private List<String> portForwarding;

    @JsonProperty("devices")
    private List<String> devices;

    @JsonProperty("gravatar")
    private Boolean gravatar;

    @JsonProperty("audit")
    private Boolean audit;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("sso")
    private String sso;

    @JsonProperty("auth_modes")
    private List<String> authModes;

    @JsonProperty("dns_mapping")
    private List<String> dnsMapping;

    @JsonProperty("network_links")
    private List<String> networkLinks;

    @JsonProperty("servers")
    private List<Server> servers;
}
