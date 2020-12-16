package org.mydotey.caravan.util.safelist;

import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.type.TypeConverter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class SafeListManagerConfig<T> {

    private StringProperties _properties;
    private TypeConverter<String, T> _valueParser;

    public SafeListManagerConfig(StringProperties properties, TypeConverter<String, T> valueParser) {
        ObjectExtension.requireNonNull(properties, "properties");
        ObjectExtension.requireNonNull(valueParser, "valueParser");
        _properties = properties;
        _valueParser = valueParser;
    }

    public StringProperties properties() {
        return _properties;
    }

    public TypeConverter<String, T> valueParser() {
        return _valueParser;
    }

}
