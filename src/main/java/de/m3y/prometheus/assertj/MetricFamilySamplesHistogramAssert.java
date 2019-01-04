package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;

/**
 * AssertJ support for <code>{@link io.prometheus.client.Collector.MetricFamilySamples}</code> with additional Histogram support.
 * <p>
 * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfHistogram()}. Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfHistogram() // Smart upgrade to MetricFamilySamplesHistogramAssert
 *     .hasSampleBucketValue(...)
 * }</pre>
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfHistogram()
 */
public class MetricFamilySamplesHistogramAssert extends AbstractMetricFamilySamplesHistogramAssert<MetricFamilySamplesHistogramAssert> {

    /**
     * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfHistogram()}:
     *
     * @param actual the actual sample.
     */
    protected MetricFamilySamplesHistogramAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }
}
