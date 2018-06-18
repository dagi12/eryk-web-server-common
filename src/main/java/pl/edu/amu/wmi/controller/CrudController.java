package pl.edu.amu.wmi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.amu.wmi.service.CrudService;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public abstract class CrudController<T> {

    @Autowired
    private final CrudService<T> crudService;

    @Autowired
    protected CrudController(CrudService<T> crudService) {
        this.crudService = crudService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public T create(@RequestBody T t) {
        crudService.create(t);
        return t;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public T update(@PathVariable int id, @RequestBody T t) {
        crudService.update(id, t);
        return t;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) {
        crudService.delete(id);
    }


}
