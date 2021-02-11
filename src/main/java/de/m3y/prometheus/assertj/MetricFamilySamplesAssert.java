package de.m3y.prometheus.assertj;

import java.util.Arrays;
import java.util.List;

import io.prometheus.client.Collector;

/**
 * AssertJ support for <code>{@link io.prometheus.client.Collector.MetricFamilySamples}</code> metrics.
 * <p>
 * Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfCounter()
 *     .has...
 * }</pre>
 */
public class MetricFamilySamplesAssert extends AbstractMetricFamilySamplesAssert<MetricFamilySamplesAssert> {
    /**
     * Should not be directly instantiated. Use {@link #assertThat(Collector.MetricFamilySamples)}.
     *
     * @param metricFamilySamples the samples.
     */
    protected MetricFamilySamplesAssert(Collector.MetricFamilySamples metricFamilySamples) {
        super(metricFamilySamples);
    }

    /**
     * Asserts that the MFS is of type Info.
     * <p>
     * Returns a specialization with additional asserts for MFS of type Info.
     *
     * @return SELF
     */
    public MetricFamilySamplesCounterAndGaugeAssert hasTypeOfInfo() {
        // TODO?
        return new MetricFamilySamplesCounterAndGaugeAssert(hasType(Collector.Type.INFO).actual);
    }

    /**
     * Asserts that the MFS is of type Counter.
     * <p>
     * Returns a specialization with additional asserts for MFS of type Counter or Gauge.
     *
     * @return SELF
     */
    public MetricFamilySamplesCounterAndGaugeAssert hasTypeOfCounter() {
        return new MetricFamilySamplesCounterAndGaugeAssert(hasType(Collector.Type.COUNTER).actual);
    }

    /**
     * Asserts that the MFS is of type Gauge.
     * <p>
     * Returns a specialization with additional asserts for MFS of type Counter or Gauge.
     *
     * @return SELF
     */
    public MetricFamilySamplesCounterAndGaugeAssert hasTypeOfGauge() {
        return new MetricFamilySamplesCounterAndGaugeAssert(hasType(Collector.Type.GAUGE).actual);
    }

    /**
     * Asserts that the MFS is of type Summary.
     * <p>
     * Returns a specialization with additional asserts for MFS of type Summary.
     *
     * @return SELF
     */
    public MetricFamilySamplesSummaryAssert hasTypeOfSummary() {
        return new MetricFamilySamplesSummaryAssert(hasType(Collector.Type.SUMMARY).actual);
    }

    /**
     * Asserts that the MFS is of type Histogram.
     * <p>
     * Returns a specialization with additional asserts for MFS of type Histogram.
     *
     * @return SELF
     */
    public MetricFamilySamplesHistogramAssert hasTypeOfHistogram() {
        return new MetricFamilySamplesHistogramAssert(hasType(Collector.Type.HISTOGRAM).actual);
    }

    /**
     * Creates a new assertion for given MFS.
     *
     * @param actual the MFS.
     * @return new instance.
     */
    public static MetricFamilySamplesAssert assertThat(Collector.MetricFamilySamples actual) {
        return new MetricFamilySamplesAssert(actual);
    }

    /**
     * Returns vararg label values as list. DSL syntactic sugar.
     *
     * @param labelValues the label values
     * @return the list of label values.
     */
    public static List<String> labelValues(String... labelValues) {
        return Arrays.asList(labelValues);
    }
}
