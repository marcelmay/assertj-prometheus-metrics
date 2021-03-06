package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;
import org.assertj.core.api.DoubleAssert;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * AssertJ support for <code>{@link Collector.MetricFamilySamples}</code> extension for sum and count,
 * as base for Summary and Histogram metrics.
 */
public abstract class AbstractMetricFamilySamplesSumAndCountAssert
        <SELF extends AbstractMetricFamilySamplesSumAndCountAssert<SELF>> extends
        AbstractMetricFamilySamplesAssert<SELF> {
    protected AbstractMetricFamilySamplesSumAndCountAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }

    /**
     * Verifies the sum value for a Histogram or Summary type metric.
     *
     * @param value the expected sum value.
     * @return {@code this} assertion object.
     */
    public SELF hasSampleSumValue(double value) {
        return hasSampleSumValue(da -> da.isEqualTo(value));
    }

    /**
     * Verifies the sum value for a Histogram or Summary type metric.
     *
     * @param valueAssert the expected sum value.
     * @return {@code this} assertion object.
     * @see #hasSampleSumValue(double)
     */
    public SELF hasSampleSumValue(UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleSumValue(Collections.emptyList(), valueAssert);
    }

    /**
     * Verifies the sum value for a Histogram or Summary type metric.
     *
     * @param labelValues the expected label values.
     * @param value       the expected sum value.
     * @return {@code this} assertion object.
     * @see #hasSampleSumValue(List, UnaryOperator)
     */
    public SELF hasSampleSumValue(List<String> labelValues, double value) {
        return hasSampleSumValue(labelValues, da -> da.isEqualTo(value));
    }

    /**
     * Verifies the sum value for a Histogram or Summary type metric.
     *
     * @param labelValues the expected label values.
     * @param valueAssert the expected sum value.
     * @return {@code this} assertion object.
     * @see #hasSampleSumValue(List, double)
     */
    public SELF hasSampleSumValue(List<String> labelValues,
                                  UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(actual.name + "_sum", labelValues, valueAssert);
    }

    /**
     * Verifies the count value for a Histogram or Summary type metric.
     *
     * @param value the expected count value.
     * @return {@code this} assertion object.
     * @see #hasSampleCountValue(UnaryOperator)
     */
    public SELF hasSampleCountValue(double value) {
        return hasSampleCountValue(da -> da.isEqualTo(value));
    }

    /**
     * Verifies the count value for a Histogram or Summary type metric.
     *
     * @param valueAssert the expected count value.
     * @return {@code this} assertion object.
     */
    public SELF hasSampleCountValue(UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleCountValue(Collections.emptyList(), valueAssert);
    }

    /**
     * Verifies the count value for a Histogram or Summary type metric.
     *
     * @param labelValues the expected label values.
     * @param value       the expected count value.
     * @return {@code this} assertion object.
     * @see #hasSampleCountValue(UnaryOperator)
     */
    public SELF hasSampleCountValue(List<String> labelValues, double value) {
        return hasSampleCountValue(labelValues, da -> da.isEqualTo(value));
    }

    /**
     * Verifies the count value for a Histogram or Summary type metric.
     *
     * @param labelValues the expected label values.
     * @param valueAssert the expected count value.
     * @return {@code this} assertion object.
     * @see #hasSampleCountValue(UnaryOperator)
     */
    public SELF hasSampleCountValue(
            List<String> labelValues,
            UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(actual.name + "_count", labelValues, valueAssert);
    }

    /**
     * Verifies the _created value for a Histogram, Summary or Count type metric.
     *
     * @param value the expected _created value.
     * @return {@code this} assertion object.
     */
    public SELF hasSampleCreatedValue(double value) {
        return hasSampleCreatedValue(da -> da.isEqualTo(value));
    }

    /**
     * Verifies the _created value for a Histogram, Summary or Count type metric.
     *
     * @param valueAssert the expected _created value.
     * @return {@code this} assertion object.
     * @see #hasSampleCreatedValue(UnaryOperator)
     */
    public SELF hasSampleCreatedValue(UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleCreatedValue(Collections.emptyList(), valueAssert);
    }

    /**
     * Verifies the _created value for a Histogram, Summary or Count type metric.
     *
     * @param labelValues the expected label values.
     * @param value       the expected _created value.
     * @return {@code this} assertion object.
     * @see #hasSampleCreatedValue(UnaryOperator)
     */
    public SELF hasSampleCreatedValue(List<String> labelValues, double value) {
        return hasSampleCreatedValue(labelValues, da -> da.isEqualTo(value));
    }

    /**
     * Verifies the _created value for a Histogram, Summary or Count type metric.
     *
     * @param labelValues the expected label values.
     * @param valueAssert the expected created value.
     * @return {@code this} assertion object.
     * @see #hasSampleCreatedValue(UnaryOperator)
     */
    public SELF hasSampleCreatedValue(List<String> labelValues, UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleValue(actual.name + "_created", labelValues, valueAssert);
    }
}
