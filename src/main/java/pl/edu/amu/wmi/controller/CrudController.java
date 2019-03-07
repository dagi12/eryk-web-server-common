package pl.edu.amu.wmi.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.amu.wmi.exception.VerifyException;
import pl.edu.amu.wmi.service.CrudService;

import javax.validation.Valid;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudController<T> {

    CrudService<T> getCrudService();

    @PostMapping
    default T create(@RequestBody @Valid T item) {
        CrudService<T> crudService = getCrudService();
        String result = crudService.verify(item);
        if (result != null) {
            throw new VerifyException(result);
        }
        return crudService.create(item);
    }

    @PutMapping(value = "/{id}")
    default T update(@PathVariable int id, @RequestBody @Valid T t) {
        CrudService<T> crudService = getCrudService();
        String result = crudService.verifyUpdate(t);
        if (result != null) {
            throw new VerifyException(result);
        }
        return getCrudService().update(id, t);
    }

    @DeleteMapping(value = "/{id}")
    default void delete(@PathVariable int id) {
        getCrudService().delete(id);
    }


}
