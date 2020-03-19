package de.m3y.prometheus.assertj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import io.prometheus.client.Collector;
import org.assertj.core.api.DoubleAssert;

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
 *     .hasSampleValue(0.9, // quantile
 *                     42)   // value
 *     .hasSampleValue(0.95, // quantile
 *                     47)   // value
 *      ;
 * }</pre>
 *
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfSummary()
 */
public abstract class AbstractMetricFamilySamplesSummaryAssert
        <SELF extends AbstractMetricFamilySamplesSummaryAssert<SELF>> extends
        AbstractMetricFamilySamplesSumAndCountAssert<SELF> {
    protected AbstractMetricFamilySamplesSummaryAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param quantile    the quantile
     * @param valueAssert assert for value, eg <code>da-&gt;da.isCloseTo(10.0, withinPercentage(20d))</code>
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(double quantile, UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(Collections.emptyList(), quantile, valueAssert);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param quantile the quantile
     * @param value    the expected value
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(double quantile, double value) {
        return hasSampleValue(Collections.emptyList(), quantile, da -> da.isEqualTo(Double.valueOf(value)));
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @param quantile    the quantile
     * @param value       the expected value
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(List<String> labelValues, double quantile, double value) {
        return hasSampleValue(labelValues, quantile, da -> da.isEqualTo(Double.valueOf(value)));
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @param quantile    the quantile
     * @param valueAssert assert for value, eg <code>da-&gt;da.isCloseTo(10.0, withinPercentage(20d))</code>
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(List<String> labelValues,
                               double quantile,
                               UnaryOperator<? super DoubleAssert> valueAssert) {
        List<String> extendedLabelValues = new ArrayList<>(labelValues.size() + 1);
        extendedLabelValues.addAll(labelValues);
        extendedLabelValues.add(Collector.doubleToGoString(quantile));
        return hasSampleValue(actual.name, extendedLabelValues, valueAssert);
    }
}
