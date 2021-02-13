# AssertJ support for Prometheus Metrics

[![Maven Central](https://img.shields.io/maven-central/v/de.m3y.prometheus.assertj/assertj-prometheus.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22de.m3y.prometheus.assertj%22%20AND%20a%3A%22assertj-prometheus%22)

This library provides AssertJ support for [Prometheus Java Client](https://github.com/prometheus/client_java) metrics,
which simplifies testing your own (Java) exporters or own (Java) application natively exposing metrics.

## Download
Available on [Maven Central](https://repo1.maven.org/maven2/de/m3y/prometheus/assertj/assertj-prometheus/) (GAV: de.m3y.prometheus.assertj:assertj-prometheus:0.4). Add to your POM:
```xml
<dependency>
    <groupId>de.m3y.prometheus.assertj</groupId>
    <artifactId>assertj-prometheus</artifactId>
    <version>0.4</version>
    <scope>test</scope>
</dependency>
```

## Examples

A very simple example for [VersionInfoExports](https://github.com/prometheus/client_java/blob/master/simpleclient_hotspot/src/main/java/io/prometheus/client/hotspot/VersionInfoExports.java):
```java
VersionInfoExports versionInfoExports = new VersionInfoExports();

// Verify jvm_info
MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples(
        versionInfoExports.collect(), "jvm");
assertThat(mfs)
        .hasTypeOfInfo()
        .hasSampleLabelNames("vendor", "runtime", "version")
        .hasSampleValue(
                labelValues(
                        System.getProperty("java.runtime.name", "unknown"),
                        System.getProperty("java.vm.vendor", "unknown"),
                        System.getProperty("java.runtime.version", "unknown")
                ));
```

For further examples, have a look at the [tests](src/test/java/de/m3y/prometheus/assertj/).

### Helpers
Helpers for fetching a single MFS:
```java
Collector.MetricFamilySamples mfs;

// From default registry CollectorRegistry.defaultRegistry
mfs = MetricFamilySamplesUtils.getMetricFamilySamples("my_metric");

// From specific registry
mfs = MetricFamilySamplesUtils.getMetricFamilySamples( CollectorRegistry.defaultRegistry, "my_metric");

// From collector aka exporter
VersionInfoExports versionInfoExports = new VersionInfoExports();
mfs = MetricFamilySamplesUtils.getMetricFamilySamples(versionInfoExports.collect(), "jvm");
```

### Info
Example for Info:
```java
Info info = Info.build().name("testInfo").help("help")
        .labelNames("version", "vendor")
        .create().register();
...
Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples("testInfo");
assertThat(mfs)
        .hasType(INFO)
        .hasTypeOfInfo()
        .hasSampleSize(1)
        .hasSampleValue("A", "B"); // Checks if label values exist-
                                   // typically values are your build info version 
```

### Counter or Gauge
Example for a Gauge or Counter:
```java
Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples("my_metric");
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
```java
Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples("my_metric");
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
```java
Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples("my_metric");
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
```bash
mvn clean install
```

## Requirements

* JDK 8+

## License and Copyright

Licensed under [Apache 2.0 License](LICENSE)

Copyright 2018-2020 Marcel May and project contributors.
