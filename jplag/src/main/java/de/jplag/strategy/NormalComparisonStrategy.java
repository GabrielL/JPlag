package de.jplag.strategy;

import java.util.ArrayList;
import java.util.List;

import de.jplag.GreedyStringTiling;
import de.jplag.JPlagComparison;
import de.jplag.JPlagResult;
import de.jplag.Submission;
import de.jplag.SubmissionSet;
import de.jplag.options.SimilarityMetric;

public class NormalComparisonStrategy extends AbstractComparisonStrategy {

    public NormalComparisonStrategy(GreedyStringTiling greedyStringTiling, SimilarityMetric similarityMetric, float similarityThreshold) {
        super(greedyStringTiling, similarityMetric, similarityThreshold);
    }

    @Override
    public JPlagResult compareSubmissions(SubmissionSet submissionSet) {
        boolean withBaseCode = submissionSet.hasBaseCode();
        if (withBaseCode) {
            compareSubmissionsToBaseCode(submissionSet);
        }

        List<Submission> submissions = submissionSet.getSubmissions();
        long timeBeforeStartInMillis = System.currentTimeMillis();
        int i, j, numberOfSubmissions = submissions.size();
        Submission first, second;
        List<JPlagComparison> comparisons = new ArrayList<>();

        for (i = 0; i < (numberOfSubmissions - 1); i++) {
            first = submissions.get(i);
            if (first.getTokenList() == null) {
                continue;
            }
            for (j = (i + 1); j < numberOfSubmissions; j++) {
                second = submissions.get(j);
                if (second.getTokenList() == null) {
                    continue;
                }
                compareSubmissions(first, second, withBaseCode).ifPresent(comparisons::add);
            }
        }

        long durationInMillis = System.currentTimeMillis() - timeBeforeStartInMillis;
        return new JPlagResult(comparisons, submissionSet, durationInMillis);
    }

}
