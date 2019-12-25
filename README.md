
# OCHaCafe - Cloud Native時代のモダンJavaの世界

[Helidon](https://helidon.io/) を使って [Eclipse MicroProfile](https://microprofile.io/) の仕様を確認するデモ.

## デモのソース

```text
src/main
├── java
│   └── oracle
│       └── demo
│           ├── package-info.java
│           ├── Main.java [起動クラス]
│           ├── App.java [JAX-RS Application]
│           ├── greeting [Helidon MP付属のサンプルコード]
│           │   ├── GreetingProvider.java
│           │   └── GreetResource.java
│           ├── echo [JAX-RS, CDI, JAX-P, JAX-B の基本]
│           │   └── EchoResource.java
│           ├── country [OpenAPI]
│           │   ├── CountryNotFoundExceptionMapper.java
│           │   └── CountryResource.java
│           ├── filter [JAX-RSのフィルター]
│           │   ├── Auth.java
│           │   ├── BasicAuthFilter.java
│           │   ├── CORSFilter.java
│           │   ├── CORS.java
│           │   ├── DebugFilter.java
│           │   └── Debug.java
│           ├── mapper [JAX-RSの例外マッパー]
│           │   └── CountryNotFoundExceptionMapper.java
│           ├── ft [フォルトトレランス]
│           │   ├── FaultToleranceResource.java
│           │   └── FaultToleranceTester.java
│           ├── health [ヘルスチェック]
│           │   ├── HealthCheckResource.java
│           │   ├── MyHealthCheck.java
│           │   └── TimeToFail.java
│           ├── metrics [メトリクス]
│           │   └── MetricsResource.java
│           ├── restclient [RESTクライアント]
│           │   ├── Movie.java
│           │   ├── MovieReviewService.java
│           │   ├── MovieReviewServiceResource.java
│           │   ├── MovieReviewServiceRestClientResource.java
│           │   └── Review.java
│           ├── security [セキュリティ]
│           │   ├── IdcsResource.java
│           │   └── SecurityResource.java
│           ├── tracing [トレーシング]
│           │   └── TracingResource.java
│           ├── jpa [拡張機能 JPA/JTA]
│           │   ├── Country.java
│           │   ├── CountryResource.java
│           │   ├── Greeting.java
│           │   └── JPAExampleResource.java
│           └── cowweb [おまけ]
│               └── CowwebResource.java
└── resources
    ├── application.yaml [Helidonで使う設定ファイル]
    ├── createtable.ddl [JPA拡張機能で使うH2用のDDL]
    ├── jbossts-properties.xml [JTAの設定ファイル]
    ├── logging.properties [ログ設定ファイル]
    ├── META-INF
    │   ├── beans.xml [CDIの設定ファイル]
    │   ├── microprofile-config.properties [MicroProfile設定ファイル]
    │   └── persistence.xml [JPAの設定ファイル]
    └── WEB [静的コンテンツのフォルダー]
        └── index.html 
demo
├── ft [Fault Tolerance テスト用sh]
│   ├── bulkhead-test.sh
│   └── circuit-breaker-test.sh
├── k8s [kubernetesデプロイメント用マニフェスト]
│   ├── liveness-check.yaml
│   ├── open-tracing.yaml
│   ├── simple-deployment.yaml
│   └── simple-service.yaml
└── tracing [トレーシングデモ]
    ├── request.json
    └── tracing-demo.sh
```

## ビルド方法

```
mvn package
```

## アプリケーションの起動

```
java -jar target/helidon-demo-mp.jar
```
若しくは
```
mvn exec:java
```

## Docker イメージの作成

Dockerfileを使わずに、[Jib](https://github.com/GoogleContainerTools/jib) を使ってMavenから直接イメージをビルドします.

### 通常（ローカル）のタグを付与する場合

```
mvn post-integration-test # 便宜上post-integration-testにアサインしているだけ
```

### リモート用のタグを付与する場合

pom.xmlを以下のように設定し、
```
    <profiles>
        <!-- mvn -P could ... -->
        <profile>
            <id>cloud</id>
            <properties>
                <docker.repo.prefix>(remote docker repository path/)</docker.repo.prefix>
            </properties>
        </profile>
    </profiles>
```
以下を実行します.

```
mvn -P cloud post-integration-test
```

ローカルリポジトリに作成されたイメージをリポートリポジトリにpushします.
```
$ docker images
REPOSITORY                                        TAG                 IMAGE ID            CREATED             SIZE
helidon-demo-mp                                   1.0-SNAPSHOT        1b4d2e82f64a        49 years ago        125MB
helidon-demo-mp                                   latest              1b4d2e82f64a        49 years ago        125MB
(remote docker repository path/)helidon-demo-mp   1.0-SNAPSHOT        116de0207be6        49 years ago        125MB
(remote docker repository path/)helidon-demo-mp   latest              116de0207be6        49 years ago        125MB

$ docker push (remote docker repository path/)helidon-demo-mp
```
<br/>

---
_Copyright © 2019, Oracle and/or its affiliates. All rights reserved._  
_This software includes the work that is distributed in the Apache License 2.0._

