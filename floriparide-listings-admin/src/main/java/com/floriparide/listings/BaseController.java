package com.floriparide.listings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floriparide.listings.admin.api.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Defines basic behaviour of a controller.
 * <p/>
 * All API methods defined in classes that extends this abstract class should return result wrapped by
 * {@link org.springframework.http.ResponseEntity} object which makes easier to
 * manage {@link org.springframework.http.HttpStatus} status codes and {@link org.springframework.http.HttpHeaders}
 * headers.
 *
 * @author Mikhail Bragin
 */
public abstract class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    protected ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    protected MappingJackson2HttpMessageConverter jsonConverter;

    @PostConstruct
    public void init() {
        /*//do not show empty arrays of lists
		jsonConverter.getObjectMapper().configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);*/
    }


    /**
     * Handles an exception thrown by a controller. This method is needed
     * because the application front-end access some features using http POST
     * and GET. We need to deliver proper error codes to the client.
     *
     * @param exception
     * @throws Exception
     * @see
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(HttpServletResponse response, Exception exception) throws IOException {
        exception.printStackTrace();
        response.addHeader("X-application-error-code", "some_code");
        response.addHeader("X-application-error-subcode", "some_subcode");
        return new ResponseEntity<>(new ErrorResponse(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
