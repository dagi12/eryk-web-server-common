package pl.edu.amu.wmi.util;

import org.springframework.http.ResponseEntity;
import pl.edu.amu.wmi.model.GeneralResponse;

import java.util.function.Supplier;


/**
 * Created by erykmariankowski on 17.06.2018.
 */
public class ResponseUtil {

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

    public static ResponseEntity<GeneralResponse> responseGeneral(boolean t) {
        return t ? ResponseEntity.ok(new GeneralResponse()) : ResponseEntity.badRequest().build();
    }

    public static ResponseEntity<?> responseSupplier(Supplier<String> supplier) {
        String msg = supplier.get();
        return msg == null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body(msg);
    }

    public static ResponseEntity<GeneralResponse> responseGeneral(Supplier<String> supplier) {
        String msg = supplier.get();
        return msg == null ? ResponseEntity.ok(new GeneralResponse()) : ResponseEntity.badRequest().body(new GeneralResponse(msg));
    }
}
