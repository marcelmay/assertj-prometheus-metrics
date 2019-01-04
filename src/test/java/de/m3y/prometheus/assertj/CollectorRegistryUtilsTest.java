package de.m3y.prometheus.assertj;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 *
 */
public class CollectorRegistryUtilsTest {
    @Test
    public void testGetMetricFamilySamplesFromDefaultRegistry() {
        String name = "testGetMetricFamilySamplesFromDefaultRegistry";
        Counter counter = Counter.build()
                .name(name).help("example counter")
                .create().register();

        Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples(name);
        assertThat(mfs).isNotNull();
        assertThat(mfs.name).isEqualTo(name);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> CollectorRegistryUtils.getMetricFamilySamples("nonexistent MFS name"));
    }

    @Test
    public void testGetMetricFamilySamples() {
        CollectorRegistry collectorRegistry = new CollectorRegistry();
        String name = "testGetMetricFamilySamples";
        Counter counter = Counter.build()
                .name(name).help("example counter")
                .create().register(collectorRegistry);

        Collector.MetricFamilySamples mfs = CollectorRegistryUtils.getMetricFamilySamples(collectorRegistry, name);
        assertThat(mfs).isNotNull();
        assertThat(mfs.name).isEqualTo(name);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> CollectorRegistryUtils.getMetricFamilySamples(collectorRegistry, "nonexistent MFS name"));
    }
}
