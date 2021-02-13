package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;
import org.assertj.core.api.AbstractDoubleAssert;

import java.util.Arrays;
import java.util.List;

/**
 * AssertJ support for <code>{@link Collector.MetricFamilySamples}</code> extension for info,
 * as base for Info metrics.
 */
public abstract class AbstractMetricFamilySamplesInfoAssert
        <SELF extends AbstractMetricFamilySamplesInfoAssert<SELF>> extends
        AbstractMetricFamilySamplesAssert<SELF> {
    protected AbstractMetricFamilySamplesInfoAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(String... labelValues) {
        return hasSampleValue(actual.name, Arrays.asList(labelValues), AbstractDoubleAssert::isOne);
    }

    /**
     * Verifies the recorded sample value.
     *
     * @param labelValues the expected value labels
     * @return {@code this} assertion object.
     */
    public SELF hasSampleValue(List<String> labelValues) {
        return hasSampleValue(actual.name, labelValues, AbstractDoubleAssert::isOne);
    }
}
