package pl.edu.amu.wmi.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.GenericTypeResolver;
import org.springframework.lang.NonNull;
import pl.edu.amu.wmi.model.MyRuntimeException;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class ObjectUtil {

    private ObjectUtil() {

    }

    public static String getPathFromPackage(Class aClass) {
        return aClass.getPackage().getName().replace(".", "/");
    }

    public static boolean doNotOwnProperty(Class<?> clazz, String property) {
        Class<?> currentClass = clazz;
        do {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.getName().equals(property)) {
                    return true;
                }
            }
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);
        return false;
    }


    @NonNull
    public static Class<?> getFieldType(Class<?> clazz, String property) {
        Class<?> currentClass = clazz;
        do {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.getName().equals(property)) {
                    return field.getType();
                }
            }
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);
        throw new MyRuntimeException("Wrong property name");
    }

    public static boolean doNotOwnProperty(Class<?> clazz, List<String> propertyList) {
        for (String property : propertyList) {
            if (doNotOwnProperty(clazz, property)) {
                return false;
            }
        }
        return true;
    }

    public static <T> void iterateProperties(Class<T> tClass, Consumer<Field> consumer) {
        for (Field field : tClass.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers()) &&
                    field.getAnnotation(JsonIgnore.class) == null &&
                    field.getAnnotation(Transient.class) == null) {
                consumer.accept(field);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, S> Class<T> getGenericType(Object o, Class<S> tClass, int param) {
        Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(o.getClass(), tClass);
        // should stay that, suppress sonar
        if (classes != null) {
            return (Class<T>) Arrays.asList(classes).get(param);
        }
        return null;
    }

    public static boolean notNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

}
