package de.m3y.prometheus.assertj;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import io.prometheus.client.Collector;
import org.assertj.core.api.DoubleAssert;

/**
 * AssertJ support for <code>{@link Collector.MetricFamilySamples}</code> extension for sum and count,
 * as base for Summary and Histogram metrics.
 */
public abstract class AbstractMetricFamilySamplesCounterAndGaugeAssert
        <SELF extends AbstractMetricFamilySamplesCounterAndGaugeAssert<SELF>> extends
        AbstractMetricFamilySamplesAssert<SELF> {
    protected AbstractMetricFamilySamplesCounterAndGaugeAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param valueAssert assert for value, eg <code>da-&gt;da.isCloseTo(10.0, withinPercentage(20d))</code>
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(Collections.emptyList(), valueAssert);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param value the expected value
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(double value) {
        return hasSampleValue(Collections.emptyList(), da -> da.isEqualTo(value));
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @param value       the expected value
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(List<String> labelValues,
                               double value) {
        return hasSampleValue(labelValues, da -> da.isEqualTo(value));
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @param valueAssert assert for value, eg <code>da-&gt;da.isCloseTo(10.0, withinPercentage(20d))</code>
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(List<String> labelValues,
                               UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(actual.name, labelValues, valueAssert);
    }
}
