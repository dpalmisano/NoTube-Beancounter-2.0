package tv.notube.profiler.storage;

import tv.notube.commons.model.UserProfile;

import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

/**
 * Main interface defining the behavior of a generic
 * {@link ProfileStore}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ProfileStore {

    public enum Format {
        RDF,
        CSV
    }

    public void storeUserProfile(UserProfile userProfile)
            throws ProfileStoreException;

    public UserProfile getUserProfile(String username)
            throws ProfileStoreException;

    public UserProfile getUserProfile(UUID userId)
            throws ProfileStoreException;

    public void deleteUserProfile(String username)
            throws ProfileStoreException;

    public void deleteUserProfile(UUID userId)
            throws ProfileStoreException;

    public void export(
            UUID userid,
            OutputStream outputStream,
            Format format
    ) throws ProfileStoreException;

    public void setNamespaces(Map<String, String> nameSpacesConfiguration);

    public Map<String, String> getNamespaces();

}
