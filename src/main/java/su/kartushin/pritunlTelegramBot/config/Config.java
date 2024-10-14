package su.kartushin.pritunlTelegramBot.config;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class Config {

    public static String getTelegramBotName(){
        return getValue("TelegramBotName");
    }

    public static String getTelegramBotToken(){
        return getValue("TelegramBotToken");
    }

    public static String getPritunlTestServerURL(){
        return getValue("PritunlTestServerURL");
    }

    public static String getPritunlTestServerLogin(){
        return getValue("PritunlTestServerLogin");
    }

    public static String getPritunlTestServerPassword(){
        return getValue("PritunlTestServerPassword");
    }

    private static String getValue(String variableName){
        // Получение значения переменной
        String value = System.getenv(variableName);

        if (value != null) {
            return value;
        } else {
            log.error("Переменная окружения {} не найдена.", variableName);
            return null;
        }
    }
}
