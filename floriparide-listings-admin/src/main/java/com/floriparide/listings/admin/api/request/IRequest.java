package com.floriparide.listings.admin.api.request;

/**
 * Basic interface for web requests.
 * Method {@link this#validate()} should be called before getter methods
 *
 * @author Mikhail Bragin
 */
public interface IRequest {

    void validate() throws Exception;
}
