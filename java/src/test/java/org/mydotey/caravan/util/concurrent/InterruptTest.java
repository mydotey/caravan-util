package org.mydotey.caravan.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;
import org.mydotey.java.ThreadExtension;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.SimpleConfigurationSources;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.filter.RangeValueConfig;

public class InterruptTest {

    @Test
    public void testInterrupt() {
        ConfigurationManager manager = ConfigurationManagers
            .newManager(SimpleConfigurationSources.newSystemPropertiesSource());
        StringProperties properties = new StringProperties(manager);
        DynamicScheduledThreadConfig config = new DynamicScheduledThreadConfig(properties,
            new RangeValueConfig<Integer>(10, 1, 100), new RangeValueConfig<Integer>(5000, 1, 10000));
        AtomicBoolean started = new AtomicBoolean();
        DynamicScheduledThread thread = new DynamicScheduledThread("test",
            () -> started.compareAndSet(false, true), config);
        thread.setDaemon(true);
        thread.start();
        ThreadExtension.sleep(1000);
        Assert.assertTrue(started.get());
        thread.shutdown();
        ThreadExtension.sleep(100);
        thread.shutdown();
    }

}
