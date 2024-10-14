package su.kartushin.pritunlTelegramBot.pritunl.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthData {
    private String cookie;
    private String csrf;
}
