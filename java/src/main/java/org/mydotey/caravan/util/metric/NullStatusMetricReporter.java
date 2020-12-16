package org.mydotey.caravan.util.metric;

/**
 * Created by w.jian on 2016/9/12.
 */
public class NullStatusMetricReporter<T> implements MetricReporter<StatusMetric<T>> {

    @SuppressWarnings("rawtypes")
    private static final NullStatusMetricReporter INSTANCE = new NullStatusMetricReporter();

    @SuppressWarnings("unchecked")
    public static <T> NullStatusMetricReporter<T> getInstance() {
        return (NullStatusMetricReporter<T>) INSTANCE;
    }

    private NullStatusMetricReporter() {
    }

    @Override
    public void report(StatusMetric<T> metric) {

    }
}
