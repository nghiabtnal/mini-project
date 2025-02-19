/*
 * @(#)LocalStorageAdapter.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.adapter;

import org.example.miniproject.libs.exceptions.SystemException;
import org.example.miniproject.libs.services.StorageService;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class LocalStorageAdapter extends AbstractStorageAdapter {

    /** the list file style */
    private static final String STYLE_S3 = "s3";

    /** the root path prefix array */
    private String[] rootPaths;

    /** the flag to do list sub directory */
    private String listStyle;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(Properties prop) {

        super.setup(prop);

        listStyle = prop.getProperty("listStyle", "s3");

        // split root path to array
        String root = prop.getProperty("rootPath");
        if (root == null || root.isBlank()) {
            rootPaths = new String[] { "" };
        } else {
            String[] ps = root.split(",");
            for (int i = 0; i < ps.length; i++) {
                ps[i] = ps[i].replaceAll("(/$)", "") + PATH_SEPARATOR;
            }
            this.rootPaths = ps;
            this.rootPath = ps[0];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String path, InputStream inputStream) {

        File file = new File(getAbsolutePath(path));

        // try to make all dirs
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try (FileOutputStream output = new FileOutputStream(file)) {
            StreamUtils.copy(inputStream, output);
        } catch (IOException e) {
            throw new SystemException(
                    String.format("An error occurred on save local data.path=%s", path), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream get(String path) {
        try {
            return new FileInputStream(getLocalFile(path));
        } catch (FileNotFoundException e) {
            throw new SystemException(String.format("file [%s] is not found.", path), e);
        }
    }

    /**
     * check all sub root prefix path to find local file.
     * 
     * @param path the local file path
     * @return the {@linkplain File} Object.
     */
    private File getLocalFile(String path) {

        for (String root : rootPaths) {
            File file = new File(root + path);
            if (file.exists()) {
                return file;
            }
        }

        return new File(getAbsolutePath(path));
    }

    /**
     * list file with s3 style.
     * 
     * @param prefix the file prefix
     * @return the result list
     * @throws IOException
     */
    private List<String> listS3(String prefix, StorageService.ObjectFilter filter) {

        List<String> result = new ArrayList<>();

        String parent = getParent(prefix);

        File pFile = new File(getAbsolutePath(parent));

        if (pFile.listFiles() != null) {

            for (File file : pFile.listFiles()) {

                String path = parent + file.getName();

                if (path.startsWith(prefix)) {

                    if (executeFilter(path, file, filter)) {
                        result.add(path);
                    }

                    if (file.isDirectory()) {
                        addAllSubFiles(file, parent, result, filter);
                    }
                }
            }
        }

        return result;
    }

    /**
     * add all sub file to result list.
     * 
     * @param parentFile the parent file
     * @param parentPath the parent path
     * @param result     the result list
     * @throws IOException
     */
    private void addAllSubFiles(File parentFile, String parentPath, List<String> result,
            StorageService.ObjectFilter filter) {

        String parent = parentPath + parentFile.getName() + "/";

        for (File file : parentFile.listFiles()) {

            String path = parent + file.getName();

            if (executeFilter(path, file, filter)) {
                result.add(path);
            }

            if (file.isDirectory()) {
                addAllSubFiles(file, parent, result, filter);
            }
        }
    }

    /**
     * execute the {@linkplain StorageService.ObjectFilter}.
     * 
     * @param path   the path
     * @param file   the {@linkplain File}
     * @param filter the {@linkplain StorageService.ObjectFilter}
     * @return the execute result
     */
    private boolean executeFilter(String path, File file, StorageService.ObjectFilter filter) {

        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);

            FileTime time = attr.lastModifiedTime();

            return filter.execute(path, time == null ? null : time.toMillis(), attr.size());

        } catch (IOException e) {
            throw new SystemException("Cannot get file attributes for " + file.getAbsolutePath(),
                    e);
        }

    }

    /**
     * check the path is a directory.
     * 
     * @param path the path
     * @return true: is a directory
     */
    private boolean isDir(String path) {
        File file = new File(getAbsolutePath(path));
        return file.exists() && file.isDirectory();
    }

    /**
     * list file with ftp style.
     * 
     * @param prefix the file prefix
     * @return the result list
     */
    private List<String> listFtp(String prefix, StorageService.ObjectFilter filter) {

        if (isDir(prefix)) {
            return listFtpByDir(prefix, filter);

        }

        List<String> result = new ArrayList<>();

        String parent = getParent(prefix);
        String wildcard = prefix.substring(parent.length());
        Pattern namePattern = Pattern.compile(wildcard.replace("*", "[^\\/]*"));

        for (File file : new File(getAbsolutePath(parent)).listFiles()) {

            String name = file.getName();

            if (namePattern.matcher(name).matches()) {

                String path = parent + file.getName();
                if (executeFilter(path, file, filter)) {
                    result.add(path);
                }

            }
        }

        return result;
    }

    /**
     * list file with ftp style by the dir.
     * 
     * @param prefix the file prefix
     * @return the result list
     */
    private List<String> listFtpByDir(String prefix, StorageService.ObjectFilter filter) {

        List<String> result = new ArrayList<>();
        String parent = prefix.isEmpty() ? "" : prefix.replaceAll("(/$)", "") + PATH_SEPARATOR;
        for (File file : new File(getAbsolutePath(prefix)).listFiles()) {
            String path = parent + file.getName();
            if (executeFilter(path, file, filter)) {
                result.add(path);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> list(String prefix) {

        if (STYLE_S3.equals(listStyle)) {

            return listS3(prefix, (path, time, size) -> true);
        } else {

            return listFtp(prefix, (path, time, size) -> true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> filter(String prefix, StorageService.ObjectFilter filter) {

        if (STYLE_S3.equals(listStyle)) {

            return listS3(prefix, filter);
        } else {

            return listFtp(prefix, filter);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(String path) {
        return getLocalFile(path).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastModified(String path) {
        File file = getLocalFile(path);
        return file.exists() ? new Date(file.lastModified()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String path) {

        try {
            Files.delete(getLocalFile(path).toPath());
        } catch (IOException e) {
            throw new SystemException("Cannot remove file: " + path, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copy(String fromPath, StorageAdapter toAdapter, String toPath) {

        // use file copy
        if (toAdapter instanceof LocalStorageAdapter) {
            try {

                File file = ((LocalStorageAdapter) toAdapter).getLocalFile(toPath);

                // try to make all dirs
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                // do copy
                Files.copy(getLocalFile(fromPath).toPath(), file.toPath());

            } catch (IOException e) {
                throw new SystemException(
                        String.format("Storage copy error.from=%s,to=%s", fromPath, toPath), e);
            }
        }
        // use stream copy
        else {
            super.copy(fromPath, toAdapter, toPath);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Writer openWriter(String path) {
        try {

            File file = new File(getAbsolutePath(path));

            // try to make all dirs
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            return new FileWriter(file);
        } catch (IOException e) {
            throw new SystemException("Can not open file writer.", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Writer openWriter(String path, String any) {
        try {
            File file;
            if (StringUtils.hasLength(any)) {
                file = getLocalFile(path);
            } else {
                file = new File(getAbsolutePath(path));
            }
            
            // try to make all dirs
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            return new FileWriter(file);
        } catch (IOException e) {
            throw new SystemException("Can not open file writer.", e);
        }
    }
}
