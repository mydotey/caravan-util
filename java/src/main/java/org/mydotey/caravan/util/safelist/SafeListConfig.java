package org.mydotey.caravan.util.safelist;

import java.util.ArrayList;
import java.util.List;

import org.mydotey.java.ObjectExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SafeListConfig<T> {

    public static final String ENABLED_PROPERTY_KEY = "enabled";
    public static final String LIST_PROPERTY_KEY = "list";

    private SafeListChecker<T> _checker;
    private boolean _enabled;
    private List<T> _list;

    public SafeListConfig(SafeListChecker<T> checker) {
        this(checker, false, new ArrayList<T>());
    }

    public SafeListConfig(SafeListChecker<T> checker, boolean enabled, List<T> list) {
        ObjectExtension.requireNonNull(checker, "checker");
        ObjectExtension.requireNonNull(list, "list");

        _checker = checker;
        _enabled = enabled;
        _list = list;
    }

    public SafeListChecker<T> checker() {
        return _checker;
    }

    public boolean enabled() {
        return _enabled;
    }

    public List<T> list() {
        return _list;
    }

}
