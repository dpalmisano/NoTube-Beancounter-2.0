package tv.notube.commons.model.activity.bbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class GenreBuilder {

    private static final String file = "/bbc-genres.txt";

    private static GenreBuilder INSTANCE = new GenreBuilder();

    public static GenreBuilder getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GenreBuilder();
        }
        return INSTANCE;
    }

    private Map<URL, BBCGenre> genres;

    private GenreBuilder() {
        InputStream is = this.getClass().getResourceAsStream(file);
        genres = new HashMap<URL, BBCGenre>();
        try {
            loadGenres(is, genres);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        "Error while closing the stream.",
                        e
                );
            }
        }

    }

    private void loadGenres(InputStream is, Map<URL, BBCGenre> genres) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        int i = 0;
        Map<String, List<String>> hierarchy = new HashMap<String,List<String>>();
        try {
            while ((line = br.readLine()) != null) {
                fillUp(line, hierarchy);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error while reading row '" + i + "'",
                    e
            );
        }
        for(String key : hierarchy.keySet()) {
            String label = parseLabel(key);
            URL url = parseUrl(key);
            BBCGenre bbcGenre = new BBCGenre(url, label);
            for(String value : hierarchy.get(key)) {
                String valueLabel = parseLabel(value);
                URL valueUrl = parseUrl(key + '/' + value);
                BBCGenre subGenre = new BBCGenre(valueUrl, valueLabel);
                bbcGenre.addSubgenre(subGenre);
                genres.put(subGenre.getReference(), subGenre);
            }
            genres.put(bbcGenre.getReference(), bbcGenre);
        }
    }

    private void fillUp(String line, Map<String, List<String>> hierarchy) {
        String genresStr[] = line.split("/");
        if(hierarchy.containsKey(genresStr[0])) {
            for(int i=1; i < genresStr.length; i++) {
                hierarchy.get(genresStr[0]).add(genresStr[i]);
            }
        } else {
            List<String> partial = new ArrayList<String>();
            for(int i=1; i < genresStr.length; i++) {
                partial.add(genresStr[i]);
            }
            hierarchy.put(genresStr[0], partial);
        }
    }

    private String parseLabel(String s) {
        String tokens[] = s.split("_");
        String label = "";
        for(String token : tokens) {
            label += token.toUpperCase().charAt(0) + token.substring(1) + " ";
        }
        return label.substring(0, label.length() - 1);
    }

    private URL parseUrl(String s) {
        final String bbcUrl = "http://www.bbc.co.uk/programmes/genres/%s#genre";
        String urlStr =  String.format(bbcUrl, s.replace("_", ""));
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed url", e);
        }
    }

    public BBCGenre lookup(URL genre) {
        return genres.get(genre);
    }

}
