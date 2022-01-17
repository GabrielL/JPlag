package de.jplag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.jplag.exceptions.ExitException;
import de.jplag.exceptions.SubmissionException;
import de.jplag.options.JPlagOptions;
import de.jplag.strategy.ComparisonMode;
import de.jplag.strategy.ComparisonStrategy;
import de.jplag.strategy.NormalComparisonStrategy;
import de.jplag.strategy.ParallelComparisonStrategy;

/**
 * This class coordinates the whole errorConsumer flow.
 */
public class JPlag {
    // INPUT:
    private final Language language;

    // CORE COMPONENTS:
    private final ComparisonStrategy comparisonStrategy;
    private final GreedyStringTiling coreAlgorithm; // Contains the comparison logic.
    private final JPlagOptions options;
    private final ErrorCollector errorCollector;

    /**
     * Creates and initializes a JPlag instance, parameterized by a set of options.
     * @param options determines the parameterization.
     * @throws ExitException if the initialization fails.
     */
    public JPlag(JPlagOptions options) throws ExitException {
        this.options = options;
        errorCollector = new ErrorCollector(options);
        coreAlgorithm = new GreedyStringTiling(options);
        language = loadLanguage(errorCollector, options.getLanguageName());
        this.options.setLanguageDefaults(language);

        System.out.println("Initialized language " + language.getName());
        comparisonStrategy = initializeComparisonStrategy(options.getComparisonMode());
    }

    public Language getLanguage() {
        return language;
    }

    /**
     * Main procedure, executes the comparison of source code submissions.
     * @return the results of the comparison, specifically the submissions whose similarity exceeds a set threshold.
     * @throws ExitException if the JPlag exits preemptively.
     */
    public JPlagResult run() throws ExitException {
        // Parse and validate submissions.
        SubmissionSetBuilder builder = new SubmissionSetBuilder(language, options, errorCollector);
        SubmissionSet submissionSet = builder.buildSubmissionSet();

        if (submissionSet.hasBaseCode()) {
            coreAlgorithm.createHashes(submissionSet.getBaseCode().getTokenList(), options.getMinimumTokenMatch(), true);
        }

        int submissionCount = submissionSet.numberOfSubmissions();
        if (submissionCount < 2) {
            throw new SubmissionException("Not enough valid submissions! (found " + submissionCount + " valid submissions)");
        }

        // Compare valid submissions.
        JPlagResult result = comparisonStrategy.compareSubmissions(submissionSet);
        errorCollector.print("\nTotal time for comparing submissions: " + TimeUtil.formatDuration(result.getDuration()), null);
        return result;
    }

    private ComparisonStrategy initializeComparisonStrategy(final ComparisonMode comparisonMode) {
        return switch (comparisonMode) {
            case NORMAL -> new NormalComparisonStrategy(options, coreAlgorithm);
            case PARALLEL -> new ParallelComparisonStrategy(options, coreAlgorithm);
        };
    }

    private Language loadLanguage(final ErrorCollector errorCollector, final String classPath) {
        return Languages.provider(classPath).create(errorCollector);
    }
}
