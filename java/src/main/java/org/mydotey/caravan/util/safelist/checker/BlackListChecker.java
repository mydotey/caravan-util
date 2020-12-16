package org.mydotey.caravan.util.safelist.checker;

import java.util.List;

import org.mydotey.caravan.util.safelist.SafeListChecker;
import org.mydotey.java.collection.CollectionExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class BlackListChecker<T> implements SafeListChecker<T> {

    public static BlackListChecker<String> DEFAULT = new BlackListChecker<>();

    @Override
    public boolean check(List<T> list, T item) {
        if (CollectionExtension.isEmpty(list))
            return true;

        return !list.contains(item);
    }

}
