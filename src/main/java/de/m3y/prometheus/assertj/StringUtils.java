package de.m3y.prometheus.assertj;

import java.util.Arrays;

import org.apache.commons.text.similarity.LevenshteinDistance;

/**
 * String helper such as for finding similar/closest matching strings.
 */
public class StringUtils {
    private StringUtils() {
        // No instantiation
    }

    /**
     * Note: This class has a natural ordering that is inconsistent with equals.
     */
    private static class StringIntTupel implements Comparable<StringIntTupel> {
        String content;
        int distance;

        @Override
        public int compareTo(StringIntTupel o) {
            return Integer.compare(distance, o.distance);
        }

        @Override
        public String toString() {
            return "{" +
                    "content='" + content + '\'' +
                    ", distance=" + distance +
                    '}';
        }
    }

    /**
     * Finds the top N similar options for a given reference.
     *
     * @param reference the reference value.
     * @param options   the available options.
     * @param n         limit the result to at most n option values.
     * @return the top n closest matching options.
     */
    public static String[] similar(String reference, String[] options, int n) {
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        StringIntTupel[] values = new StringIntTupel[options.length];
        for (int i = 0; i < values.length; i++) {
            String option = options[i];
            final StringIntTupel tupel = new StringIntTupel();
            tupel.content = option;
            tupel.distance = levenshteinDistance.apply(reference, option);
            values[i] = tupel;
        }
        Arrays.sort(values);
        String[] candidates = new String[Math.min(options.length, n)];
        for (int i = 0; i < candidates.length; i++) {
            candidates[i] = values[i].content;
        }
        return candidates;
    }
}
