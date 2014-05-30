package com.floriparide.listings.admin.api;

import com.floriparide.listings.dao.IBaseEntityDao;
import com.floriparide.listings.web.json.Element;

import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Andrei Tupitcyn
 */
public class SimpleCrudController<E extends Element, M, D extends IBaseEntityDao<M>>
        extends BaseController implements ICRUDController<E> {

    protected D dao;

    private Class<E> elementClass;
    private Class<M> modelClass;

    protected SimpleCrudController() {
        Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), SimpleCrudController.class);
        elementClass = (Class<E>) classes[0];
        modelClass = (Class<M>) classes[1];
    }

    public void setDao(D dao) {
        this.dao = dao;
    }

    @Override
    public ResponseEntity<Long> create(@RequestBody E entity,
                                    HttpServletRequest httpRequest) throws Exception {

        long id = dao.create((M) entity.getModel());

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<List<E>> batchCreate(@RequestBody List<E> batch, HttpServletRequest httpRequest) throws Exception {
		throw new UnsupportedOperationException("not supported yet");
	}

	@Override
    public ResponseEntity delete(@PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception {

        dao.delete(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity delete(@RequestParam(value = "id") Long[] ids,
                                 HttpServletRequest httpRequest) throws Exception {

        dao.delete(ids);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json",
            headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity update(@RequestBody E entity, @PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception {

        dao.update((M) entity.getModel(), id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<E> get(@PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception {

        M model = dao.get(id);

        if (model == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        E entity = elementClass.getConstructor(modelClass).newInstance(model);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }
}
