package com.floriparide.listings.admin.api;

import com.floriparide.listings.admin.api.request.f.CreateEntityRequestCommon;
import com.floriparide.listings.admin.api.request.f.UpdateEntityRequestCommon;

import com.floriparide.listings.web.json.Element;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mikhail Bragin
 */
public interface CRUDController<E extends Element> {

	ResponseEntity<Long> create(CreateEntityRequestCommon<E> request,
	                            HttpServletRequest httpRequest) throws Exception;

	public ResponseEntity delete(long id, HttpServletRequest httpRequest) throws Exception;

	public ResponseEntity update(UpdateEntityRequestCommon<E> request,
	                             HttpServletRequest httpRequest) throws Exception;

	public ResponseEntity<E> get(long id, HttpServletRequest httpRequest) throws Exception;
}
