package pl.edu.amu.wmi.service;

import com.sun.xml.internal.bind.marshaller.Messages;
import org.springframework.stereotype.Service;
import pl.edu.amu.wmi.model.MyRuntimeException;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by erykmariankowski on 08.10.2018.
 */
@Service
public class MessageService {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public String getString(String key) {
        if (isEmpty(key)) {
            throw new MyRuntimeException("Key cannot be empty");
        }
        String result = bundle.getString(key);
        if (isEmpty(result)) {
            throw new MyRuntimeException(Messages.format("No value for key: {}, locale: {}", key, Locale.getDefault().getCountry()));
        }
        return result;
    }

}
