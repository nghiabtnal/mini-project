/*
 * @(#)StorageAdapter.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.adapter;


import org.example.miniproject.libs.services.StorageService;

import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public interface StorageAdapter extends AutoCloseable {

    /**
     * setup this adapter.
     * 
     * @param prop the configure properties.
     */
    void setup(Properties prop);

    /**
     * close the adapter
     */
    @Override
    void close();

    /**
     * add an object data to the storage.
     * 
     * @param path        The object path
     * @param inputStream The {@linkplain InputStream}
     * @return the absolute path at the storage
     */
    void put(String path, InputStream inputStream);

    /**
     * Download an object data from a storage.
     * 
     * @param path The object path
     * @return the inputStream
     */
    InputStream get(String path);

    /**
     * load all object data by prefix.
     * 
     * @param prefix The object prefix
     * @return the inputStream
     */
    InputStream load(String prefix);

    /**
     * List object path in storage by prefix
     * 
     * @param prefix The object prefix
     * @return the path list of objects
     */
    List<String> list(String prefix);

    /**
     * List objects in default storage by prefix
     * 
     * @param prefix The object prefix
     * @param filter The {@linkplain ObjectFilter}
     * @return the path list of objects
     */
    List<String> filter(String prefix, StorageService.ObjectFilter filter);

    /**
     * check the file exists or not at the default storage.
     * 
     * @param path the path
     * @return exist:true;not exist:false
     */
    boolean exist(String path);

    /**
     * Gets the value of the Last-Modified time
     * 
     * @param path the path
     * @return last modify date
     */
    Date getLastModified(String path);

    /**
     * remove an object data from a storage.
     * 
     * @param path The object path
     */
    void remove(String path);

    /**
     * copy a file to the specify storage and path.
     * 
     * @param fromPath  the from path.
     * @param toAdapter the to {@linkplain StorageAdapter}.
     * @param toPath    the to path.
     */
    void copy(String fromPath, StorageAdapter toAdapter, String toPath);

    /**
     * open a session to do operator with storage.
     * 
     * @return the {@linkplain StorageAdapter}
     */
    StorageAdapter openSession();

    /**
     * create a storage writer to write multipart data.
     * 
     * @param path the path
     */
    default Writer openWriter(String path) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * create a storage writer to write multipart data.
     * if pass param "any" has value, it find any file and overwrite file.
     * else it find absolute path and overwrite file.
     * 
     * @param path the path
     * @param any the any string
     */
    default Writer openWriter(String path, String any) {
        throw new UnsupportedOperationException();
    }
}
