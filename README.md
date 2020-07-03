## MinIO 

[minio 官方文档](https://docs.min.io/cn/minio-quickstart-guide.html)  [github-java-sdk地址](https://github.com/minio/minio-java/tree/master)

### 一、简介

​		MinIO 是一个基于Apache License v2.0开源协议的对象存储服务。它兼容亚马逊S3云存储服务接口，非常适合于存储大容量非结构化的数据，例如图片、视频、日志文件、备份数据和容器/虚拟机镜像等，而一个对象文件可以是任意大小，从几kb到最大5T不等。

​		单机与分布式都适用， 分布式状态下， 最大容许N/2+1个硬盘故障，具有高可用的特性。

### 二、 安装

#### docker 安装

1. 拉取镜像： 

   ```java
   docker pull minio/minio
   ```

2. 创建实例并运行:  

   ```java
   2. docker run -p 9000:9000 --name minio1 \
    -e "MINIO_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE" \
    -e "MINIO_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY" \
    minio/minio server /data
   ```

3. 打开 localhost:9000, 即可看到管理页面

(参数解释)

MINIO_ACCESS_KEY  公钥

MINIO_SECRET_KEY 私钥

minio/minio 映射目录

/data 容器内存储目录
<img src="https://self-file-server.oss-cn-shanghai.aliyuncs.com/workspace/minio-ph-1.png" alt="minio-ph-1" style="zoom:50%;" />

#### ....其它安装方式

### 三、结构

```java
-bucket
    |---dir
         |-- object
```

###  四、 java-sdk

**pom 依赖:**

```java
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>7.1.0</version>
</dependency>
```

**新建客户端连接:**

```java
minioClient = new MinioClient.Builder()
        .credentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
        .endpoint("http://127.0.0.1:9000")
        .build();
```

**新建bucket**

```java
minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket("newBucket")
                    .build());
```

**上传文件**

```java
File file = new File("/Users/wxl/Desktop/aaa.jpg");
FileInputStream inputStream = new FileInputStream(file);
minioClient.putObject(PutObjectArgs.builder()
                .region(null)
                .bucket("newBucket")
                .object("dir/aaa.jpg")
                .stream(inputStream, file.length(), -1)
                .build());
```

**获取文件的临时下载地址**

```java
String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .expiry(10 * 60 * 1000, TimeUnit.MICROSECONDS)
                .method(Method.GET)
                .bucket("newBucket")                               
                .object("dir/aaa.jpg")
                .build());
```

**获取文件的临时上传地址**

```java
Map<String, String> reqParams = new HashMap<>();
        reqParams.put("response-content-type", "application/octet-stream");

        String url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket("demo")         
                                .object("dir/abc.jpg")  // 和oss前端直传不同，这种只能预先设定好要创建的文件
                                .expiry(60 * 60 * 24)    // 链接失效时间
                                .extraQueryParams(reqParams)
                                .build());
postman 测试:
```

<img src="https://self-file-server.oss-cn-shanghai.aliyuncs.com/workspace/minio-ph-2.png" alt="minio-ph-2" style="zoom:50%;" />

**设置bucket加密方式**

```java
List<SseConfigurationRule> rules = new LinkedList<>();
      rules.add(new SseConfigurationRule(null, SseAlgorithm.AES256));
      SseConfiguration config = new SseConfiguration(rules);

      minioClient.setBucketEncryption(
          SetBucketEncryptionArgs.builder().bucket("my-bucketname").config(config).build());
      System.out.println("Encryption configuration of my-bucketname is set successfully");
```

**合并远程文件片段**

```java
ComposeSource s1 =
          ComposeSource.builder().bucket("my-bucketname-one").object("my-objectname-one").build();
      ComposeSource s2 =
          ComposeSource.builder().bucket("my-bucketname-two").object("my-objectname-two").build();
      ComposeSource s3 =
          ComposeSource.builder()
              .bucket("my-bucketname-three")
              .object("my-objectname-three")
              .build();

      // Adding the ComposeSource to an ArrayList
      List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
      sourceObjectList.add(s1);
      sourceObjectList.add(s2);
      sourceObjectList.add(s3);

      minioClient.composeObject(
          ComposeObjectArgs.builder()
              .bucket("my-destination-bucket")
              .object("my-destination-object")
              .sources(sourceObjectList)
              .build());
```

**分片下载**

```java
InputStream stream =
          minioClient.getObject(
              GetObjectArgs.builder()
                  .bucket("my-bucketname")
                  .object("my-objectname")
                  .offset(1024L)
                  .length(4096L)
                  .build());
```

**其它:**

​	设置 bucket 生命周期

​	设置bucket权限

​	对象打tag

​	对象加版本号

​	监听bucket

​	...

### 五、SDK兼容其它对象存储

Minio支持S3协议，而OSS也是支持S3的API的，不过在调通的阶段发现总是抛出二级域名权限异常，原因为:

![minio-ph-3](https://self-file-server.oss-cn-shanghai.aliyuncs.com/workspace/minio-ph-3.png)

path style 和 virual hosted style 决定了请求的url的风格, 对应参数的位置

minio java-sdk对接时需要在api中加入region参数 (bucket存储地区) 

如:  oss-cn-shanghai（上海）,  ap-nanjing（南京）, cn-south-1（华南-1）

#### 全局配置

```yaml
minio:
  oss:
    endpoint: http://oss-cn-shanghai.aliyuncs.com
    accessKey: XXXX
    secretKey: YSe1GEBFfH3YIRTZoWCLkhuadviTfg
  minio:
    endpoint: http://127.0.0.1:9000
    accessKey: AKIAIOSFODNN7EXAMPLE
    secretKey: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
  tencent:
    endpoint: https://cos.ap-nanjing.myqcloud.com
    accessKey: AKIDP1PXAGJhZHjwYuhC3f1RWu8GCsKWn6br
    secretKey: xxxxxxx
  qiniuyun:
    endpoint: s3-cn-south-1.qiniucs.com
    accessKey: I-gp8FzIYJzscQt4KUTrpJLkPc13kWAR_nk03Gq8
    secretKey: xxxxxx
```

#### OSS （阿里）

```java
MinioClient minioClient = ossFileUpload.getMinioClient();
List<Bucket> buckets = minioClient.listBuckets();
List<String> bucketList = buckets.stream().map(Bucket::name).collect(Collectors.toList());
System.out.println(bucketList);
ossFileUpload.putObject("self-file-server", "oss-cn-shanghai", "disk/AAA.jpeg", "/Users/wxl/Desktop/AAA.jpeg");
```

#### COS （腾讯）

```java
MinioClient minioClient = tencentFileUpload.getMinioClient();
List<Bucket> buckets = minioClient.listBuckets();
List<String> bucketList = buckets.stream().map(Bucket::name).collect(Collectors.toList());
System.out.println(bucketList);
tencentFileUpload.putObject("self-file-server-1302435553", "ap-nanjing", "disk/AAA.jpeg", "/Users/wxl/Desktop/AAA.jpeg");
```

#### QiNiuYun（七牛云）

```java
MinioClient minioClient = qiniuyunFileUpload.getMinioClient();
List<Bucket> buckets = minioClient.listBuckets();
List<String> bucketList = buckets.stream().map(Bucket::name).collect(Collectors.toList());
System.out.println(bucketList);
qiniuyunFileUpload.putObject("self-file-server", "cn-south-1", "disk/video.mp4", "/Users/wxl/Desktop/video2.mp4");
```

#### 注意点

​		兼容的API有限， 不过可以满足大部分的基本需求