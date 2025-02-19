/*
 * @(#)UnCloseStorageAdapter.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.adapter;


import org.example.miniproject.libs.services.StorageService;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UnCloseStorageAdapter implements StorageAdapter {

    private StorageAdapter target;

    /**
     * the constructor.
     * 
     * @param target the wrapped {@linkplain StorageAdapter}
     */
    public UnCloseStorageAdapter(StorageAdapter target) {
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(Properties prop) {
        // do nothing

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        target = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String path, InputStream inputStream) {
        target.put(path, inputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream get(String path) {
        return target.get(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream load(String prefix) {
        return target.load(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> list(String prefix) {
        return target.list(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> filter(String prefix, StorageService.ObjectFilter filter) {
        return target.filter(prefix, filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(String path) {
        return target.exist(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastModified(String path) {
        return target.getLastModified(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String path) {
        target.remove(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copy(String fromPath, StorageAdapter toAdapter, String toPath) {
        target.copy(fromPath, toAdapter, toPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageAdapter openSession() {
        throw new UnsupportedOperationException();
    }
}
