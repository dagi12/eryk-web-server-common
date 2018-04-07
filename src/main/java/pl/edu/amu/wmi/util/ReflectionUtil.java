package pl.edu.amu.wmi.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    private ReflectionUtil() {

    }

    public static <T> void merge(T target, T update) {
        if (!target.getClass().isAssignableFrom(update.getClass())) {
            LOGGER.warn("Obiekty nie są tego samego typu.");
            return;
        }

        Method[] methods = target.getClass().getMethods();

        for (Method fromMethod : methods) {
            if (fromMethod.getDeclaringClass().equals(target.getClass()) && !fromMethod.getName().startsWith("is")) {

                String fromName = fromMethod.getName();
                String toName = StringUtils.replaceOnce(fromName, "is", "set");

                try {
                    Method toMetod = target.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Nie można zmergować obiektu.", e);
                }

            }
        }
    }


}
