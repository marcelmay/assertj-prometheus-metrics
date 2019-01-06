# AssertJ support for Prometheus Metrics

[![Maven Central](https://img.shields.io/maven-central/v/de.m3y.prometheus.assertj/assertj-prometheus.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22de.m3y.prometheus.assertj%22%20AND%20a%3A%22assertj-prometheus%22)

This library provides AssertJ support for [Prometheus Java Client](https://github.com/prometheus/client_java) metrics,
which simplifies testing your own (Java) exporters or own (Java) application exposed metrics.

Available on [Maven Central](https://repo1.maven.org/maven2/de/m3y/prometheus/assertj/assertj-prometheus/) (GAV: de.m3y.prometheus.assertj:assertj-prometheus:0.1)

## Examples

## Counter or Gauge
Example for a Gauge or Counter:
```
Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
assertThat(mfs)
        .hasTypeOfGauge() // For a Counter: .hasTypeOfCounter()
        .hasSampleLabelNames("job_type", "app_name", "status")
        .hasSampleValue(
                labelValues("A", "B", "C"),
                10d
        )
        .hasSampleValue(
                labelValues("X", "Y", "Z"),
                da -> da.isCloseTo(10.0, withinPercentage(10d)) // AssertJ double asserts with 10% tolerance
        );
```

### Summary
Example for a Summary with sum, count and quantiles:
```
Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
assertThat(mfs)
        .hasSampleSize(12)
        .hasSampleLabelNames("label_a")
        .hasTypeOfSummary() // Required for following, summary specific asserts
        .hasSampleCountValue(labelValues("value_a"), 2)
        .hasSampleSumValue(labelValues("value_a"), 40)
        .hasSampleValue(0.5 /* Quantile */, 10)
        .hasSampleValue(0.9 /* Quantile */, 20)
        ...
```

### Histogram
Example for a Histogram with sum, count and buckets:
```
Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
assertThat(mfs)
        .hasSampleSize(12)
        .hasSampleLabelNames("label_a")
        .hasTypeOfHistogram() // Required for following, histogram specific asserts
        .hasSampleCountValue(labelValues("value_a"), 2)
        .hasSampleSumValue(labelValues("value_a"), 40)
        .hasSampleCountValue(labelValues("value_b"), 2)
        .hasSampleSumValue(labelValues("value_b"), 40)
        .hasSampleBucketValue(labelValues("value_a"), 10, 1) // Histogram bucket assertions
        ...
        .hasSampleBucketValue(labelValues("value_b"), Double.POSITIVE_INFINITY, 2)
```

## Building
```
mvn clean install
```

## Requirements

* JDK 8+

## License and Copyright

Licensed under [Apache 2.0 License](LICENSE)

Copyright 2018-2019 Marcel May and project contributors.
