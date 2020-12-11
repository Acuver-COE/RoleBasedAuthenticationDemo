package com.auhentication.userdemo.config;

import com.hazelcast.map.listener.MapListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hazelcast.config.*;

//@Configuration
public class HazelcastConfig {

    @Bean
    public MapListener mapListener() {
        return new HazelcastEntryListener();
    }

    @Bean
    public Config config() {

        final Config config = new Config();
        config.setInstanceName(StringConstants.HAZELCAST_INSTANCE_NAME);
        config.getGroupConfig().setName(StringConstants.GROUP_CONFIG_NAME);
        config.addMapConfig(mapConfig());

        final NetworkConfig networkConfig = config.getNetworkConfig();

        final JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig()
                .setEnabled(true)
                .addMember("127.0.0.1");

        return config;
    }

    // Since we are creating the map it's important to evict sessions
    // by setting a reasonable value for time to live
    // as the default is 0 which means infinite.
    //  30 seconds for demonstration purposes.
    private MapConfig mapConfig() {
        final MapConfig mapConfig = new MapConfig();
        mapConfig.setName(StringConstants.MAP_CONFIG_NAME);
        mapConfig.setTimeToLiveSeconds(30);
        mapConfig.setMaxIdleSeconds ( 60 );
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
        mapConfig.addEntryListenerConfig(new EntryListenerConfig(HazelcastEntryListener.class.getName(), false, false ) );
        return mapConfig;
    }
}
