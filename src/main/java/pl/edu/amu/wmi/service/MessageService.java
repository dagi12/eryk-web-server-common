package pl.edu.amu.wmi.service;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.amu.wmi.model.MyRuntimeException;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by erykmariankowski on 08.10.2018.
 */
@Service
public class MessageService {

    private static final String COUNTRY_CODE = Locale.getDefault().getCountry();
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private ResourceBundle bundle;
    @Value("${spring.mvc.locale:pl_PL}")
    private String locale;

    @PostConstruct
    public void init() {
        Locale.setDefault(LocaleUtils.toLocale(locale));
        bundle = ResourceBundle.getBundle("messages", new UTF8Control());
    }

    private String translate(String key) {
        try {
            String string = bundle.getString(key);
            return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (MissingResourceException e) {
            LOGGER.warn("No value for key: {}, locale: {}", key, COUNTRY_CODE, e);
        }
        return key;
    }

    public String getString(String key) {
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
