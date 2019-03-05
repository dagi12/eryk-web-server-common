package pl.edu.amu.wmi.controller;

import org.springframework.web.bind.annotation.*;
import pl.edu.amu.wmi.exception.VerifyException;
import pl.edu.amu.wmi.service.CrudService;

import javax.validation.Valid;
import java.util.List;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public abstract class CrudControllerImpl<T> implements CrudController<T> {

    private final CrudService<T> crudService;

    public CrudControllerImpl(CrudService<T> crudService) {
        this.crudService = crudService;
    }

    @Override
    public CrudService<T> getCrudService() {
        return crudService;
    }

    @Override
    @PostMapping
    public T create(@RequestBody @Valid T item) {
        String result = crudService.verify(item);
        if (result != null) {
            throw new VerifyException(result);
        }
        return crudService.create(item);
    }

    @Override
    @PutMapping(value = "/{id}")
    public T update(@PathVariable int id, @RequestBody @Valid T t) {
        String result = crudService.verifyUpdate(t);
        if (result != null) {
            throw new VerifyException(result);
        }
        return crudService.update(id, t);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        crudService.delete(id);
    }

    @GetMapping
    public List<T> get() {
        return crudService.get();
    }

}
