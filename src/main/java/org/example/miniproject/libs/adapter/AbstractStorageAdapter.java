/*
 * @(#)AbstractStorageAdapter.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.adapter;

import org.example.miniproject.libs.exceptions.SystemException;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractStorageAdapter implements StorageAdapter {

    /**
     * the path separator
     */
    protected static final char PATH_SEPARATOR = '/';

    /** the root path prefix */
    protected String rootPath;

    /** the root path end at position */
    private int rootEndAt;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(Properties prop) {

        // make sure end with slash
        String root = prop.getProperty("rootPath");

        if (root == null || root.isBlank()) {
            this.rootPath = "";
        } else {
            this.rootPath = root.replaceAll("(/$)", "") + PATH_SEPARATOR;
        }

        this.rootEndAt = this.rootPath.length();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream load(String prefix) {

        // on memory operators
        try (FastByteArrayOutputStream bos = new FastByteArrayOutputStream()) {

            for (String path : list(prefix)) {
                try (InputStream in = get(path)) {
                    StreamUtils.copy(in, bos);
                }
            }

            return bos.getInputStream();

        } catch (IOException e) {
            throw new SystemException(
                    String.format("An error occurred on load data.prefix=%s", prefix), e);
        }
    }

    /**
     * Get the absolute path of this object path.
     * 
     * @param path The object path
     * @return the absolute path
     */
    protected String getAbsolutePath(String path) {
        return rootPath + path;
    }

    /**
     * Get the relative path of this object path.
     * 
     * @param path The object path
     * @return the relative path
     */
    protected String getRelativePath(String path) {
        return path == null ? null : path.substring(rootEndAt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copy(String fromPath, StorageAdapter toAdapter, String toPath) {

        try (InputStream in = get(fromPath)) {
            toAdapter.put(toPath, in);
        } catch (IOException e) {
            throw new SystemException(
                    String.format("Storage copy error.from=%s,to=%s", fromPath, toPath), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StorageAdapter openSession() {
        return new UnCloseStorageAdapter(this);
    }

    /**
     * get the parent path from prefix.
     * 
     * @param prefix the path prefix.
     * @return the parent path
     */
    protected String getParent(String prefix) {
        int p = prefix.lastIndexOf('/');
        return p == -1 ? "" : prefix.substring(0, p + 1);
    }
}
