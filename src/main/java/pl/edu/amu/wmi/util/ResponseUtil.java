package pl.edu.amu.wmi.util;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import pl.edu.amu.wmi.model.GeneralResponse;

import java.util.List;
import java.util.function.Supplier;


/**
 * Created by erykmariankowski on 17.06.2018.
 */
public final class ResponseUtil {

    private ResponseUtil() {
    }

    public static ResponseEntity response(boolean b) {
        return b ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
    public static <T> ResponseEntity<T> response(T t) {
        return t != null ? ResponseEntity.ok(t) : ResponseEntity.badRequest().build();
    }

    public static <T> ResponseEntity<T> responseNoContent(T t) {
        return t != null ? ResponseEntity.ok(t) : ResponseEntity.noContent().build();
    }

    public static <T> ResponseEntity<T> responseForbidden(T t) {
        return t != null ? ResponseEntity.ok(t) : ResponseEntity.noContent().build();
    }

    public static ResponseEntity<String> responseSupplier(Supplier<String> supplier) {
        String msg = supplier.get();
        return msg == null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(msg);
    }

    public static <T, S extends GeneralResponse<T>> ResponseEntity<S> responseGeneral(S t) {
        return t.isDone() ? ResponseEntity.ok(t) : ResponseEntity.badRequest().body(t);
    }


    public static <T> ResponseEntity<GeneralResponse<List<T>>> responseGeneral(List<T> t) {
        return CollectionUtils.isEmpty(t) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(new GeneralResponse<>(t));
    }

    public static ResponseEntity<GeneralResponse> responseGeneral(boolean t) {
        return t ? ResponseEntity.ok(new GeneralResponse()) : ResponseEntity.badRequest().build();
    }


    public static <T> ResponseEntity<GeneralResponse<T>> responseGeneral(String msg) {
        return msg == null ?
                ResponseEntity.ok(new GeneralResponse<>()) :
                ResponseEntity.badRequest().body(new GeneralResponse<>(msg));
    }

    public static <T> ResponseEntity<GeneralResponse<T>> responseGeneral(Supplier<String> supplier) {
        String msg = supplier.get();
        return msg == null ?
                ResponseEntity.ok(new GeneralResponse<>()) :
                ResponseEntity.badRequest().body(new GeneralResponse<>(msg));
    }
}
