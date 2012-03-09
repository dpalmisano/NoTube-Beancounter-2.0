package tv.notube.synch.service;

import com.sun.jersey.api.core.InjectParam;
import tv.notube.synch.core.*;
import tv.notube.synch.model.Status;
import tv.notube.synch.model.logger.Action;
import tv.notube.synch.core.logger.LoggerException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
@Path("/synch")
@Produces(MediaType.APPLICATION_JSON)
public class SynchronizerService {

    @InjectParam
    private InstanceManager instanceManager;

    @GET
    @Path("/status")
    public Status getStatus() {
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            return synchronizer.getStatus();
        } catch (SynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
    }

    @GET
    @Path("/token/{process}")
    public UUID getToken( @PathParam("process") String processName ) {
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            return synchronizer.getToken(processName);
        } catch (SynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
    }

    @GET
    @Path("/lock/{process}/{token}")
    public void lock(
            @PathParam("process") String processName,
            @PathParam("token") String token
    ) {
        UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(token);
        } catch (Exception e) {
            final String errMsg = "[" + token + "] doesn't seem a valid UUID";
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    errMsg
            );
        }
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            synchronizer.lock(processName, tokenUuid);
        } catch (AlreadyLockedSynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.LOCKED,
                    e.getMessage()
            );
        } catch (WrongTurnSynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.WAIT,
                    e.getMessage()
            );
        }
        catch (SynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
        throw new SynchronizerServiceException(
                SynchronizerServiceException.Status.OK,
                "[" + processName + "] locks"
        );
    }

    @GET
    @Path("/release/{process}/{token}")
    public void release(
            @PathParam("process") String processName,
            @PathParam("token") String token
    ) {
        UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(token);
        } catch (Exception e) {
            final String errMsg = "[" + token + "] doesn't seem a valid UUID";
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    errMsg
            );
        }
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            synchronizer.release(processName, tokenUuid);
        } catch (AlreadyReleasedSynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.RELEASED,
                    e.getMessage()
            );
        } catch (ReleaseNotPermittedSynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.WAIT,
                    e.getMessage()
            );
        } catch (WrongTurnSynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.WAIT,
                    e.getMessage()
            );
        } catch (SynchronizerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
        throw new SynchronizerServiceException(
                SynchronizerServiceException.Status.OK,
                "[" + processName + "] releases"
        );
    }

    @GET
    @Path("/latest")
    public Action latest() {
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            return synchronizer.getLogger().getLatest();
        } catch (LoggerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
    }

    @GET
    @Path("/log")
    public Action[] log() {
        LoggableSynchronizer synchronizer = instanceManager.getSynchronizer();
        try {
            return synchronizer.getLogger().getLog();
        } catch (LoggerException e) {
            throw new SynchronizerServiceException(
                    SynchronizerServiceException.Status.ERROR,
                    e.getMessage()
            );
        }
    }

}
