package com.auhentication.userdemo.config;

import com.auhentication.userdemo.security.JwtUtil;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.listener.*;

public class HazelcastEntryListener implements EntryAddedListener<String, String>,
        EntryRemovedListener<String, String>,
        EntryUpdatedListener<String, String>,
        EntryEvictedListener<String, String>,
        MapEvictedListener,
        MapClearedListener {


    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        System.out.println("Hazelcast Entry Added: " + event);
    }

    @Override
    public void entryRemoved(EntryEvent<String, String> event) {
        System.out.println("Hazelcast Entry Removed: " + event);
    }

    @Override
    public void entryUpdated( EntryEvent<String, String> event) {
        System.out.println("Hazelcast Entry Updated: " + event);
    }

    @Override
    public void entryEvicted( EntryEvent<String, String> event) {
        System.out.println("Hazelcast Entry Evicted: " + event);
    }

    @Override
    public void mapEvicted( MapEvent event) {
        System.out.println("Hazelcast Map Evicted: " + event);
    }

    @Override
    public void mapCleared( MapEvent event) {
        System.out.println("Hazelcast Map Cleared: " + event);
    }
}
