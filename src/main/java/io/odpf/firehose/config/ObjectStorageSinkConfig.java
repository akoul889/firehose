package io.odpf.firehose.config;

import io.odpf.firehose.config.converter.CloudSinkPartitioningTypeConverter;
import io.odpf.firehose.config.converter.CloudSinkWriterTypeConverter;
import io.odpf.firehose.sink.objectstorage.Constants;

public interface ObjectStorageSinkConfig extends AppConfig {

    @Key("SINK_OBJECT_STORAGE_LOCAL_DIRECTORY")
    String getLocalDirectory();

    @Key("SINK_OBJECT_STORAGE_FILE_WRITER_TYPE")
    @DefaultValue("parquet")
    @ConverterClass(CloudSinkWriterTypeConverter.class)
    Constants.WriterType getFileWriterType();

    @Key("SINK_OBJECT_STORAGE_KAFKA_METADATA_COLUMN_NAME")
    @DefaultValue("")
    String getKafkaMetadataColumnName();

    @Key("SINK_OBJECT_STORAGE_WRITE_KAFKA_METADATA")
    boolean getWriteKafkaMetadata();

    @Key("SINK_OBJECT_STORAGE_WRITER_BLOCK_SIZE")
    int getWriterBlockSize();

    @Key("SINK_OBJECT_STORAGE_WRITER_PAGE_SIZE")
    int getWriterPageSize();

    @Key("SINK_OBJECT_STORAGE_ROTATION_DURATION_MILLIS")
    @DefaultValue("3600000")
    int getFileRotationDurationMillis();

    @Key("SINK_OBJECT_STORAGE_ROTATION_MAX_SIZE_BYTES")
    @DefaultValue("268435456")
    int getFileRationMaxSizeBytes();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_FIELD_NAME")
    String getTimePartitioningFieldName();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_DATE_PATTERN")
    @DefaultValue("YYYY-MM-dd")
    String getTimePartitioningDatePattern();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_TIME_ZONE")
    @DefaultValue("UTC")
    String getTimePartitioningTimeZone();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_DATE_PREFIX")
    @DefaultValue("dt=")
    String getTimePartitioningDatePrefix();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_TYPE")
    @DefaultValue("day")
    @ConverterClass(CloudSinkPartitioningTypeConverter.class)
    Constants.PartitioningType getPartitioningType();

    @Key("SINK_OBJECT_STORAGE_TIME_PARTITIONING_HOUR_PREFIX")
    @DefaultValue("hr=")
    String getTimePartitioningHourPrefix();

    @Key("SINK_OBJECT_STORAGE_TYPE")
    @DefaultValue("GCS")
    String getCloudStorageType();

    @Key("SINK_OBJECT_STORAGE_BUCKET_NAME")
    String getObjectStorageBucketName();

    @Key("SINK_OBJECT_STORAGE_GOOGLE_CLOUD_PROJECT_ID")
    String getStorageGcloudProjectID();
}
