/*
 * @(#)S3StorageAdapter.java
 *
 * Copyright(c) 2022 Logicom Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.adapter;

import org.example.miniproject.libs.exceptions.SystemException;
import org.example.miniproject.libs.services.StorageService;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class S3StorageAdapter extends AbstractStorageAdapter {

    /** the {@linkplain S3Client} client */
    private S3Client s3Client;

    /** the bucket name */
    private String bucket;

    /** the object time to live */
    private long ttl;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(Properties prop) {
        
        super.setup(prop);

        // create s3 client
        String region = prop.getProperty("region");
        String accessKey = prop.getProperty("accessKey");
        String secretKey = prop.getProperty("secretKey");
        String provider = prop.getProperty("credentialsProvider");

        S3ClientBuilder builder = S3Client.builder();

        if (StringUtils.hasLength(region)) {
            builder.region(Region.of(region));
        }

        if (StringUtils.hasLength(accessKey) && StringUtils.hasLength(secretKey)) {
            builder.credentialsProvider(StaticCredentialsProvider
                    .create(AwsBasicCredentials.create(accessKey, secretKey)));
        } else if ("instance".equals(provider)) {
            builder.credentialsProvider(InstanceProfileCredentialsProvider.create());
        } else {
            builder.credentialsProvider(ContainerCredentialsProvider.builder().build());
        }

        this.s3Client = builder.build();
        this.bucket = prop.getProperty("bucket");
        this.ttl = Long.parseLong(prop.getProperty("ttl", "-1"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        
        if (this.s3Client != null) {
            this.s3Client.close();
            this.s3Client = null;
        }

        super.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(String path, InputStream inputStream) {
        
        try {
            String realPath = getAbsolutePath(path);

            byte[] data = StreamUtils.copyToByteArray(inputStream);

            PutObjectRequest.Builder builder = PutObjectRequest.builder();

            builder.bucket(bucket).key(realPath);

            if (ttl != -1) {
                builder.expires(Instant.now().plusMillis(ttl));
            }

            s3Client.putObject(builder.build(), RequestBody.fromBytes(data));

        } catch (IOException e) {
            throw new SystemException("can not put Object to s3.path=" + path, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream get(String path) {
        return s3Client.getObject(
                GetObjectRequest.builder().bucket(bucket).key(getAbsolutePath(path)).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> list(String prefix) {
        return s3Client
                // list object by V2
                .listObjectsV2Paginator(ListObjectsV2Request.builder().bucket(bucket)
                        .prefix(getAbsolutePath(prefix)).maxKeys(1000).build())
                // flat map to objects
                .stream().flatMap(e -> e.contents().stream())
                // get path
                .map(S3Object::key)
                // to relative path
                .map(this::getRelativePath)
                // to list
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> filter(String prefix, StorageService.ObjectFilter filter) {
        return s3Client
                // list object by V2
                .listObjectsV2Paginator(ListObjectsV2Request.builder().bucket(bucket)
                        .prefix(getAbsolutePath(prefix)).maxKeys(1000).build())
                // flat map to objects
                .stream().flatMap(e -> e.contents().stream())
                // do custom filter
                .filter(e -> filter.execute(e.key(),
                        e.lastModified() == null ? null : e.lastModified().toEpochMilli(),
                        e.size()))
                // map the object key
                .map(S3Object::key)
                // to relative path
                .map(this::getRelativePath)
                // to list
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exist(String path) {
        
        try {
            s3Client.headObject(
                    HeadObjectRequest.builder().bucket(bucket).key(getAbsolutePath(path)).build());

            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getLastModified(String path) {
        
        try {
            HeadObjectResponse res = s3Client.headObject(
                    HeadObjectRequest.builder().bucket(bucket).key(getAbsolutePath(path)).build());

            return new Date(res.lastModified().toEpochMilli());
        } catch (NoSuchKeyException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String path) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder().bucket(bucket).key(getAbsolutePath(path)).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copy(String fromPath, StorageAdapter toAdapter, String toPath) {
        
        // use s3 copy
        if (toAdapter instanceof S3StorageAdapter) {

            S3StorageAdapter toStorage = (S3StorageAdapter) toAdapter;

            CopyObjectRequest.Builder builder = CopyObjectRequest.builder()
                    // from bucket
                    .sourceBucket(bucket)
                    // source path
                    .sourceKey(getAbsolutePath(fromPath))
                    // to bucket
                    .destinationBucket(toStorage.bucket)
                    // to path
                    .destinationKey(toStorage.getAbsolutePath(toPath));

            if (toStorage.ttl != -1) {
                builder.expires(Instant.now().plusMillis(toStorage.ttl));
            }

            s3Client.copyObject(builder.build());
        }
        // use stream copy
        else {
            super.copy(fromPath, toAdapter, toPath);
        }
    }

}
