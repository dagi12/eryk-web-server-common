package pl.edu.amu.wmi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.amu.wmi.model.GeneralResponse;
import pl.edu.amu.wmi.service.CrudService;

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
    public ResponseEntity<GeneralResponse<T>> create(@RequestBody T item) {
        String result = crudService.verify(item);
        if (result != null) {
            return ResponseEntity.badRequest().body(new GeneralResponse<>(result));
        }
        return ResponseEntity.ok(new GeneralResponse<>(crudService.create(item)));
    }

    @Override
    @PutMapping(value = "/{id}")
    public T update(@PathVariable int id, @RequestBody T t) {
        return getCrudService().update(id, t);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        getCrudService().delete(id);
    }


}
