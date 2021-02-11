package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector.MetricFamilySamples;
import io.prometheus.client.hotspot.VersionInfoExports;
import org.junit.Test;

import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.assertThat;
import static de.m3y.prometheus.assertj.MetricFamilySamplesAssert.labelValues;

/**
 * Example test for {@link VersionInfoExports}
 */
public class ExampleAssertTest {
    @Test
    public void testVersionInfoExports() {
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
                        ),
                        1d);
    }
}
