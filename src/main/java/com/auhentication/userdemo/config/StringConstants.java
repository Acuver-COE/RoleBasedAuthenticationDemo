package com.auhentication.userdemo.config;

public class StringConstants {
    public static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String passwordregx= "^[a-zA-Z0-9]{8}";
    public static final String HAZELCAST_INSTANCE_NAME="hazelcast_instance";
    public static final  String GROUP_CONFIG_NAME="session_replication_group";
    public static final String MAP_CONFIG_NAME="session_replication_map";

    StringConstants(){

    }

}
