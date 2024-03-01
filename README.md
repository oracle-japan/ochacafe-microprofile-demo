![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/oracle-japan/ochacafe-microprofile-demo)

# [Eclipse MicroProfile](https://microprofile.io/) の仕様や [Helidon](https://helidon.io/) の拡張機能を確認するデモ

[OCHaCafe 2 - #4 Cloud Native時代のモダンJavaの世界](https://ochacafe.connpass.com/event/155389/) のために作成したデモですが、随時実装を追加しています。  
[**セッション・スライドはこちら**](http://tiny.cc/ochacafe-cn-java-slides)

**※ ベースを 4.x 系にアップグレードしました。3.x系は `helidon-3.x` ブランチ、2.x系は `helidon-2.x` ブランチで各々メンテナンスしていきます。**

Helidon 4 は MicroProfile 6 と Jakarta EE 10 Core Profile をサポートしたため、デモの実装が従来と異なる箇所があります。

## 目次

+ [ビルド方法](#-ビルド方法)
+ [アプリケーションの起動](#-アプリケーションの起動)
+ [Docker イメージの作成](#-docker-イメージの作成)
+ [Health デモ](#-microprofile-health-デモ-oracledemohealth-パッケージ)
  - [Kubernetes で Health Check を試してみる](#Kubernetes-で-Health-Check-を試してみる)
+ [Tracing デモ](#-tracing-デモ-oracledemotracing-パッケージ)
  - [OCI Application Performance Monitoring (APM) の Tracer を使う](#OCI-Application-Performance-Monitoring-APM-の-Tracer-を使う)
+ [Metrics デモ](#-metrics-デモ-oracledemometrics-パッケージ)
+ [Fault Tolerance デモ](#-fault-tolerance-デモ-oracledemoft-パッケージ)
+ [Open API デモ](#-open-api-oracledemocountry-パッケージ)
+ [Rest Client デモ](#-microprofile-rest-client-oracledemorestclient-パッケージ)
+ [Security デモ](#-security-oracledemosecurity-パッケージ)
+ [JPA/Transaction デモ](#-jpa-java-persistence-api-デモ-oracledemojpa-パッケージ)
  - [接続先を H2 Databse から Oracle Database に変更するには？](#接続先を-H2-Databse-から-Oracle-Database-に変更するには)
+ [gRPC デモ](#-grpc-デモ-oracledemogrpc-パッケージ)
+ [Reactive Messaging デモ](#-microprofile-reactive-messaging-デモ-oracledemoreactive-パッケージ)
+ [GraphQL デモ](#-microprofile-graphql-デモ-oracledemographql-パッケージ)
+ [Mapped Diagnostic Context (Mdc) デモ](#-Mapped-Diagnostic-Context-Mdc-デモ-oracledemologging-パッケージ)
+ [Scheduling デモ](#-Scheduling-デモ-oracledemoscheduling-パッケージ)
+ [MicroProfile LRA デモ](#-MicroProfile-LRA-デモ-oracledemolra-パッケージ)
+ [おまけ](#-おまけcowsay-oracledemocowweb-パッケージ)

## デモのソース

<details>
<summary>ディレクトリ ＆ ファイル</summary>

```text
src/main
├── java
│   └── oracle
│       └── demo
│           ├── package-info.java
│           ├── App.java [JAX-RS Application]
│           ├── greeting [Helidon MP付属のサンプルコード]
│           │   ├── GreetingProvider.java
│           │   └── GreetResource.java
│           ├── echo [JAX-RS, CDI, JAX-P, JAX-B の基本]
│           │   └── EchoResource.java
│           ├── country [OpenAPI]
│           │   ├── CountryNotFoundException.java
│           │   ├── CountryNotFoundExceptionMapper.java
│           │   └── CountryResource.java
│           ├── filter [JAX-RSのフィルター]
│           │   ├── Auth.java
│           │   ├── BasicAuthFilter.java
│           │   ├── CORS.java
│           │   ├── CORSFilter.java
│           │   ├── Debug.java
│           │   ├── DebugFilter.java
│           │   └── FilterResource.java
│           ├── ft [フォルトトレランス]
│           │   ├── FaultToleranceResource.java
│           │   └── FaultToleranceTester.java
│           ├── graphql [GraphQL]
│           │   └── CountryGraphQLApi.java
│           ├── grpc [拡張機能 gRPC - 4.x には無し]
│           │   └── protobuf
│           │       ├── GreeterSimpleService.java
│           │       ├── GreeterService.java
│           │       ├── GrpcResource.java
│           │       └── helloworld [ビルド時に生成される]
│           │           ├── GreeterGrpc.java
│           │           └── Helloworld.java
│           ├── health [ヘルスチェック]
│           │   ├── HealthCheckHelper.java
│           │   ├── HealthCheckResource.java
│           │   └── MyHealthCheck.java
│           ├── jpa [拡張機能 JPA/JTA]
│           │   ├── CountryDAO.java
│           │   ├── Country.java
│           │   ├── CountryResource.java
│           │   ├── Greeting.java
│           │   └── JPAExampleResource.java
│           ├── logging [拡張機能 Mdc - 4.x には無し]
│           │   ├── MdcInterceptor.java
│           │   ├── Mdc.java
│           │   ├── MdcResource.java
│           │   └── Sub.java
│           ├── lra [LRA - Long Running Actions]
│           │   ├── LRAExampleResource.java
│           │   ├── LRAMain.java
│           │   ├── LRAService1.java
│           │   └── LRAService2.java
│           ├── metrics [メトリクス]
│           │   └── MetricsResource.java
│           ├── reactive [Reactive Messaging & Connecter]
│           │   ├── DaoEvent.java
│           │   ├── ExecutorServiceHelper.java
│           │   ├── ReactiveResource.java
│           │   └── ConnectorResource.java
│           ├── restclient [RESTクライアント]
│           │   ├── Movie.java
│           │   ├── MovieReviewService.java
│           │   ├── MovieReviewServiceResource.java
│           │   ├── MovieReviewServiceRestClientResource.java
│           │   └── Review.java
│           ├── scheduling [拡張機能 スケジューリング]
│           │   └── Scheduler.java
│           ├── security [セキュリティ]
│           │   ├── IdcsResource.java
│           │   └── SecurityResource.java
│           ├── tracing [トレーシング]
│           │   └── TracingResource.java
│           └── cowweb [おまけ]
│               └── CowwebResource.java
├── proto
│   └── helloworld.proto [gRPC IDL定義]
└── resources
    ├── application.yaml [Helidonの設定ファイル microprofile-config.properties 相当として利用可能]
    ├── createtable.ddl [JPA拡張機能で使うH2用のDDL]
    ├── jbossts-properties.xml [JTAの設定ファイル]
    ├── logging.properties [ログ設定ファイル]
    ├── META-INF
    │   ├── beans.xml [CDIの設定ファイル]
    │   ├── microprofile-config-k8s.properties [k8sデプロイ用プロファイル]
    │   ├── microprofile-config.properties [MicroProfile設定ファイル]
    │   ├── persistence.xml [JPAの設定ファイル]
    │   └── services
    │       ├── io.helidon.microprofile.grpc.server.spi.GrpcMpExtension  [gRPC Extension設定ファイル]
    │       └── org.eclipse.microprofile.config.spi.ConfigSource [JDBC関連Config設定ファイル]
    └── WEB [静的コンテンツのフォルダー]
        ├── graphql
        │   └── ui
        │       └── index.html [GraphQL UI]
        ├── apm.html
        ├── apm.js
        ├── apmrum.js.example
        └── index.html [テストページ]
```
</details>
<br>

## § ビルド方法

Java SE 21 が必要です。

```bash
mvn clean package -DskipTests=true
```

<br>

## § アプリケーションの起動

```bash
java -jar target/helidon-mp-demo.jar
```

[目次に戻る](#目次)
<br>

## § Docker イメージの作成

環境変数 REMOTE_REPO_PREFIX を設定した後、mvn を使って、docker イメージの作成とリモート・リポジトリへの push を行います

```
# REMOTE_REPO_PREFIX -> リモート・リポジトリのパス (/で終わる)
# 以下の例だと、iad.ocir.io/some-tenant/some-path/helidon-mp-demo:{version} となる
export REMOTE_REPO_PREFIX=iad.ocir.io/some-tenant/some-path/

# イメージの作成とタグ付け
mvn -f pom-docker.xml exec:exec@docker-build [-Ddocker.file=<任意のDockerfileを指定したい場合>]

# iad.ocir.io/some-tenant/some-path/helidon-mp-demo への image push
mvn -f pom-docker.xml exec:exec@docker-push

# ローカル・イメージの作成だけ行いたい場合
# イメージの名前は helidon-mp-demo:{version} となる
mvn -f pom-docker.xml exec:exec@docker-local-build [-Ddocker.file=<任意のDockerfileを指定したい場合>]
```

```bash
$ docker images
REPOSITORY                                          TAG                 IMAGE ID            CREATED             SIZE
helidon-mp-demo                                     2.2.2               80612d9f5ee0        4 seconds ago       299MB
helidon-mp-demo                                     latest              80612d9f5ee0        4 seconds ago       299MB
iad.ocir.io/some-tenant/some-path/helidon-mp-demo   2.2.2               80612d9f5ee0        4 seconds ago       299MB
iad.ocir.io/some-tenant/some-path/helidon-mp-demo   latest              80612d9f5ee0        4 seconds ago       299MB
```

[目次に戻る](#目次)
<br>

## § MicroProfile Health デモ (oracle.demo.health パッケージ)

`/health/live` (Liveness)、`/health/ready` (Readiness) 及び `/health` (複合パターン) のエンドポイントを使ってヘルスチェックができます。 

```json
{
    "outcome": "UP",
    "status": "UP",
    "checks": [
        {
            "name": "my-health-check-liveness",
            "state": "UP",
            "status": "UP",
            "data": {
                "time-to-fail": 0,
                "uptime": 29071
            }
        },
        {
            "name": "my-health-check-readiness",
            "state": "UP",
            "status": "UP"
        }
    ]
}
```

このデモでは、タイムアウト値を設定することによって、サーバーの起動時間が一定の値を超えるとLivenessをDOWNにすることができます。  
タイムアウト値(デフォルト= 0 [タイムアウトしない])は、2種類の方法で設定できます。

1. microprofile-config.properties (or application.yaml) で設定する

```text
# Health
#demo.healthcheck.liveness.name=_my-health-check
demo.healthcheck.time-to-fail=30 # in second, default: 0
```

2. RESTで設定する

```
$ curl localhost:8080/myhealth?timeToFail=30
```

### Kubernetes で Health Check を試してみる

KubernetesはPodの正常性をチェックし、一定の条件を満たすとPodを再起動する機能があります。

```yaml
    livenessProbe:
      httpGet:
        port: 8080
        path: /health/live
      failureThreshold: 2
      periodSeconds: 10
```

demo/k8s/liveness-check.yaml は環境変数 `demo.healthcheck.time-to-fail` を 30に設定するので、Podが起動して30秒経過すると、`/health/live` のステータスは 503 (DOWN)となります。

```bash
kubectl create namespace demo

# export environment variable as appropriate
export REMOTE_REPO_PREFIX=iad.ocir.io/some-tenant/some-additional-path/

# (オプション)プライベートリポジトリの場合は、`docker-registry-secret` という secret を作成して下さい
kubectl create secret docker-registry docker-registry-secret -n demo \
 --docker-server=iad.ocir.io \
 --docker-username='some-tenant/some-username' \
 --docker-password='access-token-or-something' \
 --docker-email='some-mail-address'

# replace "${REMOTE_REPO_PREFIX}/helidon-mp-demo:latest" in liveness-check.yaml and apply
envsubst < demo/k8s/liveness-check.yaml | kubectl apply -f -
```

ここで Pod の状態を定期的に確認すると、再起動されている ( RESTARTS がカウントアップされている) ことが分かります。 

```bash
$ kubectl get pod -n demo -w
NAME                     READY   STATUS    RESTARTS   AGE
helidon-mp-demo-health   1/1     Running   0          12s
helidon-mp-demo-health   1/1     Running   1          52s
helidon-mp-demo-health   1/1     Running   2          103s
helidon-mp-demo-health   1/1     Running   3          2m33s
```
```
$ kubectl describe pod helidon-mp-demo-health -n demo
(中略...)
Events:
  Type     Reason     Age                    From                 Message
  ----     ------     ----                   ----                 -------
  Normal   Started    9m13s (x3 over 10m)   kubelet, 10.0.10.11  Started container api-helidon-container
  Warning  Unhealthy  9m4s (x3 over 10m)    kubelet, 10.0.10.11  Liveness probe failed: Get http://10.244.0.131:8080/health/live: dial tcp 10.244.0.131:8080: connect: connection refused
  Normal   Killing    8m24s (x3 over 10m)   kubelet, 10.0.10.11  Container api-helidon-container failed liveness probe, will be restarted
  Warning  Unhealthy  5m54s (x12 over 10m)  kubelet, 10.0.10.11  Liveness probe failed: HTTP probe failed with statuscode: 503
```

[目次に戻る](#目次)
<br>

## § Tracing デモ (oracle.demo.tracing パッケージ)

**注意！ 4.x と、それ以前では実装が異なります**

## 4.x の Tracing デモ

[MicroProfile Telemetry 1.0](https://download.eclipse.org/microprofile/microprofile-telemetry-1.0/tracing/microprofile-telemetry-tracing-spec-1.0.html) ベースの実装に変更されています。
この仕様は、CNCFで策定された [OpenTelemetry](https://opentelemetry.io/) をMicroProfileアプリケーションで利用可能にするためのものです。

ローカルに Jaeger サーバを立てて、トレーシングを試します。

1. Jaeger の関連ライブラリを含めてアプリケーションをビルドして起動します。
    ```
    mvn -Pdb-h2,tracing-jaeger -DskipTests package
    java -Dotel.sdk.disabled=false -Dotel.traces.exporter=jaeger -jar target/helidon-mp-demo.jar
    ``` 

2. Jaeger を Docker コンテナとして起動します。
    ```
    demo/tracing/jaeger.sh
    ```
    UI は http://localhost:16686/ になります。

3. トレーシングができているか確認します。
    ```
    curl http://localhost:8080/jpa/country
    ```
    Jaeger から `/jpa/country` というスパンとネストされた `oracle.demo.jpa.CountryDAO.getCountries` というスパンが確認されます。


## 4.x より前のバージョンの Tracing デモ

Kubernetes に デモのPodを4つと、jaegerのPodをデプロイします。

```bash
# export environment variable as appropriate
export REMOTE_REPO_PREFIX=iad.ocir.io/some-tenant/some-additional-path/

# replace "${REMOTE_REPO_PREFIX}/helidon-mp-demo:latest" in open-tracing.yaml and apply
envsubst < demo/k8s/open-tracing.yaml | kubectl apply -f -
```

次のような状態になっているはずです。

```bash
$ kubectl get all -n demo
NAME                    READY   STATUS    RESTARTS   AGE
pod/helidon-mp-demo-0   1/1     Running   0          5m37s
pod/helidon-mp-demo-1   1/1     Running   0          25s
pod/helidon-mp-demo-2   1/1     Running   0          24s
pod/helidon-mp-demo-3   1/1     Running   0          24s
pod/jaeger              1/1     Running   0          24s

NAME                         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                                                            AGE
service/helidon-mp-demo-0    ClusterIP   10.96.108.118   <none>        8080/TCP                                                           23s
service/helidon-mp-demo-1    ClusterIP   10.96.50.135    <none>        8080/TCP                                                           23s
service/helidon-mp-demo-2    ClusterIP   10.96.98.247    <none>        8080/TCP                                                           23s
service/helidon-mp-demo-3    ClusterIP   10.96.85.112    <none>        8080/TCP                                                           23s
service/helidon-mp-demo-np   NodePort    10.96.49.26     <none>        8080:30080/TCP                                                     23s
service/jaeger               ClusterIP   10.96.42.231    <none>        5775/UDP,6831/UDP,6832/UDP,5778/TCP,16686/TCP,14268/TCP,9411/TCP   23s
service/jaeger-np            NodePort    10.96.147.52    <none>        16686:30086/TCP                                                    23s
```

ポート 30080 はHelidon、ポート 30086 はJaegerのUIとなっています。必要に応じて KubernetesのNodeにsshポートフォーワードして、ローカルからアクセスできるようにして下さい。  
ここで、リクエストをポストしてみます。

```bash
cat demo/tracing/request.json | curl -v -X POST -H "Content-Type:application/json" localhost:30080/tracing/invoke -d @-
```

そうすると [Jaegerでトレーシングされている様子](doc/images/jaeger-tracing.png) が分かります。

### ローカル Docker 環境で試す

以下のコマンドで同様のデモが可能です。ポートは 8080 (Helidon) と 16686 (Jaeger) になります。
```
$ demo/tracing/tracing-demo.sh [start | stop]
```

### OCI Application Performance Monitoring (APM) の Tracer を使う

上記のデモは、Jaeger を前提にしていますが、その他にも複数の Tracer を設定することができます。 Maven のプロファイルを使って Tracer を切り替えます。


| Tracer  | Maven プロファイル         |
|---------|---------------------------|
| Jaeger  | tracing-jaeger (default)  |
| Zipkin  | tracing-zipkin            |
| OCI APM | tracing-oci-apm           |


OCI APM 用の Tracer を使うと、トレーシングの情報やサーバーの各種メトリクスを OCI APM に送ることができます。OCI APM では、jaeger や zpkin と同様に[トレーシング可視化](doc/images/oci-apm-tracing.png)を行ったり（OCI, OKE, OS, JVM 等詳細な情報が参照可能）、HelidonのCPU/ヒープ使用状況などの[サーバー監視](doc/images/oci-apm-monitoring.png)ができます。詳しくは[ドキュメント](https://docs.oracle.com/en-us/iaas/application-performance-monitoring/doc/configure-apm-tracer.html)を参照して下さい。


設定の切り替えは、以下の要領で行って下さい。

<details>
<summary>1. application.yaml の編集</summary>
  
Jaeger, Zipkin の設定については[こちら](https://helidon.io/docs/v2/#/mp/tracing/01_tracing)を参照してください。  
OCI APM の場合の設定は、以下のようになります。

```yaml
tracing:
  enabled: true
  service: helidon-mp-demo
  name: "Helidon APM Tracer"
  data-upload-endpoint: <data upload endpoint of your OCI domain>
  private-data-key: <private data key of your OCI domain>
  collect-metrics: true # optional - default true
  collect-resources: true # optional - default true
```

data-upload-endpoint、private-data-key は、OCI APM の管理コンソールで取得できます。  
また、設定については、`tracing.data-upload-endpoint`, `tracing.private-data-key` を Dockerfile や K8s のマニフェストファイルの中で環境変数として渡すことも可能です。

</details>

<details>
<summary>2. Maven ビルド時のプロファイル指定 </summary>
  
```bash
mvn package # Jaegerの場合 (デフォルト)
mvn -P tracing-zipkin,db-h2 package # Zipkin の場合 
mvn -P tracing-oci-apm,db-h2 package # OCI APM の場合
```

</details>


[目次に戻る](#目次)
<br>

## § Metrics デモ (oracle.demo.metrics パッケージ)

Mwtrics には以下の3種類のスコープが存在します。

| スコープ      | 説明 |
|--------------|-------------------------------------------------------|
| base         | 全てのMicroProfile実装で提供しなければいけないメトリクス  |
| vendor       | ベンダ独自のメトリクス (optional)                       |
| application  | アプリケーション独自のメトリクス (optional)              |

REST エンドポイントは以下になります。

| Endpoint                         | Request Type | Supported Format  | Description                                         |
|----------------------------------|--------------|-------------------|-----------------------------------------------------|
| `/metrics`                       | GET          | JSON, OpenMetrics | 全ての登録されているメトリクスを返す                   |
| `/metrics/<scope>`               | GET          | JSON, OpenMetrics | 当該スコープに登録されているメトリクスを返す            |
| `/metrics/<scope>/<metric_name>` | GET          | JSON, OpenMetrics | 当該スコープ・名前に一致するメトリクスを返す            |
| `/metrics`                       | OPTIONS      | JSON              | 全ての登録されているメトリクスのメタデータを返す        |
| `/metrics/<scope>`               | OPTIONS      | JSON              | 当該スコープに登録されているメトリクスのメタデータを返す |
| `/metrics/<scope>/<metric_name>` | OPTIONS      | JSON              | 当該スコープ・名前に一致するメトリクスのメタデータを返す |


このデモでは @Metered を使ってメソッドのメトリクスを取得したり、@Metrics を使って特定のメトリクスを定義したりできます。

```bash
# @Metered のついたメソッドをコール
$ curl localhost:8080/mpmetrics/apple
APPLE
$ curl localhost:8080/mpmetrics/apple
APPLE
$ curl localhost:8080/mpmetrics/orange
ORANGE
$ curl localhost:8080/mpmetrics/orange
ORANGE
$ curl localhost:8080/mpmetrics/orange
ORANGE

# メトリクスを取得 oracle_demo_metrics_MetricsResource はクラス名を表している
$ curl -s localhost:8080/metrics | grep "^[^#].*_MetricsResource.*_total"
application_oracle_demo_metrics_MetricsResource_apple_total 2
application_oracle_demo_metrics_MetricsResource_orange_total 3
application_oracle_demo_metrics_MetricsResource_total 5

# このRESTコールの実装はAPIを使ってメトリクスのレジストリを参照している
$ curl localhost:8080/mpmetrics/count-total
5
```

[目次に戻る](#目次)
<br>

## § Fault Tolerance デモ (oracle.demo.ft パッケージ)

メソッドやクラスにアノテーションを付与して、障害発生時の振る舞いを設定することができます。

| アノテーション   |  機能 |
|-----------------|------|
| Timeout         | メソッド実行が指定の時間に達した場合、例外(TimeoutException)を発生させる                                                 |
| Retry           | メソッド実行の例外発生時、一定時間/回数処理を繰り返す                                                                    |
| Fallback        | メソッド実行の例外発生時、代替メソッドを呼び出す                                                                         |
| CircuitBreaker  | 例外発生が繰り返されるメソッドの実行を一時的に止めて、メソッドの処理を行う前に例外(CircuitBreakerOpenException)を発生させる  |
| Bulkhead        | メソッドの同時実行数や（非同期実行の際の）待機キューの長さが指定の数を超えた場合、例外(BulkheadException)を発生させる        |
| Asynchronous    | メソッド実行を非同期（別スレッド）で行う                                                                                |

ここでは Bulkhead と Circuit Breaker を試すことができます。

```java
    /*
     * micoroprofile-config.properties ファイル内で
     * oracle.demo.ft.FaultToleranceResource/bulkhead/Bulkhead/value=3
     * としているので、実際に許容される多重度は 3
     * フォーマット: <クラス名>/<メソッド名>/Bulkhead/value=<値>
     */
    @Bulkhead(1024) // - will be changed with Config property
    @GET @Path("/bulkhead")
    @Produces(MediaType.TEXT_PLAIN)
    public String bulkhead() {
        sleep(); // 2秒スリープ
        return "OK";
    }

    /*
     * ローリング・ウィンドウとなる連続した4回の呼び出しのうち3回(4xfailureRatio=0.75)が失敗した場合
     * サーキットはOpenとなる。サーキットは10秒間の間Openの状態を保ったのちHalf-Openに遷移し、
     * 以降5連続呼び出しが成功した場合にClosedとなる。そうでない場合は再びOpenに戻る。
     * 
     * @Bulkhead(3)としているので、4以上同時に呼び出された場合メソッド自体はエラーとなる
     */
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 10 * 1000, successThreshold = 5)
    @Bulkhead(3)
    @GET @Path("/circuit-breaker")
    @Produces(MediaType.TEXT_PLAIN)
    public String circuitBreaker(){
        sleep(); // 2秒スリープ
        return "OK";
    }
```

テスト用のクライアントも用意しています。

```bash
# usage: oracle.demo.ft.FaultToleranceTester -e <GETするURL> <同時呼び出し数>
$ java -cp ./target/helidon-mp-demo.jar oracle.demo.ft.FaultToleranceTester -e http://localhost:8080/ft/bulkhead 4
$ java -cp ./target/helidon-mp-demo.jar oracle.demo.ft.FaultToleranceTester -e http://localhost:8080/ft/circuit-breaker 6
```

Fault Tolerance のメトリクスも取得できます。

```bash
$ curl -s localhost:8080/metrics | grep "^[^#].*_FaultToleranceResource.*_circuitbreaker"
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_callsFailed_total 7
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_callsPrevented_total 5
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_callsSucceeded_total 22
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_closed_total_seconds 275.578616745
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_halfOpen_total_seconds 96.159223938
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_open_total_seconds 1.921960921
application_ft_oracle_demo_ft_FaultToleranceResource_circuitBreaker_circuitbreaker_opened_total 2
```

[目次に戻る](#目次)
<br>

## § Open API (oracle.demo.country パッケージ)

APIの仕様を定義する規約である Open API に基づいたRESTエンドポイントのメタデータを公開できます。
特に何もしなくても最低限の仕様情報は自動的に生成できますが、アノテーションを使って付加的な情報を付加することができます。
`oracle.demo.App`  や `oracle.demo.country.CountryResource` にアノテーションを付加しています。

```java
    @Operation(summary = "Find country by country code", description = "国コードから国情報を検索します")
    @APIResponses({ 
            @APIResponse(
                responseCode = "200", description = "国情報", 
                content = {@Content(
                                mediaType = "application/json", 
                                schema = @Schema(type = SchemaType.OBJECT, implementation = Country.class)
                            )}
                ),
            @APIResponse(responseCode = "401", description = "認証に失敗しました"),
            @APIResponse(responseCode = "404", description = "指定した国コードから国情報が見つかりませんでした") 
        })
    @GET
    @Path("/{countryId}")
    public Country getCountry(
                    @Parameter(
                        name = "countryId", 
                        description = "国際電話の国番号 - US=1, JP=81, etc.", 
                        style = ParameterStyle.DEFAULT, 
                        required = true
                        ) 
                    @PathParam("countryId") 
                    int countryId) {
        return countryService.getCountry(countryId);
    }
```

APIの情報は /openapi から取得できます。

```
$ curl localhost:8080/openapi
info: 
  title: Helidon MP Demo
  version: '2.2.0'
openapi: 3.0.1
paths:
  /country/{countryId}: 
    get: 
      parameters:
      - 
        in: path
        name: countryId
        required: true
        schema: 
          format: int32
          type: integer
      description: 国コードから国情報を検索します
      responses:
        '401': 
          description: 認証に失敗しました
        '404': 
          description: 指定した国コードから国情報が見つかりませんでした
        '200': 
          content:
            application/json: 
              schema: 
                properties:
                  countryId: 
                    format: int32
                    type: integer
                  countryName: 
                    type: string
                type: object
          description: 国情報
      summary: Find country by country code
```

### OpenAPI UI

Maven のプロファイル `openapi-ui` を指定してビルドすると OpenAPI のユーザーインタフェースを表示することができます。  
ブラウザから /openapi-ui/index.html にアクセスして下さい。


[目次に戻る](#目次)
<br>

## § MicroProfile Rest Client (oracle.demo.restclient パッケージ)

MicroProfile では RESTコールを行う「タイプセーフ」なクライアントAPIを規定しています。つまりJavaのメソッドを呼び出すと内部でREST呼び出しを行ってくれます。呼び出しのパラメータも返り値も全てJavaオブジェクトとして扱うことができ、RESTコールに関する手間を省きコーディング・ミスを減らすことができます。

このデモでは、まずインターフェースを定義し、これに JAX-RS のアノテーションを付与しています。

```java
@Path("/movies")
public interface MovieReviewService {

     @GET @Path("/")
     public Set<Movie> getAllMovies();

     @GET @Path("/{movieId}/reviews")
     public Set<Review> getAllReviews( @PathParam("movieId") String movieId );

     @GET @Path("/{movieId}/reviews/{reviewId}")
     public Review getReview( @PathParam("movieId") String movieId, @PathParam("reviewId") String reviewId );

     @POST @Path("/{movieId}/reviews")
     public String submitReview( @PathParam("movieId") String movieId, Review review );

     @PUT @Path("/{movieId}/reviews/{reviewId}")
     public Review updateReview( @PathParam("movieId") String movieId, @PathParam("reviewId") String reviewId, Review review );
 }
 ```

このインターフェースを利用して、サーバー実装とクライアント実装を行っています。同一のインターフェースを用いてサーバーとクライアントを実装することができるので、両者間でAPI実装の差異が生じることはありません。

```
# サーバの実装
┌────────────────────┐                    ┌────────────────────────────┐
│ MovieReviewService │ <-- implements --- │ MovieReviewServiceResource │
└────────────────────┘                    └────────────────────────────┘

# クライアントの実装
┌────────────────────┐                    ┌──────────────────────────────────────┐
│ MovieReviewService │ <--    uses    --- │ MovieReviewServiceRestClientResource │
└────────────────────┘                    └──────────────────────────────────────┘
```

REST Client オブジェクトの作成は RestClientBuilder を使って行います。

```java
MovieReviewService reviewSvc = RestClientBuilder.newBuilder().build(MovieReviewService.class);
```

以下のコマンドを実行してください。

```bash
# curl -> Rest Client -> Rest Server
$ curl "localhost:8080/restclient/1/submit-review?star=5&comment=great%21"
$ curl localhost:8080/restclient/1/reviews
```

[目次に戻る](#目次)
<br>

## § Security (oracle.demo.security パッケージ)

ユーザーに以下のロールがアサインされているとします。

| user | password  | admin role | user role |
|:-----|-----------|:----------:|:---------:|
| john | password1 | Y          | Y         |
| mary | password2 | Y          | N         |
| ken  | password3 | N          | N         |

設定ファイルで、ユーザーのBasic認証情報とロールを定義します。

```
security:
    providers:
    - abac:
    - http-basic-auth:
        realm: "helidon"
        users:
        - login: "john"
          password: "password1"
          roles: ["user", "admin"]
        - login: "mary"
          password: "password2"
          roles: ["user"]
        - login: "ken"
          password: "password3"
```

`oracle.demo.security.SecurityResource` クラスでは、メソッドにアノテーションを付与することによってアクセスコントロールしています。

```java
    @Authenticated(optional = true) // any one can access
    @GET @Path("/public") public String getPublic() {}

    @Authenticated // needs log-in
    @GET @Path("/guest")  public String getGuest() {}

    @Authenticated @Authorized @RolesAllowed("admin") // needs admin role
    @GET @Path("/admin")  public String getAdmin() {}

    @Authenticated @Authorized @RolesAllowed("user") // needs user role
    @GET @Path("/user")   public String getUser() {}
```

<details open>
<summary>各々のRESTエンドポイントを異なるユーザーでGETしてみる</summary>

```bash
# unknown user -> /public - @Authenticated(optional = true) // any one can access
$ curl -v -u unknown:foo localhost:8080/security/basic/public
< HTTP/1.1 200 OK

# unknown user -> /guest - @Authenticated // needs log-in
$ curl -v -u unknown:foo localhost:8080/security/basic/user
< HTTP/1.1 401 Unauthorized

# ken -> /guest
$ curl -v -u ken:password3 localhost:8080/security/basic/guest
< HTTP/1.1 200 OK

# ken -> /guest with wrong password
$ curl -v -u ken:password localhost:8080/security/basic/user
< HTTP/1.1 401 Unauthorized

# ken -> /admin - @Authenticated @Authorized @RolesAllowed("admin")
$ curl -v -u ken:password3 localhost:8080/security/basic/admin
< HTTP/1.1 403 Forbidden

# ken -> /user - @Authenticated @Authorized @RolesAllowed("user")
$ curl -v -u ken:password3 localhost:8080/security/basic/user
< HTTP/1.1 403 Forbidden

# mary (user role) -> /admin
$ curl -v -u mary:password2 localhost:8080/security/basic/admin
< HTTP/1.1 403 Forbidden

# mary (user role) -> /user
$ curl -v -u mary:password2 localhost:8080/security/basic/user
< HTTP/1.1 200 OK

# John (admin role) -> /admin
$ curl -v -u john:password1 localhost:8080/security/basic/admin
< HTTP/1.1 200 OK
```
</details>
<br>

### Helidon の提供するセキュリティ・プロバイダ

以下のプロバイダを設定することによって、Basic 認証だけでなく、様々なアクセスコントロールが可能です。
* JWT Provider
* HTTP Basic Authentication
* HTTP Digest Authentication
* Header Assertion
* HTTP Signatures
* ABAC Authorization
* Google Login Authentication Provider
* OIDC (OpenID Connect) Authentication Provider
* IDCS (Oracle Identity Cloud Service) Role Mapping Provider

[目次に戻る](#目次)
<br>

## § JPA (Java Persistence API) デモ (oracle.demo.jpa パッケージ)

Helidon は拡張機能として Java Persistence API (JPA) と Java Transaction API (JTA) をサポートしています。   
このデモでは
+ H2 Database JDBC Driver (DataSource)
+ Hikari Connection Pool Extension
+ EclipseLink JPA Extension
+ JTA Extension

の組み合わせで、データベースへの CRUD 処理を行っています。トランザクション処理 (commit/rollback) は コンテナ(Helidon) が管理します。

```java 
    @PersistenceContext(unitName = "Demo")
    private EntityManager em;

    @Transactional
    public void updateCountry(int countryId, String countryName) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        country.setCountryName(countryName);
        em.persist(country);
    }
```

データベース操作の実行

```bash
# select
curl localhost:8080/jpa/country/ # [{"countryId":1,"countryName":"USA"},{"countryId":81,"countryName":"Japan"}]

# insert
curl -X POST -H "Content-Type: application/json" localhost:8080/jpa/country \
   -d '[{"countryId":86,"countryName":"China"}]'
curl localhost:8080/jpa/country/86 # {"countryId":86,"countryName":"China"}

# update
curl -X PUT -H "Content-Type: application/x-www-form-urlencoded" localhost:8080/reactive/jpa/1 \
  -d "name=United States"
curl localhost:8080/jpa/country/1 # {"countryId":1,"countryName":"United States"}

# delete
curl -X DELETE localhost:8080/jpa/country/86
curl -v localhost:8080/jpa/country/86 # 404 Not Found
```

### 接続先を H2 Databse から Oracle Database に変更するには？

デフォルトでは、組み込みの H2 Database に接続するようになっていますが、接続先を Oracle Database をはじめ任意の JDBC 接続に変更できます。

1. Maven のプロファイル `db-oracle` を指定して package します。これにより JDBC 関連ライブラリが Oracle JDBC に切り替わります。
```bash
$ mvn -P db-oracle package -DskipTests=true
```
2. システムプロパティ `DEMO_DATASOURCE=OracleDataSource` を指定して Java を実行します (環境変数でも可)。
```bash
$ java -jar -DDEMO_DATASOURCE=OracleDataSource target/helidon-mp-demo.jar
```

application.yaml でデータソースを設定してビルドすることもできます(システムプロパティや環境変数は実行時にこの設定を上書きする)。  
内部的にはMicroProfile Config APIを使って、起動時に `DemoDataSource` をダイナミックに構成するようになっています (io.helidon.config.Config.DSConfigSource クラスと META-INF/org.eclipse.microprofile.config.spi.ConfigSource を使って MicroProfile の仕様に基づいた Config の拡張を行っています)。そして、META-INF/persistence.xml 内で `DemoDataSource` が参照されています。
Oracle だけでなく任意の DataSource/JDBC Driver を構成できます (JDBCドライバのライブラリは必要です)。

```yaml
javax:
    sql:
        DataSource:
            H2DataSource: 
                dataSourceClassName: org.h2.jdbcx.JdbcDataSource
                dataSource:
                    url: jdbc:h2:mem:greeting;INIT=RUNSCRIPT FROM 'classpath:createtable.ddl' 
                    user: sa
                    password: ""
            OracleDataSource:
                dataSourceClassName: oracle.jdbc.pool.OracleDataSource
                dataSource:
                    url: jdbc:oracle:thin:@abc_high?TNS_ADMIN=/tnsdir
                    user: scott
                    password: tiger

DEMO_DATASOURCE: OracleDataSource # default: H2DataSource
```

### テスト用の Oracle Database インスタンスを作成するには？ 

デモ用に設定済みの Oracle Database インスタンスを Docker コンテナで実行するためのスクリプトを用意しています。

<details>
<summary>0. (必要に応じて) Oracle コンテナ・レジストリへのログイン</summary>

  
事前に `docker login container-registry.oracle.com` を済ませておいて下さい。  
[ポータル](https://container-registry.oracle.com/) にてソフトウェア利用許諾契約 (Oracle Standard Terms and Restrictions) の確認が必要です。
</details>

<details>
<summary>1. demo/oracledb/start-oracledb.sh の実行</summary>
  
Oracle Database の公式コンテナ・イメージを取得して起動します。  
 `docker logs`を確認してデータベースが起動するまで待機して下さい。`DATABASE IS READY TO USE!` が表示されたらOKです。

```
$ docker logs -f oracledb
...
...
#########################
DATABASE IS READY TO USE!
#########################
```
</details>

<details>
<summary>2. demo/oracledb/populate-demodata.sh の実行</summary>

デモ用のユーザーとテーブルを作成します。
| | |
|-------|--------------------|
| PDB   | PDB1               |
| User  | DEMO               |
| Table | GREETINGS, COUNTRY |
</details>

<details>
<summary>停止、起動、削除</summary>

```bash
# 停止
$ docker stop oracledb

# 起動
$ docker start oracledb

# 削除
$ docker stop oracledb
$ docker rm oracledb
```

</details>

[目次に戻る](#目次)
<br>

## § gRPC デモ (oracle.demo.grpc パッケージ)

**注意！ 4.x には実装がありません**

Helidon MP はアノテーションを使って簡単に gRPC サーバーを実装することができます。  
gRPCの転送データのフォーマットである protobuf を用意する必要がありますが、このデモでは、ビルド時の `mvn -P protoc initialize` で必要な Java ソースファイルを生成しています。

```bash
# REST -> gRPC Client -> gRPC Server と呼び出される

$ curl localhost:8080/grpc-protobuf/client
Hello world

```

注: 2.3.0 から Java シリアライゼーションを用いた方法は depricated になりました。


### protobuf版 (oracle.demo.grpc.protobuf パッケージ) に関する補足

[gRPC Java Quickstart](https://grpc.io/docs/languages/java/quickstart/) と同じprotoファイルを用いて、互換性のある実装を行っていますので、Quickstart で作成したクライアントから Helidon の gRPC サーバーを呼び出すことができます。  

protobuf ペイロードを使ったサーバー実装は更に POJO + Annotaion を使った方法と、GrpcMpExtension を使って従来型のサービス実装クラスをデプロイする方法の、2種類を提供しています。おすすめは POJO + Annotaion です。

1. POJO + Annotaion を使った方法（デフォルト 有効）  
Helidonが提供するアノテーションを使って、シンプルなコーディングができます。
```java
@Grpc(name = "helloworld.Greeter")
@ApplicationScoped
public class GreeterSimpleService{

    @Unary(name = "SayHello")
    public HelloReply sayHello(HelloRequest req) {
        System.out.println("gRPC GreeterSimpleService called - name: " + req.getName());
        return HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
    }
}
```
 * 関連するファイル
```text
oracle.demo.grpc.protobuf.GreeterSimpleService
```

2. GrpcMpExtensionを使って従来型のサービス実装クラスをデプロイする方法（デフォルト 無効）  
protobufコンパイラで生成されたJavaクラスを直接使用する方式です。
```java
class GreeterService extends GreeterGrpc.GreeterImplBase { 
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> observer) {
        System.out.println("gRPC GreeterService called - name: " + req.getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        observer.onNext(reply);
        observer.onCompleted();        
    }
}
```
 * 関連するファイル
```text
oracle.demo.grpc.protobuf.GreeterService
oracle.demo.grpc.protobuf.GrpcExtension
META-INF/services/io.helidon.microprofile.grpc.server.spi.GrpcMpExtension
```

<details>
<summary>実装の切り替え方 ( POJO + Annotaion 方式 → GrpcMpExtension 方式 )</summary>

1. META-INF/services/io.helidon.microprofile.grpc.server.spi.GrpcMpExtension を編集する
```text
# コメントアウトを外す
# oracle.demo.grpc.protobuf.GrpcExtension
oracle.demo.grpc.protobuf.GrpcExtension
```

2. oracle.demo.grpc.protobuf.GreeterSimpleService を編集する
```java
// @Grpc アノテーションをコメントアウトする
// @Grpc(name = "helloworld.Greeter")
@ApplicationScoped
public class GreeterSimpleService{

    @Unary(name = "SayHello")
    public HelloReply sayHello(HelloRequest req) {
        System.out.println("gRPC GreeterSimpleService called - name: " + req.getName());
        return HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
    }
}
```
</details>
<br>

### gRPC - protoファイルのコンパイルについて

pom.xml の通常ビルドフェーズとは独立してprotoファイルのコンパイルを行うプロファイルを定義しています。
[ビルド方法](#ビルド方法) にあるとおり、protoc を使ってまず最初に proto ファイルから Java ソースを生成して、srcディレクトリにコピーをします。詳細は、pom.xml の内容を確認して下さい。

[目次に戻る](#目次)
<br>

## § MicroProfile Reactive Messaging デモ (oracle.demo.reactive パッケージ)

JPA/JDBC経由でデータベースにアクセスするデモ(oracle.demo.jpaパッケージ)のバリエーションとして、[MicroProfile Reactive Messaging](https://download.eclipse.org/microprofile/microprofile-reactive-messaging-2.0/microprofile-reactive-messaging-spec-2.0.html) を使ったデータベースの非同期更新(Event Sourcing)処理を実装しています。RESTでリクエストを受け付けた後、非同期更新イベントを発行します。  


```bash
# insert
curl -X POST -H "Content-Type: application/json" http://localhost:8080/reactive/country \
   -d '[{"countryId":86,"countryName":"China"}]'
curl http://localhost:8080/jpa/country/86 # {"countryId":86,"countryName":"China"}

# update
curl -X PUT -H "Content-Type: application/x-www-form-urlencoded" http://localhost:8080/reactive/country/86 \
  -d "name=People's Republic of China"
curl http://localhost:8080/jpa/country/86 # {"countryId":86,"countryName":"People's Republic of China"}

# delete
curl -X DELETE http://localhost:8080/reactive/country/86
curl -v http://localhost:8080/jpa/country/86 # 404 Not Found
```

ReactiveResource.Java が上記の実装ですが、 @Outgoing("dao-event") アノテーションがついたメソッド（メッセージを作成する役割）と @Incoming("dao-event") アノテーションがついたメソッド（メッセージを消費する役割）が "dao-event" チャネルを介してメッセージ (DaoEvent) を非同期に送受信しています。

```
┌────────────────────────┐                              ┌────────────────────────┐
│ @Outgoing("dao-event") │                              │ @Incoming("dao-event") │
│ preparePublisher()     │ --- (Reactive Messaging) --- │ consume()              │
└────────────────────────┘                              └────────────────────────┘
```


### Kafka Connector を使った非同期メッセージング

MicroProfile Reactive Messaging には Connector という仕様があります。既に出来上がった Connector を使って様々な外部システムと簡単に連携できますし、また自分で新しい Connector を開発して提供することも可能です。Helidon では Apache Kafka、Java Messaging Service (JMS)、Oracle Database Advanced Queueing (AQ) に対応した Connector を提供しています。
ここでは、Kafka Connector を使って OCI Streaming の Kafak 互換API を介したメッセージの送受信を行ってみます。
前述のデモでは非同期更新イベントを JVM 内で受け渡ししていましたが、このデモでは更新イベントの送受信は外部のメッセージ・ブローカーで行われます。送信側と受信側は異なる JVM 上に存在して構いません。


```
┌────────────────────────────┐               ┌───────┐               ┌───────────────────────────┐
│ @Outgoing("connector-out") │     Kafka     │ Kafka │     Kafka     │ @Incoming("connector-in") │
│ preparePublisher()         │ - Connector - │       │ - Connector - │ consume()                 │
└────────────────────────────┘               └───────┘               └───────────────────────────┘
```


まず、application.yaml を更新します。デフォルトの状態では、Mock Connector を使う設定になっていますが、これを Kafka Connector に変更し、OCI Streaming の [Kafka 互換 API](https://docs.oracle.com/ja-jp/iaas/Content/Streaming/Tasks/kafkacompatibility.htm) に接続できるようにパラメータを設定します。

```yaml
# Reactive Messaging - Kafka connector
mp.messaging:
  incoming.connector-in:
    #connector: helidon-mock
    connector: helidon-kafka
    topic: stream01 # same as connector-out
outgoing.connector-out:
    #connector: helidon-mock
    connector: helidon-kafka
    topic: stream01 # same as connector-in
  connector:
    helidon-kafka:
      bootstrap.servers: "streaming.us-phoenix-1.oci.oraclecloud.com:9092" # change endpoint as required
      sasl.jaas.config: ${SASL_JAAS_CONFIG}
```

ビルドする際は、Kafka Connector 関連のライブラリが含まれるように pom.xml 内の `kafka` プロファイルを追加して下さい。

```
mvn package -Pdb-h2,kafka -DskipTests=true
```

```bash
# insert
curl -X POST -H "Content-Type: application/json" http://localhost:8080/connector/country \
   -d '[{"countryId":86,"countryName":"China"}]'
curl http://localhost:8080/jpa/country/86 # {"countryId":86,"countryName":"China"}

# update
curl -X PUT -H "Content-Type: application/x-www-form-urlencoded" http://localhost:8080/connector/country/86 \
  -d "name=People's Republic of China"
curl http://localhost:8080/jpa/country/86 # {"countryId":86,"countryName":"People's Republic of China"}

# delete
curl -X DELETE http://localhost:8080/connector/country/86
curl -v http://localhost:8080/jpa/country/86 # 404 Not Found
```


## § MicroProfile GraphQL デモ (oracle.demo.graphql パッケージ)

JPA経由でデータベースのCRUD操作をRestで公開するコードは既に提供していましたが、これをMicroProfile GraphQL仕様にしたものを追加しました。  
スキーマは `/graphql/schema.graphql` から取得できます。


```graphql
type Country {
  countryId: Int!
  countryName: String!
}

type Mutation {
  deleteCountry(countryId: Int!): Int!
  insertCountries(countries: [CountryInput]): [Country]
  insertCountry(country: CountryInput): Country
  updateCountry(countryId: Int!, countryName: String): Country
}

type Query {
  countries: [Country]
  country(countryId: Int!): Country
}

input CountryInput {
  countryId: Int!
  countryName: String!
}
```

<details open>
<summary>curlでテストする場合は、以下を参考にして下さい</summary>
  
同様の操作は、GrapghQLのテストケース(CountryGraphQLApiTest.java)でも行っていますので、そちらも参考にしてください。

```bash
# query: countries: [Country]
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "query { countries { countryId countryName } }" }'

# query: country(countryId: Int!): Country
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "query { country(countryId: 1) { countryName } }" }'

# mutation: insertCountry(country: CountryInput): Country
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { insertCountry (country:{countryId:86,countryName:\"China\"}) { countryId countryName } }" }'

# mutation: insertCountries(countries: [CountryInput]): [Country]
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { insertCountries (countries:[{countryId:82,countryName:\"Korea\"},{countryId:91,countryName:\"India\"}]) { countryId countryName } }" }'

# mutation: updateCountry(countryId: Int!, countryName: String): Country
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { updateCountry (countryId:1,countryName:\"United States\") { countryId countryName } }" }'

# mutation: deleteCountry(countryId: Int!): Int!
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { deleteCountry (countryId:86) }" }'
```
</details>

クエリを実行する UI も提供しています。ブラウザから `http://localhost:8080/graphql/ui/` にアクセスして下さい。


[目次に戻る](#目次)
<br>

## § Mapped Diagnostic Context (Mdc) デモ (oracle.demo.logging パッケージ)

**注意！ 4.x には実装がありません**

Mapped Diagnostic Context (Mdc) は、並列処理で実行されるログ出力をトレースするために使うことができます。サーバーが複数のクライアントからのリクエストをマルチスレッドで処理する（=同じクラス＆メソッドのログ出力が入り乱れる）場合などに便利です。ログに実行スレッド名を出力することもできますが、単一のリクエストの処理が複数のスレッドにまたがって行われるようなケースでは、スレッドをまたがったトレースが困難になります。  

このデモでは、実行コンテキストIDの付与 (Execution Context ID = ECID) を Mdc を使って実装します。Mdcの設定・消去は CDI Intercepter (@Mdc アノテーション) を使っていますので、本来の業務ロジックの処理(=メソッドの中身)には影響を与えずに、メソッドの実行前後で Mdc 関連の処理を割り込ませています。
ECID がメソッドの実行時に存在しない場合、IDを新たに設定し、メソッド終了時に新規設定した ID を消去します。 ECID がメソッドの実行時に既に存在する場合、処理は行わずスルーします。

**/logging**  
- MdcResource#nomdc()
  - @Mdc Sub#sub()
    - CompletableFuture.supplyAsync()
      - Supplier() [lambda]  

**/logging/mdc**  
- @Mdc MdcResource#mdc()
  - @Mdc Sub#sub() 
    - CompletableFuture.supplyAsync()
      - Supplier() [lambda]  

logging.properties では HelidonConsoleHandler を使い、%X{<キー>} で Mdc を出力します。

```conf
handlers=io.helidon.logging.jul.HelidonConsoleHandler
java.util.logging.SimpleFormatter.format=!thread! ECID\{%X{ECID}\}: %5$s%6$s%n
```

2つのエンドポイントに GET してみます。

```
curl http://localhost:8080/logging # MdcResource#nomdc()
// ログ出力
MdcResource Thread[helidon-1,5,server]{}: Invoking Sub#get()
Sub Thread[helidon-1,5,server]{4cfbec87bab0829c}: Sub#get() called
Sub Thread[sub-1,5,helidon-thread-pool-7]{4cfbec87bab0829c}: Thread started
Sub Thread[helidon-1,5,server]{4cfbec87bab0829c}: Thread ended
MdcResource Thread[helidon-1,5,server]{}: Ended Sub#get()

curl http://localhost:8080/logging/mdc # @Mdc MdcResource#mdc()
// ログ出力
MdcResource Thread[helidon-2,5,server]{8cd601a8d345d884}: Invoking Sub#get()
Sub Thread[helidon-2,5,server]{8cd601a8d345d884}: Sub#get() called
Sub Thread[sub-2,5,helidon-thread-pool-7]{8cd601a8d345d884}: Thread started
Sub Thread[helidon-2,5,server]{8cd601a8d345d884}: Thread ended
MdcResource Thread[helidon-2,5,server]{8cd601a8d345d884}: Ended Sub#get()
```

@Mdc を付与していないメソッドでは ECID が発行されていないのがわかります。またスレッドを超えて ECID が伝播されているのも確認できます。これは Helidon の提供する ThreadPoolSupplier から作成された ExecutorService が、Helidon のランタイム内で保持しているグローバル・コンテキストをスレッド間で受け渡しするからです。
逆に言うと、通常の ExecutorService から作成される Thread では Helido のグローバル・コンテキストをそのままでは認識できません。この場合 `io.helidon.common.context.Contexts#wrap()` メソッドでグローバル・コンテキストに対応することができます。

```
ExecutorService es = Contexts.wrap(Executors.newSingleThreadExecutor());
# now es is aware of helidon's global context
```

ECID は 並行処理される実行ログの中から、リクエスト単位のログを識別するのに役立ちます。

```
# Fault Tolerance のデモで使った「複数リクエスト同時発射装置」で試してみる
java -cp ./target/helidon-mp-demo.jar oracle.demo.ft.FaultToleranceTester -e http://localhost:8080/logging/mdc 3
// ログ出力
Thread[helidon-4,5,server]{699595b3c0a746ff}: Invoking Sub#get()
Thread[helidon-4,5,server]{699595b3c0a746ff}: Sub#get() called
Thread[helidon-5,5,server]{a00dc58b026dec6c}: Invoking Sub#get()
Thread[sub-3,5,helidon-thread-pool-7]{699595b3c0a746ff}: Thread started
Thread[helidon-5,5,server]{a00dc58b026dec6c}: Sub#get() called
Thread[helidon-3,5,server]{2e2f6112b8ec0330}: Invoking Sub#get()
Thread[sub-4,5,helidon-thread-pool-7]{a00dc58b026dec6c}: Thread started
Thread[helidon-5,5,server]{a00dc58b026dec6c}: Thread ended
Thread[helidon-5,5,server]{a00dc58b026dec6c}: Ended Sub#get()
Thread[helidon-3,5,server]{2e2f6112b8ec0330}: Sub#get() called
Thread[sub-5,5,helidon-thread-pool-7]{2e2f6112b8ec0330}: Thread started
Thread[helidon-3,5,server]{2e2f6112b8ec0330}: Thread ended
Thread[helidon-3,5,server]{2e2f6112b8ec0330}: Ended Sub#get()
Thread[helidon-4,5,server]{699595b3c0a746ff}: Thread ended
Thread[helidon-4,5,server]{699595b3c0a746ff}: Ended Sub#get()
```

### (応用編) ECID による Oracle Database との連携 (oracle.demo.jpa.ecid パッケージ)

Helidon で設定した Mdc を Oracle Database の Execution Context ID (ECID) として連携してみます。さらに、このデモでは ECID として Open Tracing の Trace ID が利用できる場合はそれを利用するように実装していますので、RESTの最初の入り口から Database の SQL まで end-to-end でトレーシングが可能になります。  
Oracle Database の JDBCドライバは ECID を受け取るための標準的な方法を提供しています。JDBC クライアントは以下のような形で 実行中のセッションに ECID を設定できます。  

```
String ecid = ...
java.sql.Connection con = ...
con.setClientInfo("OCSID.ECID", ecid);
```

oracale.demo.jpa.ecid.EcidExampleResource で定義されてる二つのエンドポイント (/ecid/insert, ecid/update) は、意図的に完了時間を遅くするストアード・プロシージャを呼び出します。実装は以下のようになっており、@Mdc アノテーションによってこのメソッドに入るタイミングで Mdc がセットされ、さらに @Ecid アノテーションによって特定の条件に合致する場合に JDBC Connection 経由で ECID が設定されます。

```java
    @GET @Path("insert") @Produces("text/plain") // JAX-RS
    @Transactional // JTA
    @Mdc // set Mdc
    @Ecid // set ECID when available
    public String insertCountry(
      @QueryParam("id") Integer id, @QueryParam("name") String name, @QueryParam("delay") Integer delay) {
        logger.info(String.format("Insert (id = %d, name = %s, delay=%d)", id, name, delay));
        em.createStoredProcedureQuery("DEMO.INSERT_COUNTRY")
            .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
            .setParameter(1, id)
            .setParameter(2, name)
            .setParameter(3, Optional.ofNullable(delay).orElse(30))
            .execute();
        return "OK\n";
    }
```

では完了に60秒かかる INSERT 処理を呼び出してみます。

```
$ curl "http://localhost:8080/ecid/insert?id=9002&name=Test&delay=60"
```

Helidon のログには Insert 処理の ECID {a32f6112b8ec0350} が出力されています。

```
2021.01.24 03:42:31 INFO oracle.demo.jpa.ecid.EcidExampleResource Thread[helidon-1,5,server]{a32f6112b8ec0350}: Insert (id = 9002, name = Test, delay=60)
```

では、このオペレーションが完了する前に、v$session で ECID が伝達されているか確認します。

```
SQL> select username, ecid, sql_id 
      from v$session 
      where ecid = 'a32f6112b8ec0350';

USERNAME ECID             SQL_ID
-------- ---------------- -------------
DEMO     a32f6112b8ec0350 fdw79cubmrrxz
```

実行された SQL_ID も分かりますので、実行に関する統計情報も確認できます。 

```
SQL> select sql_id, executions, elapsed_time, sql_text
      from v$sql
      where sql_id = 'fdw79cubmrrxz';

SQL_ID        EXECUTIONS ELAPSED_TIME
------------- ---------- ------------
SQL_TEXT
--------------------------------------------------------------------------------
fdw79cubmrrxz          2         6788
BEGIN DEMO.INSERT_COUNTRY(:1 , :2 , :3 ); END;
```

[目次に戻る](#目次)
<br>

## § Scheduling デモ (oracle.demo.scheduling パッケージ)

@Scheduled または @FixedRate アノテーションを使って、定期実行するタスクをスケジュールできます。

```java
    @FixedRate(initialDelay = 2, value = 3, timeUnit = TimeUnit.MINUTES)
    public void fixedRate0(FixedRateInvocation inv) {
        logger.info(inv.description());
    }    

    @Scheduled("0/30  * * ? * *")
    private void scheduled0(CronInvocation inv){
        logger.info(inv.description());
    }

    @Scheduled("15,45 * * ? * *")
    private void scheduled1(CronInvocation inv){
        logger.info(inv.description());
    }
```

上記のようなアノテーションをつけたメソッドを定義しておくと、以下のようなタスク実行結果となります。

```
...
10:00:45 INFO oracle.demo.scheduling.Scheduler Thread[scheduled-2,5,main]: at 15 and 45 seconds
10:01:00 INFO oracle.demo.scheduling.Scheduler Thread[scheduled-6,5,main]: every 30 seconds
10:01:11 INFO oracle.demo.scheduling.Scheduler Thread[scheduled-3,5,main]: every 3 minutes with initial delay 2 minutes
10:01:15 INFO oracle.demo.scheduling.Scheduler Thread[scheduled-7,5,main]: at 15 and 45 seconds
10:01:30 INFO oracle.demo.scheduling.Scheduler Thread[scheduled-4,5,main]: every 30 seconds
...
```

注: ログの出力がうるさいので、ソースのアノテーションをコメントアウトしています(=デフォルトではスケジューリングされていません)。デモする場合は、oracle.demo.scheduling.Scheduer.java のコメントアウトを外して下さい。


[目次に戻る](#目次)
<br>

## § MicroProfile LRA デモ (oracle.demo.lra パッケージ)

[MicroProfile LRA (Long Running Actions)](https://projects.eclipse.org/projects/technology.microprofile/releases/lra-1.0) とは、分散環境での一貫性を保証するための手法の1つで、マイクロサービスにおける SAGA pattern（非同期通信、分散ロックなし、補償アクションを使ったリカバリ）を実現する仕様です。
このデモでは、仕様ドキュメントに記載のある[補償トランザクションのパターン](https://download.eclipse.org/microprofile/microprofile-lra-1.0-M1/images/lra.png)を試します。

### (事前作業) LRA Coordinator の作成＆起動

LRA の実行には、サービス間のトランザクションを管理するコーディネータが介在します。まずのこのコーディネータの Docker コンテナを作成して起動します。

```sh
# Cordinator のコンテナイメージを作成
$ demo/lra/create_lra_coordinator_image.sh
...
Successfully built xxxxxxxxxxxx
Successfully tagged helidon/lra-coordinator:latest

# Coordinator を起動
$ docker run --rm -d --name lra-coordinator -p 8070:8070 --network="host" helidon/lra-coordinator
...
...

# 起動を確認
$ docker ps -f "name=lra-coordinator" --format "{{.Names}} - {{.Status}}"
lra-coordinator - Up 10 seconds
```

--network="host" でコンテナを起動していることに注意して下さい（現在の実装の制約です）。

それでは、以降で実際に LRA を試してみます。LRAMain, LRAService1, LRAService2 の3つのサービスがトランザクションに関係します。
LRAMain がトランザクションを開始し、LRAService1, LRAService2 がそのトランザクションに参加します。

1. クライアント (curl) が LRAMain を呼び出す (body = LRAMainから呼び出すサービスのリスト)
2. LRAMain でトランザクションが開始
3. LRAMain が LRAService1, LRAService2 を呼び出す
4. LRAService1, LRAService2 はトランザクションに参加し、処理（実際は何もしない）を行いリターン
5. LRAMain は LRAService1, LRAService2 呼び出し後、リターンするタイミングでトランザクションも終了
6. クライアントがレスポンスを受け取る

### ソースの解説

LRAMain#start でトランザクションが開始されます。

```java
  @LRA(
      value = LRA.Type.REQUIRES_NEW,
      timeLimit = 3000, timeUnit = ChronoUnit.MILLIS
  )
  @POST @Path("start")
  public Response start(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String[] urls, 
                                            @QueryParam("raise-error") boolean raiseError){
    ...
  }
```

LRAService1, LRAService2 の各メソッドは、LRAMain で開始されたトランザクションに参加します。

```java
  @LRA(value = LRA.Type.REQUIRED, end=false)
  @GET @Path("serv")
  public Response serve(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
    ...
  }
```

トランザクションの結果は、LRAMain の @AfterLRA のアノテーションのついたメソッドに通知されます。

```java
  @AfterLRA
  @PUT @Path("after")
  public Response notifyLRAFinished(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, LRAStatus status) {
    ...
  }
```

end=false なので、トランザクションは 呼び出し元である LRAMain の後続処理まで継続されます。

### 正常パターン

クライアントからリクエスト送信

```bash
cat <<EOF | curl -v -H "Content-Type: application/json" http://localhost:8080/lra-main/start -d @-
[
  "http://localhost:8080/lra-service1/serv",
  "http://localhost:8080/lra-service2/serv"
]
EOF
< HTTP/1.1 200 OK
< Long-Running-Action: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65

OK
```

サーバーログ

```txt
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 started
INFO LRAMain : http://localhost:8080/lra-service1/serv <- calling
INFO LRAService1 : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 joined
INFO LRAService1 : Done.
INFO LRAMain : http://localhost:8080/lra-service1/serv -> 200 OK
INFO LRAMain : http://localhost:8080/lra-service2/serv <- calling
INFO LRAService2 : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 joined
INFO LRAService2 : Done.
INFO LRAMain : http://localhost:8080/lra-service2/serv -> 200 OK
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 completed 🎉
INFO LRAService1 : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 completed 🎉
INFO LRAService2 : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 completed 🎉
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/daa70c14-4963-4775-b1e6-0b0e97f4bc65 ended with status "Closed"
```

各サービスの @Complete アノテーションのついたメソッドが呼び出されています。


### 異常（Exception による補償トランザクション起動）パターン

LRAMain がリターン直前に RuntimeException を throw するシナリオ

```bash
cat <<EOF | curl -v -H "Content-Type: application/json" http://localhost:8080/lra-main/start?raise-error=true -d @-
[
  "http://localhost:8080/lra-service1/serv",
  "http://localhost:8080/lra-service2/serv"
]
EOF
< HTTP/1.1 500 Internal Server Error
< Long-Running-Action: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42

Error by request
```

サーバーログ

```txt
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d started
INFO LRAMain : http://localhost:8080/lra-service1/serv <- calling
INFO LRAService1 : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d joined
INFO LRAService1 : Done.
INFO LRAMain : http://localhost:8080/lra-service1/serv -> 200 OK
INFO LRAMain : http://localhost:8080/lra-service2/serv <- calling
INFO LRAService2 : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d joined
INFO LRAService2 : Done.
INFO LRAMain : http://localhost:8080/lra-service2/serv -> 200 OK
WARNING io.helidon.microprofile.server.JaxRsCdiExtension Thread[helidon-6,5,server]: Internal server error
java.lang.RuntimeException
        at oracle.demo.lra.LRAMain.start(LRAMain.java:66)
        ...

SEVERE LRAMain : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d compensated 🚒
SEVERE LRAService1 : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d compensated 🚒
SEVERE LRAService2 : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d compensated 🚒
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/2180a5a8-e39c-4123-a187-d5e62729b42d ended with status "Cancelled"
```

今度は、各サービスの @Compensate アノテーションのついたメソッドが呼び出されています。

### 異常（タイムアウトによる補償トランザクション起動）パターン

LRAService2 はトランザクションのタイムアウト値 (3000ms) を超える処理遅延が生じるシナリオ

```bash
cat <<EOF | curl -v -H "Content-Type: application/json" http://localhost:8080/lra-main/start -d @-
[
  "http://localhost:8080/lra-service1/serv",
  "http://localhost:8080/lra-service2/serv-slow"
]
EOF
< HTTP/1.1 200 OK
< Long-Running-Action: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908

OK
```

サーバーログ

```txt
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 started
INFO LRAMain : http://localhost:8080/lra-service1/serv <- calling
INFO LRAService1 : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 joined
INFO LRAService1 : Done.
INFO LRAMain : http://localhost:8080/lra-service1/serv -> 200 OK
INFO LRAMain : http://localhost:8080/lra-service2/serv-slow <- calling
INFO LRAService2 : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 joined
SEVERE LRAMain : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 compensated 🚒
SEVERE LRAService1 : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 compensated 🚒
SEVERE LRAService2 : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 compensated 🚒
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/012167b9-8d1f-464d-8de7-1bd73aa9d908 ended with status "Cancelled"
INFO LRAService2 : Done.
INFO LRAMain : http://localhost:8080/lra-service2/serv-slow -> 200 OK
```
LRAMain の `/lra-main/start` リクエスト自体は正常終了して、クライアントにも 200 OK が返っていますが、タイムアウトによりトランザクションはキャンセルされ、補償トランザクションが呼び出されています。LRAMain は同期的に呼び出した LRAService2 のリターンを待っている間に LRA トランザクションコーディネータからタイムアウトをきっかけにした補償トランザクションの呼び出しを受け、更に最終的な "Cancelled" のステータスの通知を受けています。この非同期のイベントは `/lra-main/start` リクエスト処理を完了した後に受信される可能性もあります。

上記にあるとおり、 `/lra-main/start` を呼び出したレスポンスには `Long-Running-Action` というヘッダにトランザクションIDがセットされています。ではこの ID を使って、LRA トランザクションコーディネータからコールバックされる最終的なステータスを同期的に待ち受けてみましょう。 `/lra-main/monitor` は 間接的に `/lra-main/start` を呼び出した後に LRA コーディネータからのトランザクションステータスの通知イベントを待って、その LRA トランザクションの最終的なステータスをレスポンスの文字列として返します。クライアントへのレスポンスコードは、トランザクションが "Closed" の時だけ 200 を返します。

```bash
cat <<EOF | curl -v -H "Content-Type: application/json" http://localhost:8080/lra-main/monitor -d @- 
[
  "http://localhost:8080/lra-service1/serv",
  "http://localhost:8080/lra-service2/serv-slow"
]
EOF
< HTTP/1.1 500 Internal Server Error

Cancelled
```

サーバログ

```txt
INFO LRAMain : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 started
INFO  LRAMain : http://localhost:8080/lra-service1/serv <- calling
INFO  LRAService1 : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 joined
INFO  LRAService1 : Done.
INFO  LRAMain : http://localhost:8080/lra-service1/serv -> 200 OK
INFO  LRAMain : http://localhost:8080/lra-service2/serv-slow <- calling
INFO  LRAService2 : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 joined
SEVERE LRAMain : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 compensated 🚒
SEVERE LRAService1 : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 compensated 🚒
SEVERE LRAService2 : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 compensated 🚒
INFO  LRAMain : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 ended with status "Cancelled"
INFO  LRAService2 : Done.
INFO  LRAMain : http://localhost:8080/lra-service2/serv-slow -> 200 OK
INFO  LRAMain : LRA initiator returned with status 200
INFO  LRAMain : LRA id: http://localhost:8070/lra-coordinator/791c52e5-7ebf-4266-93b3-d6c5b8d0eaa3 final status "Cancelled"
```

LRA トランザクションは非同期に行われ、クライアントの同期呼び出しにリアルタイムでトランザクション結果を返すことが適切でないケースが多いと考えられます。この場合クライアントからのリクエスト受付処理（トランザクションIDを返す）とクライアントからのリクエスト結果確認処理を分けて実装するのが望ましいです。

[目次に戻る](#目次)
<br>


## § （おまけ）Cowsay (oracle.demo.cowweb パッケージ)

https://github.com/ricksbrown/cowsay

```
$ curl localhost:8080/cowsay/say
 ______
< Moo! >
 ------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||

$ curl "localhost:8080/cowsay/think?message=Hello%21&cowfile=moose"
 ________
( Hello! )
 --------
  o
   o   \_\_    _/_/
    o      \__/
           (oo)\_______
           (__)\       )\/\
               ||----w |
               ||     ||

```
エンジョイ！

[目次に戻る](#目次)
<br>
---





