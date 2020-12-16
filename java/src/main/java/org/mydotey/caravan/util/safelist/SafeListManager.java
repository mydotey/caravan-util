package org.mydotey.caravan.util.safelist;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.java.ObjectExtension;
import org.mydotey.java.StringExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.type.string.StringInplaceConverter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SafeListManager<T> {

    public static SafeListManager<String> newManager(StringProperties properties) {
        return new SafeListManager<>(StringExtension.EMPTY,
            new SafeListManagerConfig<>(properties, StringInplaceConverter.DEFAULT));
    }

    private String _managerId;
    private SafeListManagerConfig<T> _config;
    private ConcurrentHashMap<String, SafeList<T>> _listMap = new ConcurrentHashMap<>();

    public SafeListManager(String managerId, SafeListManagerConfig<T> config) {
        ObjectExtension.requireNonNull(managerId, "managerId");
        ObjectExtension.requireNonNull(config, "config");
        _managerId = managerId;
        _config = config;
    }

    public String managerId() {
        return _managerId;
    }

    public SafeListManagerConfig<T> config() {
        return _config;
    }

    public Collection<SafeList<T>> lists() {
        return Collections.unmodifiableCollection(_listMap.values());
    }

    public SafeList<T> getList(String listId, final SafeListConfig<T> config) {
        ObjectExtension.requireNonBlank(listId, "listId");
        ObjectExtension.requireNonNull(config, "config");

        final String trimmedListId = StringExtension.trim(listId);
        return _listMap.computeIfAbsent(trimmedListId,
            k -> new DefaultSafeList<T>(trimmedListId, _config.valueParser(), _config.properties(), config));
    }

}
