package org.mydotey.caravan.util.metric;

/**
 * Created by w.jian on 2016/9/9.
 */
public interface StatusMetric<T> extends Metric {

    T getStatus();
}
