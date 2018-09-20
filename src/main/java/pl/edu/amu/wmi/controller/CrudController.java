package pl.edu.amu.wmi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.amu.wmi.model.GeneralResponse;
import pl.edu.amu.wmi.service.CrudService;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudController<T> {

    CrudService<T> getCrudService();

    @PostMapping
    default ResponseEntity<GeneralResponse<T>> create(@RequestBody T item) {
        CrudService<T> crudService = getCrudService();
        String result = crudService.verify(item);
        if (result != null) {
            return ResponseEntity.badRequest().body(new GeneralResponse<>(result));
        }
        return ResponseEntity.ok(new GeneralResponse<>(crudService.create(item)));
    }

    @PutMapping(value = "/{id}")
    default T update(@PathVariable int id, @RequestBody T t) {
        return getCrudService().update(id, t);
    }

    @DeleteMapping(value = "/{id}")
    default void delete(@PathVariable int id) {
        getCrudService().delete(id);
    }


}
