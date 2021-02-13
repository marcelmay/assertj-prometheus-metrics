package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;

/**
 * AssertJ support for <code>{@link Collector.MetricFamilySamples}</code> with additional OpenTelemetry Info type support.
 * <p>
 * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfInfo()}. Recommended usage:
 * <pre>{@code
 * Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
 * MetricFamilySamplesAssert.assertThat(mfs)
 *     .hasTypeOfInfo() // Type check and smart upgrade to MetricFamilySamplesInfoAssert
 *     .hasSampleValue(...)
 * }</pre>
 *
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfInfo()
 */
public class MetricFamilySamplesInfoAssert extends AbstractMetricFamilySamplesInfoAssert<MetricFamilySamplesInfoAssert> {

    /**
     * Do not use directly but via {@link MetricFamilySamplesAssert#hasTypeOfInfo()}
     *
     * @param actual the actual sample.
     */
    protected MetricFamilySamplesInfoAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }
}
