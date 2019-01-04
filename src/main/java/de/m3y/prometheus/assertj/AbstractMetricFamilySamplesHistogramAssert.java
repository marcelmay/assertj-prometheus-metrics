package de.m3y.prometheus.assertj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

import io.prometheus.client.Collector;
import org.assertj.core.api.DoubleAssert;

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
 *
 * @see MetricFamilySamplesAssert#assertThat(Collector.MetricFamilySamples)
 * @see MetricFamilySamplesAssert#hasTypeOfHistogram()
 */
public abstract class AbstractMetricFamilySamplesHistogramAssert
        <SELF extends AbstractMetricFamilySamplesHistogramAssert<SELF>> extends
        AbstractMetricFamilySamplesSumAndCountAssert<SELF> {
    protected AbstractMetricFamilySamplesHistogramAssert(Collector.MetricFamilySamples actual) {
        super(actual);
    }

    public SELF hasSampleBucketValue(double le, double value) {
        return hasSampleBucketValue(le, da -> da.isEqualTo(value));
    }

    public SELF hasSampleBucketValue(double le,
                                     UnaryOperator<? super DoubleAssert> valueAssert) {
        return hasSampleBucketValue(Collections.emptyList(), le, valueAssert);
    }

    public SELF hasSampleBucketValue(List<String> labelValues, double le, double value) {
        return hasSampleBucketValue(labelValues, le, da -> da.isEqualTo(value));
    }

    public SELF hasSampleBucketValue(List<String> labelValues,
                                     double le,
                                     UnaryOperator<? super DoubleAssert> valueAssert) {
        List<String> extendedLabelValues = new ArrayList<>(labelValues.size() + 1);
        extendedLabelValues.addAll(labelValues);
        extendedLabelValues.add(Collector.doubleToGoString(le));

        return hasSampleValue(actual.name + "_bucket", extendedLabelValues, valueAssert);
    }
}
