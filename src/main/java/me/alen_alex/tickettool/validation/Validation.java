package me.alen_alex.tickettool.validation;

import me.alen_alex.tickettool.configuration.Configuration;
import org.apache.commons.lang3.Validate;

public class Validation {

    public static void validateStartupParameters(){
        Validate.notNull(Configuration.getBotToken(),"The token for the bot seems to empty!");
    }

}
