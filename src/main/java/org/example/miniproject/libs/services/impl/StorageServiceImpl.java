/*
 * @(#)StorageServiceImpl.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.miniproject.libs.adapter.LocalStorageAdapter;
import org.example.miniproject.libs.adapter.S3StorageAdapter;
import org.example.miniproject.libs.adapter.StorageAdapter;
import org.example.miniproject.libs.exceptions.SystemException;
import org.example.miniproject.libs.services.StorageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * implement of the {@link StorageService}.
 *
 * @author ducnn
 * @version 1.0 2022/04/19 create
 */
@Service
@ConfigurationProperties(prefix = "storage")
public class StorageServiceImpl implements StorageService {

    /** the separator to div schema and path */
    private static final String SCHEMA_SEPARATOR = "://";

    /**
     * available storage names
     */
    protected Map<String, Properties> adapters;

    /**
     * available storage names
     */
    protected Map<String, StorageAdapter> adapterServices;

    /**
     * init this service
     */
    @PostConstruct
    public void init() {
        adapterServices = new ConcurrentHashMap<>();
    }

    /**
     * destroy this service
     */
    @PreDestroy
    public void destroy() {
        adapterServices.values().forEach(StorageAdapter::close);
    }

    /**
     * the getter of the adapters.
     *
     * @return the adapters
     */
    public Map<String, Properties> getAdapters() {
        return adapters;
    }

    /**
     * the setter of the adapters.
     *
     * @param adapters the adapters to set
     */
    public void setAdapters(Map<String, Properties> adapters) {
        this.adapters = adapters;
    }

    /**
     * get the adapter instance.
     * 
     * @param storageName The storage name
     */
    protected StorageAdapter getAdapterServices(String storageName) {

        return adapterServices.computeIfAbsent(storageName, this::createAdapter);
    }

    /**
     * create the adapter instance.
     * 
     * @param storageName The storage name
     */
    protected StorageAdapter createAdapter(String storageName) {

        Properties prop = adapters.get(storageName);
        if (prop == null) {
            throw new SystemException(
                    String.format("The storage [%s] is not configured.", storageName));
        }

        String type = prop.getProperty("type");

        // new instance by type
        StorageAdapter instance = null;
        if ("s3".equals(type)) {
            instance = new S3StorageAdapter();
        } else {
            instance = new LocalStorageAdapter();
        }

        // setup properties
        instance.setup(prop);

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getPropertiesStorage(String storageName) {
        Properties prop = adapters.get(storageName);
        if (prop == null) {
            throw new SystemException(
                    String.format("The storage [%s] is not configured.", storageName));
        }
        return prop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String put(Storage storage, String path, InputStream inputStream) {

        getAdapterServices(storage.getValue()).put(path, inputStream);

        return getAbsolutePath(storage, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageAdapter openSession(Storage storage) {
        return getAdapterServices(storage.getValue()).openSession();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAbsolutePath(Storage storage, String path) {
        return storage.getValue() + SCHEMA_SEPARATOR + path;
    }

    /**
     * parse the absolute path.
     * 
     * @param absolutePath
     * @return the parsed array with tow item : [storage,path].
     */
    private String[] parse(String absolutePath) {

        String[] ps = absolutePath.split(SCHEMA_SEPARATOR);

        if (ps.length != 2) {
            throw new SystemException(
                    String.format("Incorrect format for the absolute path [%s].", absolutePath));
        }

        return ps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream get(String absolutePath) {
        String[] ps = parse(absolutePath);
        return getAdapterServices(ps[0]).get(ps[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream get(Storage storage, String path) {
        return getAdapterServices(storage.getValue()).get(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> list(Storage storage, String prefix) {
        return getAdapterServices(storage.getValue()).list(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream load(Storage storage, String prefix) {
        return getAdapterServices(storage.getValue()).load(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> filter(Storage storage, String prefix, ObjectFilter filter) {
        return getAdapterServices(storage.getValue()).filter(prefix, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastModified(Storage storage, String path) {
        return getAdapterServices(storage.getValue()).getLastModified(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(Storage storage, String path) {
        return getAdapterServices(storage.getValue()).exist(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(String absolutePath) {

        String[] ps = absolutePath.split(SCHEMA_SEPARATOR);
        if (ps.length != 2) {
            return false;
        }

        if (!adapters.containsKey(ps[0])) {
            return false;
        }

        return getAdapterServices(ps[0]).exist(ps[1]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Storage storage, String path) {
        getAdapterServices(storage.getValue()).remove(path);
    }

    /**
     * copy file within storages.
     * 
     * @param fromStorage the from {@linkplain StorageAdapter}
     * @param fromPath    the from path
     * @param toStorage   the to {@linkplain StorageAdapter}
     * @param toPath      the tp path
     */
    private void copy(StorageAdapter fromStorage, String fromPath, StorageAdapter toStorage,
            String toPath) {
        fromStorage.copy(fromPath, toStorage, toPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String copy(Storage fromStorage, String fromPath, Storage toStorage, String toPath) {

        copy(getAdapterServices(fromStorage.getValue()), fromPath,
                getAdapterServices(toStorage.getValue()), toPath);

        return getAbsolutePath(toStorage, toPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String copy(String absolutePath, Storage toStorage, String toPath) {

        String[] ps = parse(absolutePath);

        copy(getAdapterServices(ps[0]), ps[1], getAdapterServices(toStorage.getValue()), toPath);

        return getAbsolutePath(toStorage, toPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Writer openWriter(Storage storage, String path) {
        return getAdapterServices(storage.getValue()).openWriter(path);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Writer openWriter(Storage storage, String path, String any) {
        return getAdapterServices(storage.getValue()).openWriter(path, any);
    }
}
