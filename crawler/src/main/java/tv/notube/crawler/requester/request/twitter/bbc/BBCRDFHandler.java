package tv.notube.crawler.requester.request.twitter.bbc;

import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import tv.notube.commons.model.activity.BBCProgramme;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BBCRDFHandler implements RDFHandler {

    private BBCProgramme bbcProgramme;

    private boolean isComplete = false;

    public void startRDF() throws RDFHandlerException {
        bbcProgramme = new BBCProgramme();
    }

    public void endRDF() throws RDFHandlerException {
        isComplete = true;
    }

    public void handleNamespace(String s, String s1) throws RDFHandlerException {
        // do nothing
    }

    public void handleStatement(Statement statement) throws RDFHandlerException {
        org.openrdf.model.URI predicate = statement.getPredicate();
        if (predicate.equals(new URIImpl("http://xmlns.com/foaf/0.1/depiction"))) {
            Value object = statement.getObject();
            try {
                bbcProgramme.setPicture(new URL(object.stringValue()));
            } catch (MalformedURLException e) {
                // put it to null
            }
        } else if (predicate.equals(new URIImpl("http://purl.org/ontology/po/genre"))) {
            Value object = statement.getObject();
            bbcProgramme.addGenre(getGenre(object.stringValue()));
        } else if (predicate.equals(new URIImpl("http://xmlns.com/foaf/0.1/primaryTopic"))) {
            Value object = statement.getObject();
            try {
                bbcProgramme.setUrl(new URL(object.stringValue()));
            } catch (MalformedURLException e) {
                // put it to null
            }
        } else if (predicate.equals(new URIImpl("http://purl.org/dc/elements/1.1/title"))) {
            Value object = statement.getObject();
            bbcProgramme.setName(object.stringValue());
        } else if (predicate.equals(new URIImpl("http://purl.org/ontology/po/medium_synopsis"))) {
            Value object = statement.getObject();
            bbcProgramme.setMediumSynopsis(object.stringValue());
        } else if (predicate.equals(new URIImpl("http://www.w3.org/2000/01/rdf-schema#label"))) {
            Value object = statement.getObject();
            bbcProgramme.setDescription(object.stringValue());
        }
    }

    private String getGenre(String genreURL) {
        throw new UnsupportedOperationException("NIY");
    }

    public void handleComment(String s) throws RDFHandlerException {
        // do nothing - it's a comment
    }

    public BBCProgramme getProgramme() {
        if(!isComplete) {
            throw new IllegalStateException("Parsing could be not ended");
        }
        return bbcProgramme;
    }
}
