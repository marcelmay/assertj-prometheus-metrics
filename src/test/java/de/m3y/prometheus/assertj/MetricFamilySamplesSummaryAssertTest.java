package de.m3y.prometheus.assertj;


import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.Summary;
import org.junit.Before;
import org.junit.Test;

import static de.m3y.prometheus.assertj.MetricFamilySamplesUtils.getMetricFamilySamples;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.assertThat;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.labelValues;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssertTest.expectAssertionError;
import static io.prometheus.client.CollectorRegistry.defaultRegistry;

public class MetricFamilySamplesSummaryAssertTest {
    @Before
    public void setUp() {
        defaultRegistry.clear(); // Reset between tests
    }

    @Test
    public void testSummaryHasSampleValue() {
        Summary summary = Summary.build().name("testSummaryHasSampleValue").help("help")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .quantile(0.99, 0.001)
                .create().register();
        MetricFamilySamples mfsSummary = getMetricFamilySamples("testSummaryHasSampleValue");
        assertThat(mfsSummary)
                .hasTypeOfSummary()
                .hasSampleValue(0.5, Double.NaN)
                .hasSampleValue(0.9, Double.NaN)
                .hasSampleValue(0.99, Double.NaN)
                .hasSampleCountValue(0)
                .hasSampleCountValue(da -> da.isEqualTo(0))
                .hasSampleSumValue(0)
                .hasSampleSumValue(da -> da.isEqualTo(0));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary().hasSampleValue(0.1, 5));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary().hasSampleValue(0.5, da -> da.isEqualTo(5)));

        summary.observe(10);
        summary.observe(10);
        summary.observe(10);
        summary.observe(20);
        summary.observe(20);

        MetricFamilySamples mfsSummary2 = getMetricFamilySamples("testSummaryHasSampleValue");
        assertThat(mfsSummary2)
                .hasTypeOfSummary()
                .hasSampleValue(0.5, 10)
                .hasSampleValue(0.9, 20)
                .hasSampleValue(0.99, 20)
                .hasSampleCountValue(5)
                .hasSampleCountValue(da -> da.isEqualTo(5))
                .hasSampleSumValue(70)
                .hasSampleSumValue(da -> da.isEqualTo(70));
    }

    @Test
    public void testSummaryHasSampleValueWithLabel() {
        Summary summary = Summary.build().name("testSummaryHasSampleValueWithLabel").help("help")
                .quantile(0.5, 0.05)
                .quantile(0.9, 0.01)
                .quantile(0.99, 0.001)
                .labelNames("label")
                .create().register();

        summary.labels("A").observe(10);
        summary.labels("A").observe(10);
        summary.labels("A").observe(10);
        summary.labels("A").observe(20);
        summary.labels("A").observe(20);

        MetricFamilySamples mfsSummary = getMetricFamilySamples("testSummaryHasSampleValueWithLabel");

        assertThat(mfsSummary)
                .hasTypeOfSummary()
                .hasSampleValue(labelValues("A"), 0.5, 10)
                .hasSampleValue(labelValues("A"), 0.9, 20)
                .hasSampleValue(labelValues("A"), 0.99, 20)
                .hasSampleCountValue(labelValues("A"), 5)
                .hasSampleCountValue(labelValues("A"), da -> da.isEqualTo(5))
                .hasSampleSumValue(labelValues("A"), 70)
                .hasSampleSumValue(labelValues("A"), da -> da.isEqualTo(70));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary()
                .hasSampleValue(labelValues("Nonexistent"), 0.1, 5));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary()
                .hasSampleValue(labelValues("A"), 0.1, 5));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary()
                .hasSampleValue(labelValues("A"), 0.5, da -> da.isEqualTo(5)));

    }
}
