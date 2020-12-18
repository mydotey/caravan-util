package org.mydotey.caravan.util.ratelimiter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.mydotey.scf.util.PropertyKeyGenerator;
import org.mydotey.java.BooleanExtension;
import org.mydotey.scf.Property;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.filter.AbstractValueFilter;
import org.mydotey.scf.filter.PipelineValueFilter;
import org.mydotey.scf.type.string.StringInplaceConverter;
import org.mydotey.scf.type.string.StringToLongConverter;
import org.mydotey.util.CounterBuffer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
class DefaultRateLimiter implements RateLimiter {

    private String _rateLimiterId;
    private Property<String, Boolean> _enabledProperty;
    private Property<String, Long> _defaultRateLimitProperty;
    private Property<String, Map<String, Long>> _rateLimitMapProperty;
    private CounterBuffer<String> _counterBuffer;

    public DefaultRateLimiter(String rateLimiterId, StringProperties properties,
        RateLimiterConfig rateLimiterConfig) {
        _rateLimiterId = rateLimiterId;

        String propertyKey = PropertyKeyGenerator.generatePropertyKey(_rateLimiterId,
            RateLimiterConfig.ENABLED_PROPERTY_KEY);
        _enabledProperty = properties.getBooleanProperty(propertyKey, rateLimiterConfig.enabled());

        propertyKey = PropertyKeyGenerator.generatePropertyKey(_rateLimiterId,
            RateLimiterConfig.DEFAULT_RATE_LIMIT_PROPERTY_KEY);
        _defaultRateLimitProperty = properties.getLongProperty(propertyKey,
            rateLimiterConfig.rateLimitPropertyConfig());

        PipelineValueFilter<Long> rateLimitValueCorrector = new PipelineValueFilter<>(Arrays.asList(
            rateLimiterConfig.rateLimitPropertyConfig().toDefaultValueFilter(),
            rateLimiterConfig.rateLimitPropertyConfig().toRangeValueFilter()));
        propertyKey = PropertyKeyGenerator.generatePropertyKey(_rateLimiterId,
            RateLimiterConfig.RATE_LIMIT_MAP_PROPERTY_KEY);
        _rateLimitMapProperty = properties.getMapProperty(propertyKey, new HashMap<>(), StringInplaceConverter.DEFAULT,
            StringToLongConverter.DEFAULT, new AbstractValueFilter<Map<String, Long>>() {
                @Override
                public Map<String, Long> apply(Map<String, Long> v) {
                    Map<String, Long> result = new HashMap<>();
                    for (Map.Entry<String, Long> item : v.entrySet()) {
                        result.put(item.getKey(), rateLimitValueCorrector.apply(item.getValue()));
                    }
                    return result;
                }

                @Override
                public boolean equals(Object obj) {
                    return obj != null && this.getClass() == obj.getClass();
                }
            });

        _counterBuffer = new CounterBuffer<>(rateLimiterConfig.bufferConfig());
    }

    @Override
    public String rateLimiterId() {
        return _rateLimiterId;
    }

    @Override
    public boolean isRateLimited(String identity) {
        if (BooleanExtension.isFalse(_enabledProperty.getValue()))
            return false;

        _counterBuffer.increment(identity);
        if (_counterBuffer.get(identity) <= getRateLimit(identity))
            return false;

        _counterBuffer.decrement(identity);
        return true;
    }

    private long getRateLimit(String identity) {
        Map<String, Long> rateLimitMap = _rateLimitMapProperty.getValue();
        Long rateLimit = null;
        if (rateLimitMap != null)
            rateLimit = rateLimitMap.get(identity);

        if (rateLimit == null)
            rateLimit = _defaultRateLimitProperty.getValue();

        return rateLimit == null ? Long.MAX_VALUE : rateLimit.longValue();
    }

}
