package su.kartushin.pritunlTelegramBot.pritunl.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
@Builder
@EqualsAndHashCode(callSuper = false)
public class Organization extends haveId {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("auth_api")
    private boolean authApi;

    @JsonProperty("auth_token")
    private String authToken;

    @JsonProperty("auth_secret")
    private String authSecret;

    @JsonProperty("user_count")
    private int userCount;
}