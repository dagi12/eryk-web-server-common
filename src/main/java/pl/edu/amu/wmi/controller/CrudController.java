package pl.edu.amu.wmi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.amu.wmi.service.CrudService;

/**
 * Stworzone przez Eryk Mariankowski dnia 18.06.18.
 */
public interface CrudController<T> {

    CrudService<T> getCrudService();

    @RequestMapping(method = RequestMethod.POST)
    default T create(@RequestBody T t) {
        getCrudService().create(t);
        return t;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    default T update(@PathVariable int id, @RequestBody T t) {
        getCrudService().update(id, t);
        return t;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    default void delete(@PathVariable int id) {
        getCrudService().delete(id);
    }


}
