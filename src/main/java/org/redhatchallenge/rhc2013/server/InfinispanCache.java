package org.redhatchallenge.rhc2013.server;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author: Terry Chia (terrycwk1994@gmail.com)
 */
public class InfinispanCache {

    private static EmbeddedCacheManager cacheManager;

    static {
        GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
        global.globalJmxStatistics()
                .allowDuplicateDomains(true).jmxDomain("org.redhatchallenge.rhc2013");
        ConfigurationBuilder builder = new ConfigurationBuilder();
        Configuration config = builder.build(true);
        cacheManager = new DefaultCacheManager(config);
    }

    public static EmbeddedCacheManager getCacheManager() {
        return cacheManager;
    }
}
