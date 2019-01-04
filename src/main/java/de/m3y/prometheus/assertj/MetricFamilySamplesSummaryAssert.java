package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;

/**
 * AssertJ support for <code>{@link io.prometheus.client.Collector.MetricFamilySamples}</code> for Summary metrics.
 * <p>
 * Do not use directly. Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfSummary() // Smart upgrade to AbstractMetricFamilySamplesSummaryAssert
 *     .hasSampleSumValue(...)
 *     .hasSampleCountValue(...)
 * }</pre>
 *
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfSummary()
 */
public class MetricFamilySamplesSummaryAssert extends AbstractMetricFamilySamplesSummaryAssert<MetricFamilySamplesSummaryAssert> {
    /**
     * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfSummary()} ()}:
     *
     * @param actual the sample
     */
    protected MetricFamilySamplesSummaryAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }
}
