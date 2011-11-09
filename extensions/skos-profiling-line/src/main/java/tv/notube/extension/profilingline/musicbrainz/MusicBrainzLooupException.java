package tv.notube.extension.profilingline.musicbrainz;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MusicBrainzLooupException extends Exception {

    public MusicBrainzLooupException(String message, Exception e) {
        super(message, e);
    }

    public MusicBrainzLooupException(String message) {
        super(message);
    }
}
