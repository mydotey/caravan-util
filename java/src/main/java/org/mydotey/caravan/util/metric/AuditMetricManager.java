package org.mydotey.caravan.util.metric;

/**
 * Created by w.jian on 2016/9/12.
 */
public interface AuditMetricManager extends MetricManager<AuditMetric> {
    AuditMetric getMetric(String metricId, MetricConfig metricConfig);
}
