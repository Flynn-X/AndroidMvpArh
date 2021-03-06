package com.example.xsl.corelibrary.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by xsl on 2017/10/31.
 * xsl
 */
public class BuildProperties {

    private static BuildProperties ourInstance;

    public static BuildProperties getInstance() throws IOException {

        if(ourInstance==null) {
            ourInstance = new BuildProperties();
        }
        return ourInstance;
    }

    private final Properties properties;

    private BuildProperties() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(new File(Environment.getRootDirectory(),"build.prop")));
    }

    public boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name,defaultValue);
    }

    public Set<Map.Entry<Object,Object>> entrySet() {
        return properties.entrySet();
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public int size() {
        return properties.size();

    }


}
