/*
 * @(#)StorageService.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.services;

import org.example.miniproject.libs.adapter.StorageAdapter;

import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * information about StorageService.
 *
 * @author ducnn
 * @version 1.0 2022/04/19 create
 */
public interface StorageService {

    /**
     * list of The storage types
     */
    enum Storage {
        // default
        DEFAULT("default"),
        // temp
        TEMP("temp"),
        // system
        SYSTEM("system"),
        // if
        IF("if"),
        EXCEL("excel"),
        REPORT("report"),
        IMPORT("import"),
        S3("S3");
        /**
         * storage name code
         */
        private String value;

        /**
         * constructor
         * 
         * @param value
         */
        private Storage(String value) {
            this.value = value;
        }

        /**
         * code getter
         * 
         * @return storage name code
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The Object Filter to filter Objects.
     */
    public interface ObjectFilter {

        /**
         * do filter object.
         * 
         * @param key
         * @param lastModified
         * @param size
         * @return
         */
        boolean execute(String key, Long lastModified, Long size);
    }

    /**
     * Get properties from storage name
     * @param storageName storage name
     * @return A Properties for storage name
     */
    Properties getPropertiesStorage(String storageName);

    /**
     * adds an object to the given storage.
     * 
     * @param storage     The storage type
     * @param path        The object path
     * @param inputStream The {@linkplain InputStream}
     * @return the absolute path at the storage
     */
    String put(Storage storage, String path, InputStream inputStream);

    /**
     * open a {@linkplain StorageAdapter} to do a large number of files operators.
     * 
     * @param storage The storage type
     * @return a {@linkplain StorageAdapter}
     */
    StorageAdapter openSession(Storage storage);

    /**
     * Get the absolute path of this object path.
     * 
     * @param storage The storage type
     * @param path    The object path
     * @return the absolute path with the format "{storage}://{path}"
     */
    String getAbsolutePath(Storage storage, String path);

    /**
     * Download an object by the absolute path with the format "{storage}://{path}".
     * 
     * @param absolutePath The absolute path
     * @return the inputStream
     */
    InputStream get(String absolutePath);

    /**
     * Download an object from the given storage.
     * 
     * @param storage The storage type
     * @param path    The object path
     * @return the inputStream
     */
    InputStream get(Storage storage, String path);

    /**
     * List objects in the given storage by prefix
     * 
     * @param storage The storage type
     * @param prefix  The object prefix
     * @return list of the file key
     */
    List<String> list(Storage storage, String prefix);

    /**
     * load all object data by prefix.
     * 
     * @param storage The storage type
     * @param prefix  The object prefix
     * @return the inputStream
     */
    InputStream load(Storage storage, String prefix);

    /**
     * List objects in default storage by prefix
     * 
     * @param storage The storage type
     * @param prefix  The object prefix
     * @param filter  The {@linkplain ObjectFilter}
     * @return the path list of objects
     */
    List<String> filter(Storage storage, String prefix, ObjectFilter filter);

    /**
     * Gets the value of the Last-Modified time from the specified storage.
     * 
     * @param storage The storage type
     * @param path    the path
     * @return last modify date
     */
    Date getLastModified(Storage storage, String path);

    /**
     * check the file exists or not at the specified storage.
     * 
     * @param storage The storage type
     * @param path    The path
     * @return exist:true / not exist:false
     */
    boolean exist(Storage storage, String path);

    /**
     * check the file exists or not at the specified absolute path.
     * 
     * @param absolutePath The absolute path
     * @return exist:true / not exist:false
     */
    boolean exist(String absolutePath);

    /**
     * remove a file from a specified storage.
     * 
     * @param storage The storage type
     * @param path    The path
     */
    void remove(Storage storage, String path);

    /**
     * copy the file.
     * 
     * @param fromStorage the from storage name
     * @param fromPath    the from file path
     * @param toStorage   the to storage name
     * @param toPath      the to file path
     * @return the absolute path with the format "{storage}://{path}".
     */
    String copy(Storage fromStorage, String fromPath, Storage toStorage, String toPath);

    /**
     * copy the file by the absolute path with the format "{storage}://{path}".
     * 
     * @param absolutePath the absolute path
     * @param toStorage    the to storage name
     * @param toPath       the to file path
     * @return the absolute path with the format "{storage}://{path}".
     */
    String copy(String absolutePath, Storage toStorage, String toPath);

    /**
     * create a storage writer to write multipart data.
     * 
     * @param storage the storage name
     * @param path    the path
     */
    Writer openWriter(Storage storage, String path);
    
    /**
     * create a storage writer to write multipart data.
     * 
     * @param storage the storage name
     * @param path    the path
     * @param any     the any string
     */
    Writer openWriter(Storage storage, String path, String any);
}
