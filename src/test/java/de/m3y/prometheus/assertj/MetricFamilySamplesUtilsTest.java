package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.hotspot.VersionInfoExports;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MetricFamilySamplesUtilsTest {
    @Test
    public void testGetMetricFamilySamplesFromDefaultRegistry() {
        String name = "testGetMetricFamilySamplesFromDefaultRegistry";
        Counter counter = Counter.build()
                .name(name).help("example counter")
                .create().register();

        Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples(name);
        assertThat(mfs).isNotNull();
        assertThat(mfs.name).isEqualTo(name);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> MetricFamilySamplesUtils.getMetricFamilySamples("nonexistent MFS name"));
    }

    @Test
    public void testGetMetricFamilySamples() {
        CollectorRegistry collectorRegistry = new CollectorRegistry();
        String name = "testGetMetricFamilySamples";
        Counter counter = Counter.build()
                .name(name).help("example counter")
                .create().register(collectorRegistry);

        Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples(collectorRegistry, name);
        assertThat(mfs).isNotNull();
        assertThat(mfs.name).isEqualTo(name);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> MetricFamilySamplesUtils.getMetricFamilySamples(collectorRegistry,
                        "nonexistent MFS name"));
    }

    @Test
    public void testGetMetricFamilySamplesWithCollector() {
        VersionInfoExports versionInfoExports = new VersionInfoExports();
        String name = "jvm";
        Collector.MetricFamilySamples mfs = MetricFamilySamplesUtils.getMetricFamilySamples(versionInfoExports.collect(), name);
        assertThat(mfs).isNotNull();
        assertThat(mfs.name).isEqualTo(name);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MetricFamilySamplesUtils.getMetricFamilySamples(versionInfoExports.collect(),
                        "nonexistent MFS name"));
    }
}
