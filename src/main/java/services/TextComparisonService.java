package services;

import domain.googlecloud.Evaluation;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.NGram;

import java.util.Map;

/**
 * Created by ericreis on 11/15/17.
 */
public class TextComparisonService {

    Levenshtein l;
    NGram ngram;
    Cosine cosine;

    public TextComparisonService() {
        this.l = new Levenshtein();
        this.ngram = new NGram(5);
        this.cosine = new Cosine(5);
    }

    public Evaluation evaluate(String tgtTxt, String cmpTxt) {
        Map<String, Integer> tgtProfile = cosine.getProfile(tgtTxt);
        Map<String, Integer> cmpProfile = cosine.getProfile(cmpTxt);
        return new Evaluation(l.distance(tgtTxt, cmpTxt),
                              ngram.distance(tgtTxt, cmpTxt),
                              cosine.similarity(tgtProfile, cmpProfile));
    }
}
