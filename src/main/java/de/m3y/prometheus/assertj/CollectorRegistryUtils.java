package de.m3y.prometheus.assertj;

import java.util.Collections;
import java.util.List;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

/**
 * Helpers around CollectorRegistry handling.
 */
public class CollectorRegistryUtils {
    /**
     * Prevent instantiation.
     */
    private CollectorRegistryUtils() {
        // Nothing
    }

    /**
     * Gets a copy of MetricFamilySamples by its name from the default collector registry.
     *
     * @param name the name of the MFS.
     * @return the MFS found, or throws IllegalArgumentException if no MFS found.
     * @see io.prometheus.client.CollectorRegistry#defaultRegistry
     */
    public static Collector.MetricFamilySamples getMetricFamilySamples(String name) {
        return getMetricFamilySamples(CollectorRegistry.defaultRegistry, name);
    }

    /**
     * Gets a copy of MetricFamilySamples by its name from the given collector registry.
     *
     * @param collectorRegistry the collector registry.
     * @param name              the name of the MFS.
     * @return the MFS found, or throws IllegalArgumentException if no MFS found.
     */
    public static Collector.MetricFamilySamples getMetricFamilySamples(CollectorRegistry collectorRegistry, String name) {
        return getMetricFamilySamples(Collections.list(collectorRegistry.metricFamilySamples()), name);
    }

    /**
     * Gets a copy of MetricFamilySamples by its name from the given list of MetricFamiliySample.
     *
     * @param metricFamilySamples  a list of MFS, eg provided by a {@link Collector#collect()}
     * @param name              the name of the MFS.
     * @return the MFS found, or throws IllegalArgumentException if no MFS found.
     */
    public static Collector.MetricFamilySamples getMetricFamilySamples(List<Collector.MetricFamilySamples> metricFamilySamples, String name) {
        return metricFamilySamples.stream().filter(mfs -> name.equals(mfs.name))
                .findAny().orElseThrow(() -> new IllegalArgumentException("No MetricFamilySamples found by name " + name));
    }
}
