# AssertJ support for Prometheus Metrics

This library provides AssertJ support for [Prometheus Java Client](https://github.com/prometheus/client_java) metrics,
which simplifies testing your own (Java) exporters or own (Java) application exposed metrics.

## Examples

## Counter or Gauge
```
Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
assertThat(mfs)
        .hasTypeOfGauge()
        .hasAnySamples()
        .hasSampleSize(12)
        .hasSampleLabelNames("job_type", "app_name", "status")
        .hasSampleValue(
                labelValues("A", "B", "C"),
                10
        )
        .hasSampleValue(
                labelValues("X", "Y", "Z"),
                da -> da.isCloseTo(10.0, withinPercentage(10d)) // AssertJ double asserts with 10% tolerance
        );
```

### Summary
A Summary contains sum and count sub metrics.

```
Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples("my_metric");
assertThat(mfs)
        .hasSampleSize(12)
        .hasSampleLabelNames("label_a")
        .hasTypeOfSummary() // Required for following, summary specific asserts
        .hasSampleValue(0.5 /* Quantile */, 10)
        .hasSampleValue(0.9 /* Quantile */, 20)
        .hasSampleCountValue(labelValues("value_a"), 2)
        .hasSampleSumValue(labelValues("value_a"), 40)
        .hasSampleCountValue(labelValues("value_b"), 2)
        .hasSampleSumValue(labelValues("value_b"), 40)
        ...
```

### Histogram
Histogram contains sum, count and bucket sub metrics.
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
        .hasSampleBucketValue(labelValues("value_a"), 20, 1)
        ...
        .hasSampleBucketValue(labelValues("value_b"), Double.POSITIVE_INFINITY, 2)
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

Copyright 2018+ Marcel May and project contributors.
