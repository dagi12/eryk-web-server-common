package pl.edu.amu.wmi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import pl.edu.amu.wmi.model.GeneralResponse;
import pl.edu.amu.wmi.model.MyRuntimeException;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by erykmariankowski on 08.10.2018.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageSourceAccessor messageSourceAccessor;

    public MessageService(MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    public String translate(String key) {
        if (key == null) {
            return null;
        }
        try {
            return messageSourceAccessor.getMessage(key);
        } catch (NoSuchMessageException e) {
            LOGGER.warn("No value for key: {}, locale: {}", key, LocaleContextHolder.getLocale(), e);
        }
        return key;
    }

    public <T> void translate(GeneralResponse<T> response) {
        String translate = translate(response.getErrorMessage());
        response.setErrorMessage(translate);
    }

    /**
     * This method extracts error code string from sql messge and translates it
     *
     * @param key sql error code
     * @return translated code
     */
    public String getSqlString(String key) {
        if (key == null) {
            return null;
        }
        if (isEmpty(key)) {
            throw new MyRuntimeException("Key cannot be empty");
        }
        String[] splitted = key.split("([:\n])");
        String tokens = splitted[1];
        if (key.contains("|")) {
            String[] i18nToken = tokens.split("\\|");
            return translate(i18nToken[0].trim()) + ": " + i18nToken[1].trim();
        }
        return translate(tokens.trim());
    }

}
