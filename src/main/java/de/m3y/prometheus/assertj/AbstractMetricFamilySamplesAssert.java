package de.m3y.prometheus.assertj;

import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import io.prometheus.client.Collector;
import io.prometheus.client.Collector.MetricFamilySamples;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.DoubleAssert;

/**
 * AssertJ generic support for MetricFamilySamples.
 * <p>
 * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfHistogram()}. Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfCounter()
 *     .hasSampleLabels(...)
 *     .hasSampleValue(...)
 * }</pre>
 *
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfHistogram()
 */
public abstract class AbstractMetricFamilySamplesAssert
        <SELF extends AbstractMetricFamilySamplesAssert<SELF>> extends
        AbstractAssert<SELF, MetricFamilySamples> {

    /**
     * Default constructor. Do not use directly but via {@link de.m3y.prometheus.assertj.MetricFamilySamplesAssert#assertThat }
     *
     * @param metricFamilySamples the MFS to assert.
     * @see MetricFamilySamplesAssert#assertThat(io.prometheus.client.Collector.MetricFamilySamples)
     */
    protected AbstractMetricFamilySamplesAssert(MetricFamilySamples metricFamilySamples) {
        super(metricFamilySamples, AbstractMetricFamilySamplesAssert.class);
    }

    /**
     * Verifies the Collector Type.
     *
     * @param type the Collector type.
     * @return {@code this} assertion object.
     * @see MetricFamilySamplesAssert#hasTypeOfCounter()
     * @see MetricFamilySamplesAssert#hasTypeOfGauge()
     * @see MetricFamilySamplesAssert#hasTypeOfSummary()
     * @see MetricFamilySamplesAssert#hasTypeOfHistogram()
     */
    public SELF hasType(Collector.Type type) {
        isNotNull();

        if (!Objects.equals(actual.type, type)) {
            failWithMessage("Expected MetricFamilySamples's %s type to be <%s> but was <%s>", actual.name, type, actual.type);
        }

        return myself;
    }


    /**
     * Verifies that one or more samples exist.
     * <p>
     * Note:
     * <ul>
     * <li>Metrics without labels always have a single, zero sample.</li>
     * </ul>
     *
     * @return {@code this} assertion object.
     */
    public SELF hasAnySamples() {
        isNotNull();

        if (null == actual.samples || actual.samples.isEmpty()) {
            failWithMessage("Expected one or more MetricFamilySamples %s samples but samples <%s> are null or empty", actual.name, actual.samples);
        }

        return myself;
    }

    /**
     * Verifies the number of samples.
     * <p>
     * Note: Metrics without labels have always one sample.
     *
     * @param size the number of samples.
     * @return {@code this} assertion object.
     */
    public SELF hasSampleSize(int size) {
        isNotNull();

        if (null == actual.samples) {
            failWithMessage("Expected MetricFamilySamples's %s samples <%s> but was null", actual.name);
        }
        if (actual.samples.size() != size) {
            failWithMessage("Expected MetricFamilySamples's %s samples with size %s but is with size %s .\nSamples are :\n%s",
                    actual.name, size, actual.samples.size(), toPrettyString(actual.samples));
        }

        return myself;
    }

    /**
     * Verifies the label names.
     *
     * @param labelNames a list of expected label names.
     * @return {@code this} assertion object.
     */
    public SELF hasSampleLabelNames(String... labelNames) {
        hasAnySamples();

        String sampleName = actual.name;
        if (actual.type == Collector.Type.SUMMARY || actual.type == Collector.Type.HISTOGRAM) {
            sampleName += "_count";
        }
        final List<String> actuallabelNames = getLabelNames(actual, sampleName);
        try {
            org.assertj.core.api.Assertions.assertThat(actuallabelNames).containsExactlyInAnyOrder(labelNames);
        } catch (AssertionError ae) {
            if (labelNames.length == 0) {
                failWithMessage("Expected MetricFamilySamples's %s samples to have no labels: %s ",
                        actual.name, ae.getMessage());
            } else {
                failWithMessage("Expected MetricFamilySamples's %s samples to have given labels: %s ",
                        actual.name, ae.getMessage() + "\nSamples are:\n" + toPrettyString(actual.samples));
            }
        }

        return myself;
    }


    protected SELF hasSampleValue(
            String sampleName,
            List<String> labelValues,
            UnaryOperator<? super DoubleAssert> valueAssert) {
        isNotNull();
        hasAnySamples();

        // Check if sample exist
        final List<String> labelNames = getLabelNames(actual, sampleName);
        MetricFamilySamples.Sample sample = findSample(sampleName, labelNames, labelValues);
        if (null == sample) {
            failWithMessage("Expected %s{%s} sample in samples :\n%s",
                    sampleName,
                    joinLabelNamesAndValues(labelNames, labelValues),
                    toPrettyString(actual.samples));
        } else {
            // Check sample value, if provided
            if (null != valueAssert) {
                try {
                    valueAssert.apply(new DoubleAssert(sample.value));
                } catch (AssertionError ae) {
                    failWithMessage("Unexpected value for %s{%s} : %s",
                            sampleName,
                            joinLabelNamesAndValues(labelNames, labelValues),
                            ae.getMessage());
                }
            }
        }

        return myself;
    }

    private String toPrettyString(List<MetricFamilySamples.Sample> samples) {
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        for (MetricFamilySamples.Sample sample : samples) {
            if (buf.length() > 1) {
                buf.append(",\n");
            }
            toPrettyString(buf, sample);
        }
        buf.append(']');
        return buf.toString();
    }

    private void toPrettyString(StringBuilder buf, MetricFamilySamples.Sample sample) {
        buf.append(sample.name).append('{');
        for (int i = 0; i < sample.labelNames.size(); i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(sample.labelNames.get(i)).append('=').append(sample.labelValues.get(i));
        }
        buf.append("} ").append(sample.value);
    }


    private MetricFamilySamples.Sample findSample(String sampleName,
                                                  List<String> labelNames, List<String> labelValues) {
        for (MetricFamilySamples.Sample sample : actual.samples) {
            if (sampleName.equals(sample.name) &&
                    sample.labelNames.equals(labelNames)) {
                if (null == labelValues) {
                    return sample; // First one found, by label name only
                }
                if (sample.labelValues.equals(labelValues)) {
                    return sample;
                }
            }
        }
        return null;
    }

    private String joinLabelNamesAndValues(List<String> labelNames, List<String> labelValues) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < Math.max(labelNames.size(), labelValues.size()); i++) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(i < labelNames.size() ? labelNames.get(i) : "");
            buf.append('=');
            buf.append(i < labelValues.size() ? labelValues.get(i) : "");
        }
        return buf.toString();
    }

    private static List<String> getLabelNames(MetricFamilySamples metricFamilySamples, String sampleName) {
        if (null == metricFamilySamples.samples || metricFamilySamples.samples.isEmpty()) {
            throw new IllegalArgumentException(metricFamilySamples.name + " has no samples : "
                    + metricFamilySamples.samples);
        }
        for (Collector.MetricFamilySamples.Sample sample : metricFamilySamples.samples) {
            // Extract labels from first sample matched by name
            if (sampleName.equals(sample.name)) {
                return sample.labelNames;
            }
        }
        throw new IllegalStateException("Can not extract label names for sample name " + sampleName
                + " from " + metricFamilySamples);
    }

}
