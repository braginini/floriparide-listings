package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.PagingRequest;
import com.floriparide.listings.admin.api.response.ListResponse;
import com.floriparide.listings.web.json.Element;
import com.floriparide.listings.web.json.ProjectElement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface ICRUDController<E extends Element> {

    /**
     * Creates an entity of type {@code E}
     *
     * @param entity      {@link com.floriparide.listings.admin.api.request.impl.CreateEntityRequest} branch data to create
     * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
     *                    (headers, statuses, etc)
     * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created entity
     * with a HTTP 200 or HTTP 204 status code
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json",
            headers = "Accept=application/json")
    ResponseEntity<Long> create(@RequestBody E entity,
                             HttpServletRequest httpRequest) throws Exception;

	/**
	 * Creates an entity of type {@code E} in a batch
	 *
	 * @param batch       a list of entities to create
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return a instance of {@link org.springframework.http.ResponseEntity} with a list of newly created entities
	 * with a HTTP 200 or HTTP 204 status code, exception response if any exception occurs
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/batch", consumes = "application/json",
			headers = "Accept=application/json")
	ResponseEntity<List<E>> batchCreate(@RequestBody List<E> batch,
	                            HttpServletRequest httpRequest) throws Exception;

    /**
     * Deletes a specific entity of type {@code E}
     *
     * @param id The id of the entity to delete
     * @return an instance of empty {@link org.springframework.http.ResponseEntity}
     * with a HTTP 200 or HTTP 204 status code. In case if the entity was not found,
     * a HTTP 404 status should be returned along with custom error response. //todo create ExceptionResponse
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity delete(@PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception;

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity delete(@RequestParam(value = "id") Long[] ids,
                                 HttpServletRequest httpRequest) throws Exception;

    /**
     * Updates a specific entity of type {@code E}
     *
     * @param request     {@link com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest} entity data to update
     * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
     *                    (headers, statuses, etc)
     * @return an instance of empty {@link org.springframework.http.ResponseEntity}
     * with a HTTP 200 or HTTP 204 status code. In case if the entity was not found, a HTTP 404 status should be returned along with custom
     * error response. //todo create ExceptionResponse
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = "application/json",
            headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity update(@RequestBody E entity, @PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception;

    /**
     * Gets a specific entity of type {@code E}
     *
     * @param id The id of the entity to get
     * @return an instance of {@link com.floriparide.listings.admin.api.response.EntityResponse} wrapped with
     * {@link org.springframework.http.ResponseEntity} with a HTTP 200 or HTTP 204 status code.
     * In case of resource was not found, HTTP 404 status code
     * should be returned along with custom error response. //todo create ExceptionResponse
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", consumes = "application/json",
            headers = "Accept=application/json")
    public ResponseEntity<E> get(@PathVariable(value = "id") long id,
                                 HttpServletRequest httpRequest) throws Exception;
}
