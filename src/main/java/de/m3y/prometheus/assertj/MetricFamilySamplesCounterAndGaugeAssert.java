package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;

/**
 * AssertJ support for <code>{@link Collector.MetricFamilySamples}</code> with additional Histogram support.
 * <p>
 * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfHistogram()}. Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfCounter() // Type check and mart upgrade to MetricFamilySamplesCounterAndGaugeAssert
 *                         // - or -
 *     .hasTypeOfGauge()   // Type check and smart upgrade to MetricFamilySamplesCounterAndGaugeAssert
 *     .hasSampleBucketValue(...)
 * }</pre>
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfHistogram()
 */
public class MetricFamilySamplesCounterAndGaugeAssert extends AbstractMetricFamilySamplesCounterAndGaugeAssert<MetricFamilySamplesCounterAndGaugeAssert> {

    /**
     * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfHistogram()}:
     *
     * @param actual the actual sample.
     */
    protected MetricFamilySamplesCounterAndGaugeAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }
}
