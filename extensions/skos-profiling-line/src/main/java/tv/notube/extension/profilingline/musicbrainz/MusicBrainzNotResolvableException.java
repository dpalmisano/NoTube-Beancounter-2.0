package tv.notube.extension.profilingline.musicbrainz;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MusicBrainzNotResolvableException extends MusicBrainzLooupException {

    public MusicBrainzNotResolvableException(String message, Exception e) {
        super(message, e);
    }

    public MusicBrainzNotResolvableException(String message) {
        super(message);
    }
}
