package domain.googlecloud;

/**
 * Created by ericreis on 11/15/17.
 */
public class Evaluation {
    private double levenshteinDist;
    private double ngramDist;
    private double cosineSim;

    public Evaluation(double levenshteinDist, double ngramDist, double cosineSim) {
        this.levenshteinDist = levenshteinDist;
        this.ngramDist = ngramDist;
        this.cosineSim = cosineSim;
    }
}
