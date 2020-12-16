package org.mydotey.caravan.util.safelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mydotey.scf.Property;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.type.TypeConverter;
import org.mydotey.scf.util.PropertyKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
class DefaultSafeList<T> implements SafeList<T> {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultSafeList.class);

    private String _safeListId;
    private SafeListChecker<T> _checker;

    private Property<String, Boolean> _enabledProperty;
    private Property<String, List<T>> _listProperty;

    public DefaultSafeList(String safeListId, TypeConverter<String, T> valueParser,
        StringProperties properties, SafeListConfig<T> config) {
        _safeListId = safeListId;
        _checker = config.checker();

        String propertyKey = PropertyKeyGenerator.generatePropertyKey(_safeListId, SafeListConfig.ENABLED_PROPERTY_KEY);
        _enabledProperty = properties.getBooleanProperty(propertyKey, config.enabled());

        propertyKey = PropertyKeyGenerator.generateKey(_safeListId, SafeListConfig.LIST_PROPERTY_KEY);
        _listProperty = properties.<T>getListProperty(propertyKey,
            config.list() == null ? new ArrayList<T>() : config.list(), valueParser);
    }

    @Override
    public String safeListId() {
        return _safeListId;
    }

    @Override
    public List<T> list() {
        return Collections.unmodifiableList(_listProperty.getValue());
    }

    @Override
    public boolean check(T item) {
        if (!_enabledProperty.getValue())
            return true;

        try {
            return _checker.check(_listProperty.getValue(), item);
        } catch (Throwable ex) {
            _logger.error("Safe list failed to check.", ex);
            return true;
        }
    }

}
