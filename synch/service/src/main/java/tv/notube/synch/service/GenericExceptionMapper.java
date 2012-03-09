package tv.notube.synch.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * {@link BaseExceptionMapper} implementation to handle {@link RuntimeException}s.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Provider
public class GenericExceptionMapper extends BaseExceptionMapper<SynchronizerServiceException> {

    public javax.ws.rs.core.Response toResponse(SynchronizerServiceException re) {
        Gson gson = ( new GsonBuilder() ).setPrettyPrinting().create();
        ErrorBean eb = build(re);
        String json = gson.toJson(eb);
        return javax.ws.rs.core.Response.status(
                Response.Status.OK
        ).entity( json ).build();
    }

    private ErrorBean build(SynchronizerServiceException e) {
            return new ErrorBean(
                    e.getStatus().name(),
                    e.getMessage()
            );
        }

    private class ErrorBean {

        private String status;

        private String message;

        protected ErrorBean(String status, String message) {
            this.status = status;
            this.message = message;
        }



    }

}