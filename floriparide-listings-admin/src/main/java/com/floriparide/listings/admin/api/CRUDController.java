package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.impl.CreateEntityRequest;
import com.floriparide.listings.admin.api.request.impl.UpdateEntityRequest;

import com.floriparide.listings.web.json.Element;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikhail Bragin
 */
public interface CRUDController<E extends Element> {

	/**
	 * Creates an entity of type {@code E}
	 *
	 * @param request     {@link com.floriparide.listings.admin.api.request.impl.CreateEntityRequest} branch data to create
	 * @param httpRequest The raw httpRequest type of {@link javax.servlet.http.HttpServletRequest} for advanced usage
	 *                    (headers, statuses, etc)
	 * @return an instance of {@link org.springframework.http.ResponseEntity} with an id of newly created entity
	 * with a HTTP 200 or HTTP 204 status code
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json",
			headers = "Accept=application/json")
	ResponseEntity<Long> create(CreateEntityRequest<E> request,
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
	@RequestMapping(method = RequestMethod.DELETE, consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity delete(long id, HttpServletRequest httpRequest) throws Exception;

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
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json",
			headers = "Accept=application/json")
	public ResponseEntity update(UpdateEntityRequest<E> request,
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
	public ResponseEntity<E> get(@PathVariable(value = "id") long id, HttpServletRequest httpRequest) throws Exception;
}
