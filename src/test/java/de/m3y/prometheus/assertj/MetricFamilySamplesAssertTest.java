package de.m3y.prometheus.assertj;


import io.prometheus.client.*;
import io.prometheus.client.Collector.MetricFamilySamples;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;

import static de.m3y.prometheus.assertj.CollectorRegistryUtils.getMetricFamilySamples;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.assertThat;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.labelValues;
import static io.prometheus.client.Collector.Type.*;
import static io.prometheus.client.CollectorRegistry.defaultRegistry;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MetricFamilySamplesAssertTest {
    @Before
    public void setUp() {
        defaultRegistry.clear(); // Reset between tests
    }

    @Test
    public void testHasTypeOf() {
        Counter counter = Counter.build().name("counter_metric").help("help")
                .create().register();
        MetricFamilySamples mfsCounter = getMetricFamilySamples("counter_metric");
        assertThat(mfsCounter).hasType(COUNTER).hasTypeOfCounter();
        expectAssertionError(() -> assertThat(mfsCounter).hasType(GAUGE));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfGauge());
        expectAssertionError(() -> assertThat(mfsCounter).hasType(SUMMARY));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfSummary());
        expectAssertionError(() -> assertThat(mfsCounter).hasType(HISTOGRAM));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfHistogram());

        Gauge gauge = Gauge.build().name("gauge_metric").help("help")
                .create().register();
        MetricFamilySamples mfsGauge = getMetricFamilySamples("gauge_metric");
        expectAssertionError(() -> assertThat(mfsGauge).hasType(COUNTER));
        expectAssertionError(() -> assertThat(mfsGauge).hasTypeOfCounter());
        expectAssertionError(() -> assertThat(mfsGauge).hasType(SUMMARY));
        expectAssertionError(() -> assertThat(mfsGauge).hasTypeOfSummary());
        expectAssertionError(() -> assertThat(mfsGauge).hasType(HISTOGRAM));
        expectAssertionError(() -> assertThat(mfsGauge).hasTypeOfHistogram());

        Summary summary = Summary.build().name("summary_metric").help("help")
                .create().register();
        MetricFamilySamples mfsSummary = getMetricFamilySamples("summary_metric");
        expectAssertionError(() -> assertThat(mfsSummary).hasType(COUNTER));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfCounter());
        expectAssertionError(() -> assertThat(mfsSummary).hasType(GAUGE));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfGauge());
        expectAssertionError(() -> assertThat(mfsSummary).hasType(HISTOGRAM));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfHistogram());

        Histogram histogram = Histogram.build().name("histogram_metric").help("help")
                .create().register();
        MetricFamilySamples mfsHistogram = getMetricFamilySamples("histogram_metric");
        expectAssertionError(() -> assertThat(mfsHistogram).hasType(COUNTER));
        expectAssertionError(() -> assertThat(mfsHistogram).hasTypeOfCounter());
        expectAssertionError(() -> assertThat(mfsHistogram).hasType(GAUGE));
        expectAssertionError(() -> assertThat(mfsHistogram).hasTypeOfGauge());
        expectAssertionError(() -> assertThat(mfsHistogram).hasType(SUMMARY));
        expectAssertionError(() -> assertThat(mfsHistogram).hasTypeOfSummary());
    }

    @Test
    public void testHasAnySample() {
        // Without labels
        Counter counter = Counter.build().name("testHasAnySample").help("help")
                .create().register();
        MetricFamilySamples mfsCounter = getMetricFamilySamples("testHasAnySample");
        assertThat(mfsCounter).hasAnySamples();

        // With labels
        Counter counterWithLabels = Counter.build().name("testHasAnySample_withLabels").help("help")
                .labelNames("label")
                .create().register();
        MetricFamilySamples mfsCounterWithLabels = getMetricFamilySamples("testHasAnySample_withLabels");
        expectAssertionError(() -> assertThat(mfsCounterWithLabels).hasAnySamples());
        counterWithLabels.labels("value").inc();

        assertThat(getMetricFamilySamples("testHasAnySample_withLabels"))  // Need to fetch again!
                .hasAnySamples();
    }

    @Test
    public void testHasSampleSize() {
        // Without labels
        Counter counter = Counter.build().name("testHasSampleSize").help("help")
                .create().register();
        MetricFamilySamples mfsCounter = getMetricFamilySamples("testHasSampleSize");
        assertThat(mfsCounter).hasSampleSize(1); // Without labels, there is always an initializing sample.

        // With labels
        Counter counterWithLabels = Counter.build().name("testHasSampleSize_withLabels").help("help")
                .labelNames("label")
                .create().register();
        assertThat(getMetricFamilySamples("testHasSampleSize_withLabels")).hasSampleSize(0);
        counterWithLabels.labels("a").inc();
        assertThat(getMetricFamilySamples("testHasSampleSize_withLabels")).hasSampleSize(1);
        // Same label value does not increase samples
        counterWithLabels.labels("a").inc();
        assertThat(getMetricFamilySamples("testHasSampleSize_withLabels")).hasSampleSize(1);
        // Different label value does increase samples
        counterWithLabels.labels("b").inc();
        assertThat(getMetricFamilySamples("testHasSampleSize_withLabels")).hasSampleSize(2);
    }

    @Test
    public void testHasSampleLabels() {
        // Without labels
        Counter counter = Counter.build().name("testHasSampleLabels").help("help")
                .create().register();
        MetricFamilySamples mfsCounter = getMetricFamilySamples("testHasSampleLabels");
        assertThat(mfsCounter).hasSampleLabelNames(); // No labels
        expectAssertionError(() -> assertThat(mfsCounter).hasSampleLabelNames("foo"));


        // With labels
        Counter counterWithLabels = Counter.build().name("testHasSampleLabels_withLabels").help("help")
                .labelNames("label")
                .create().register();
        counterWithLabels.labels("value").inc();

        final MetricFamilySamples mfsCounter_withLabel = getMetricFamilySamples("testHasSampleLabels_withLabels");
        assertThat(mfsCounter_withLabel).hasSampleLabelNames("label");
        expectAssertionError(() -> assertThat(mfsCounter_withLabel).hasSampleLabelNames());
        expectAssertionError(() -> assertThat(mfsCounter_withLabel).hasSampleLabelNames("foo"));
        expectAssertionError(() -> assertThat(mfsCounter_withLabel).hasSampleLabelNames("label", "foo"));
    }

    @Test
    public void testHasSampleValueWithoutLabels() {
        Counter counter = Counter.build().name("testHasSampleValueWithoutLabels").help("help")
                .create().register();
        MetricFamilySamples mfsCounter = getMetricFamilySamples("testHasSampleValueWithoutLabels");
        assertThat(mfsCounter)
                .hasTypeOfCounter()
                .hasSampleValue(0)
                .hasSampleValue(da -> da.isEqualTo(0));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter().hasSampleValue(5));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter().hasSampleValue(da -> da.isEqualTo(5)));
    }

    @Test
    public void testSummmaryHasSampleValueWithoutLabels() {
        Summary summary = Summary.build().name("testSummmaryHasSampleValueWithoutLabels").help("help")
                .create().register();
        MetricFamilySamples mfsSummary = getMetricFamilySamples("testSummmaryHasSampleValueWithoutLabels");
        assertThat(mfsSummary)
                .hasTypeOfSummary()
                .hasSampleCountValue(0)
                .hasSampleCountValue(da -> da.isEqualTo(0))
                .hasSampleSumValue(0)
                .hasSampleSumValue(da -> da.isEqualTo(0));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary().hasSampleCountValue(5));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary().hasSampleCountValue(da -> da.isEqualTo(5)));
    }

    @Test
    public void testHistogramHasSampleValueWithoutLabels() {
        Histogram histogram = Histogram.build().name("testHistogramHasSampleValueWithoutLabels").help("help")
                .buckets(1, 5)
                .create().register();
        MetricFamilySamples mfsHistogram = getMetricFamilySamples("testHistogramHasSampleValueWithoutLabels");
        assertThat(mfsHistogram)
                .hasTypeOfHistogram()
                .hasSampleCountValue(0)
                .hasSampleCountValue(da -> da.isEqualTo(0))
                .hasSampleSumValue(0)
                .hasSampleSumValue(da -> da.isEqualTo(0))
                .hasSampleBucketValue(1, 0)
                .hasSampleBucketValue(1, da -> da.isEqualTo(0))
                .hasSampleBucketValue(5, 0)
                .hasSampleBucketValue(5, da -> da.isEqualTo(0));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleCountValue(5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleCountValue(da -> da.isEqualTo(5)));

        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleSumValue(5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleSumValue(da -> da.isEqualTo(5)));

        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(1, 5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(1, da -> da.isEqualTo(5)));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(2, 0));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(2, da -> da.isEqualTo(0)));
    }

    @Test
    public void testHasSampleValueWithLabels() {
        Counter counter = Counter.build().name("testHasSampleValueWithLabels").help("help")
                .labelNames("label")
                .create().register();
        counter.labels("foo").inc();

        MetricFamilySamples mfsCounter = getMetricFamilySamples("testHasSampleValueWithLabels");
        assertThat(mfsCounter)
                .hasTypeOfCounter()
                .hasSampleValue(labelValues("foo"), 1)
                .hasSampleValue(labelValues("foo"), da -> da.isEqualTo(1));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter()
                .hasSampleValue(1));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter()
                .hasSampleValue(da -> da.isEqualTo(1)));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter()
                .hasSampleValue(labelValues("fooooo"), 1));
        expectAssertionError(() -> assertThat(mfsCounter).hasTypeOfCounter()
                .hasSampleValue(labelValues("foo"), 2));
    }

    @Test
    public void testSummmaryHasSampleValueWithLabels() {
        Summary summary = Summary.build().name("testSummmaryHasSampleValueWithtLabels").help("help")
                .labelNames("label")
                .create().register();
        summary.labels("foo").observe(42d);
        MetricFamilySamples mfsSummary = getMetricFamilySamples("testSummmaryHasSampleValueWithtLabels");
        assertThat(mfsSummary)
                .hasTypeOfSummary()
                .hasSampleLabelNames("label")
                .hasSampleCountValue(labelValues("foo"), 1)
                .hasSampleCountValue(labelValues("foo"), da -> da.isEqualTo(1))
                .hasSampleSumValue(labelValues("foo"), 42)
                .hasSampleSumValue(labelValues("foo"), da -> da.isEqualTo(42));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary()
                .hasSampleCountValue(labelValues("foo"), 5));
        expectAssertionError(() -> assertThat(mfsSummary).hasTypeOfSummary()
                .hasSampleCountValue(labelValues("foo"), da -> da.isEqualTo(5)));
    }

    @Test
    public void testHistogramHasSampleValueWithLabels() {
        Histogram histogram = Histogram.build().name("testHistogramHasSampleValueWithLabels").help("help")
                .labelNames("label")
                .buckets(1, 5)
                .create().register();
        histogram.labels("foo").observe(42);

        MetricFamilySamples mfsHistogram = getMetricFamilySamples("testHistogramHasSampleValueWithLabels");
        assertThat(mfsHistogram)
                .hasTypeOfHistogram()
                .hasSampleLabelNames("label")
                .hasSampleCountValue(labelValues("foo"), 1)
                .hasSampleCountValue(labelValues("foo"), da -> da.isEqualTo(1))
                .hasSampleSumValue(labelValues("foo"), 42)
                .hasSampleSumValue(labelValues("foo"), da -> da.isEqualTo(42))
                .hasSampleBucketValue(labelValues("foo"), 1, 0)
                .hasSampleBucketValue(labelValues("foo"), 1, da -> da.isEqualTo(0))
                .hasSampleBucketValue(labelValues("foo"), 5, 0)
                .hasSampleBucketValue(labelValues("foo"), 5, da -> da.isEqualTo(0))
                .hasSampleBucketValue(labelValues("foo"), Double.POSITIVE_INFINITY, 1)
                .hasSampleBucketValue(labelValues("foo"), Double.POSITIVE_INFINITY, da -> da.isEqualTo(1));

        // Should fail:
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleCountValue(labelValues("foo"), 5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleCountValue(labelValues("foo"), da -> da.isEqualTo(5)));

        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleSumValue(labelValues("foo"), 5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleSumValue(labelValues("foo"), da -> da.isEqualTo(5)));

        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(labelValues("foo"), 1, 5));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(labelValues("foo"), 1, da -> da.isEqualTo(5)));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(labelValues("foo"), 2, 0));
        expectAssertionError(() -> assertThat(mfsHistogram)
                .hasTypeOfHistogram().hasSampleBucketValue(labelValues("foo"), 2, da -> da.isEqualTo(0)));
    }


    @Test
    public void testCounter() {
        Counter counter = Counter.build().name("testCounter").help("help")
                .create().register();
        counter.inc(20);

        MetricFamilySamples mfs = getMetricFamilySamples("testCounter");
        assertThat(mfs)
                .hasType(COUNTER)
                .hasTypeOfCounter()
                .hasSampleValue(20);
    }

    @Test
    public void testCounterWithLabels() {
        Counter counter = Counter.build().name("testCounterWithLabels").help("help")
                .labelNames("label_a", "label_b")
                .create().register();
        counter.labels("value_a", "value_b").inc();

        final MetricFamilySamples mfs = getMetricFamilySamples("testCounterWithLabels");
        assertThat(mfs)
                .hasTypeOfCounter()
                .hasSampleValue(
                        labelValues("value_a", "value_b"),
                        1d /* one count */
                );
    }

    @Test
    public void testGauge() {
        Gauge gauge = Gauge.build().name("testGauge").help("help")
                .create().register();
        gauge.inc(42);

        final MetricFamilySamples mfs = getMetricFamilySamples("testGauge");
        assertThat(mfs)
                .hasType(GAUGE)
                .hasTypeOfGauge()
                .hasSampleValue(42);
    }

    @Test
    public void testGaugeWithLabels() {
        Gauge gauge = Gauge.build().name("testGaugeWithLabels").help("help")
                .labelNames("label_ga")
                .create().register();
        gauge.labels("value_ga").inc(42);

        final MetricFamilySamples mfs = getMetricFamilySamples("testGaugeWithLabels");
        assertThat(mfs)
                .hasTypeOfGauge()
                .hasSampleValue(
                        labelValues("value_ga"),
                        42d
                );
    }

    @Test
    public void testSummary() {
        Summary summary = Summary.build().name("testSummary").help("help")
                .create().register();
        summary.observe(123);

        final MetricFamilySamples mfs = getMetricFamilySamples("testSummary");
        assertThat(mfs)
                .hasType(Collector.Type.SUMMARY)
                .hasTypeOfSummary()
                .hasSampleCountValue(1)
                .hasSampleSumValue(123);
    }

    @Test
    public void testSummaryWithLabels() {
        Summary summary = Summary.build().name("testSummaryWithLabels").help("help")
                .labelNames("label_sa")
                .create().register();
        summary.labels("value_sa").observe(1234);

        final MetricFamilySamples mfs = getMetricFamilySamples("testSummaryWithLabels");
        assertThat(mfs)
                .hasSampleLabelNames("label_sa")
                .hasTypeOfSummary()
                .hasSampleCountValue(
                        labelValues("value_sa"),
                        1)
                .hasSampleSumValue(
                        labelValues("value_sa"),
                        1234);
    }

    @Test
    public void testHistogram() {
        Histogram histogram = Histogram.build().name("testHistogram").help("help")
                .buckets(10, 20, 30)
                .create().register();
        histogram.observe(10);
        histogram.observe(20);
        histogram.observe(20);
        histogram.observe(30);

        final MetricFamilySamples mfs = getMetricFamilySamples("testHistogram");
        assertThat(mfs)
                .hasSampleSize(6)
                .hasType(Collector.Type.HISTOGRAM)
                .hasTypeOfHistogram()
                .hasSampleBucketValue(10, 1)
                .hasSampleBucketValue(20, 3)
                .hasSampleBucketValue(30, 4)
                .hasSampleCountValue(4)
                .hasSampleSumValue(80)
        ;
    }

    @Test
    public void testExample() {
        // Setup example metric and samples
        Histogram histogram = Histogram.build().name("testExample").help("help")
                .buckets(10, 20, 30)
                .labelNames("label_a")
                .create().register();
        histogram.labels("value_a").observe(10);
        histogram.labels("value_a").observe(30);
        // Different label
        histogram.labels("value_b").observe(20);
        histogram.labels("value_b").observe(20);

        // Verify
        final MetricFamilySamples mfs = getMetricFamilySamples("testExample");
        assertThat(mfs)
                .hasSampleSize(12)
                .hasSampleLabelNames("label_a")
                // Generic and equivalent to next line,
                // but not returning specialisation for Summary or Histogram
                .hasType(Collector.Type.HISTOGRAM)
                // Same as above. Returns specialization for Histogram
                // enabling asserting bucket-, count- and sum-values
                .hasTypeOfHistogram()
                .hasSampleBucketValue(labelValues("value_a"), 10, 1) // Histogram bucket assertions
                .hasSampleBucketValue(labelValues("value_a"), 20, 1)
                .hasSampleBucketValue(labelValues("value_a"), 30, 2)
                .hasSampleBucketValue(labelValues("value_b"), 10, 0) // Histogram bucket assertions
                .hasSampleBucketValue(labelValues("value_b"), 20, 2)
                .hasSampleBucketValue(labelValues("value_b"), 30, 2)
                .hasSampleBucketValue(labelValues("value_b"), Double.POSITIVE_INFINITY, 2)
                .hasSampleBucketValue(labelValues("value_b"), Double.POSITIVE_INFINITY, 2)
                .hasSampleCountValue(labelValues("value_a"), 2) // Summary or Histogram count assertions
                .hasSampleCountValue(labelValues("value_b"), 2) // Summary or Histogram count assertions
                .hasSampleSumValue(labelValues("value_a"), 40) // Summary or Histogram sum assertions
                .hasSampleSumValue(labelValues("value_b"), 40) // Summary or Histogram sum assertions
        ;
    }

    static void expectAssertionError(ThrowableAssert.ThrowingCallable throwingCallable) {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(throwingCallable);
    }
}
