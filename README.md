![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/oracle-japan/ochacafe-microprofile-demo)

# [Helidon](https://helidon.io/) を使って [Eclipse MicroProfile](https://microprofile.io/) の仕様や拡張機能を確認するデモ

[OCHaCafe 2 - #4 Cloud Native時代のモダンJavaの世界](https://ochacafe.connpass.com/event/155389/) のために作成したデモですが、随時実装を追加しています。  
[**セッション・スライドはこちら**](http://tiny.cc/ochacafe-cn-java-slides)

## 目次

+ [ビルド方法](#■-ビルド方法)
+ [アプリケーションの起動](#■-アプリケーションの起動)
+ [Docker イメージの作成](#■-docker-イメージの作成)
+ [Health デモ](#■-microprofile-health-デモ-oracledemohealth-パッケージ)
+ [Open Tracing デモ](#■-open-tracing-デモ-oracledemotracing-パッケージ)
+ [Open Tracing 拡張](#■-opentracing-span定義のためのアノテーション-oracledemotracinginterceptor-パッケージ)
+ [Metrics デモ](#■-metrics-デモ-oracledemometrics-パッケージ)
+ [Fault Tolerance デモ](#■-fault-tolerance-デモ-oracledemoft-パッケージ)
+ [Open API デモ](#■-open-api-oracledemocountry-パッケージ)
+ [Rest Client デモ](#■-microprofile-rest-client-oracledemorestclient-パッケージ)
+ [Security デモ](#■-security-oracledemosecurity-パッケージ)
+ [JPA/Transaction デモ](#■-jpa-java-persistence-api-デモ-oracledemojpa-パッケージ)
+ [gRPC デモ](#■-grpc-デモ-oracledemogrpc-パッケージ)
+ [Reactive Messaging デモ](#■-microprofile-reactive-messaging-デモ-oracledemoreactive-パッケージ)
+ [GraphQL デモ](#■-microprofile-graphql-デモ-oracledemographql-パッケージ)
+ [おまけ](#■-おまけcowsay-oracledemocowweb-パッケージ)

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
│           │   ├── HealthCheckHelper.java
│           │   ├── HealthCheckResource.java
│           │   └── MyHealthCheck.java
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
│           │   ├── TracingResource.java
│           │   └── interceptor [SPAN定義 Interceptor & アノテーション]
│           │       ├── TraceInterceptor.java
│           │       ├── Trace.java
│           │       ├── TraceTagHolder.java
│           │       └── TraceTag.java
│           ├── reactive [Reactive Messaging & Connecter]
│           │   ├── DaoEvent.java
│           │   ├── ExecutorServiceHelper.java
│           │   ├── ReactiveJmsResource.java
│           │   └── ReactiveResource.java
│           ├── graphql [GraphQL]
│           │   ├── Country.java
│           │   └── CountryGraphQLApi.java
│           ├── jpa [拡張機能 JPA/JTA]
│           │   ├── CountryDAO.java
│           │   ├── Country.java
│           │   ├── CountryResource.java
│           │   ├── Greeting.java
│           │   └── JPAExampleResource.java
│           ├── grpc [拡張機能 gRPC]
│           │   ├── javaobj [gRPC Javaシリアライゼーション版]
│           │   │   ├── GreeterServiceImpl.java
│           │   │   ├── GreeterService.java
│           │   │   └── GrpcResource.java
│           │   └── protobuf [gRPC protobuf版]
│           │       ├── GreeterSimpleService.java
│           │       ├── GreeterService.java
│           │       ├── GrpcResource.java
│           │       └── helloworld
│           │           ├── GreeterGrpc.java
│           │           └── Helloworld.java
│           └── cowweb [おまけ]
│               └── CowwebResource.java
├── proto
│   └── helloworld.proto [gRPC IDL定義]
└── resources
    ├── application.yaml [Helidonの設定ファイル microprofile-config.properties 相当として利用可能]
    ├── createtable.ddl [JPA拡張機能で使うH2用のDDL]
    ├── jbossts-properties.xml [JTAの設定ファイル]
    ├── logging.properties [ログ設定ファイル]
    ├── META-INF
    │   ├── beans.xml [CDIの設定ファイル]
    │   ├── microprofile-config.properties [MicroProfile設定ファイル]
    │   ├── persistence.xml [JPAの設定ファイル]
    │   └── services
    │       └── io.helidon.microprofile.grpc.server.spi.GrpcMpExtension [gRPC Extension設定ファイル]
    └── WEB [静的コンテンツのフォルダー]
        └── index.html 
demo
├── k8s [kubernetesデプロイメント用マニフェスト]
│   ├── liveness-check.yaml
│   ├── open-tracing.yaml
│   ├── simple-deployment.yaml
│   └── simple-service.yaml
├── tracing [トレーシングデモ]
│   ├── request.json
│   └── tracing-demo.sh
└── weblogic [WebLogic Server コンテナ作成・設定]
    ├── config-jms.sh
    ├── config-jms.yaml
    ├── domain.properties
    └── start-weblogic.sh
```

## ■ ビルド方法

```bash
# for the first time, generate java source files for gRPC by compiling proto file
mvn clean -P protoc generate-sources
# then create jar
mvn package
```

## ■ アプリケーションの起動

```bash
java -jar target/helidon-demo-mp.jar
```

## ■ Docker イメージの作成

Dockerfileを使わずに、[Jib](https://github.com/GoogleContainerTools/jib) を使ってMavenから直接イメージをビルドします.  
ルートディレクトリにあるDockerfileを使ってもイメージの作成は可能です.

### 通常（ローカル）のタグを付与する場合

```bash
mvn post-integration-test -DskipTests=true # 便宜上post-integration-testにアサインしているだけ
```

### リモート用のタグを付与する場合

環境変数 REMOTE_REPO_PREFIX を設定した後、Mavenのプロファイルを指定して実行します。

```bash
# export environment variable as appropriate
export REMOTE_REPO_PREFIX=iad.ocir.io/some-tenant/some-additional-path/

mvn -P remote-repo-prefix post-integration-test -DskipTests=true
```

ローカルリポジトリに作成されたイメージをリポートリポジトリにpushします.

```bash
$ docker images
REPOSITORY                                          TAG                 IMAGE ID            CREATED             SIZE
helidon-demo-mp                                     2.0-SNAPSHOT        1b4d2e82f64a        49 years ago        125MB
helidon-demo-mp                                     latest              1b4d2e82f64a        49 years ago        125MB
(remote docker repository prefix/)helidon-demo-mp   2.0-SNAPSHOT        116de0207be6        49 years ago        125MB
(remote docker repository prefix/)helidon-demo-mp   latest              116de0207be6        49 years ago        125MB

$ docker push (remote docker repository prefix/)helidon-demo-mp
```

<br>

## ■ MicroProfile Health デモ (oracle.demo.health パッケージ)

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

# replace "${REMOTE_REPO_PREFIX}/helidon-demo-mp:latest" in liveness-check.yaml and apply
envsubst < demo/k8s/liveness-check.yaml | kubectl apply -f -
```

ここで Pod の状態を定期的に確認すると、再起動されている ( RESTARTS がカウントアップされている) ことが分かります。 

```bash
$ kubectl get pod -n demo -w
NAME                     READY   STATUS    RESTARTS   AGE
helidon-demo-mp-health   1/1     Running   0          12s
helidon-demo-mp-health   1/1     Running   1          52s
helidon-demo-mp-health   1/1     Running   2          103s
helidon-demo-mp-health   1/1     Running   3          2m33s
```
```
$ kubectl describe pod helidon-demo-mp-health -n demo
(中略...)
Events:
  Type     Reason     Age                    From                 Message
  ----     ------     ----                   ----                 -------
  Normal   Started    9m13s (x3 over 10m)   kubelet, 10.0.10.11  Started container api-helidon-container
  Warning  Unhealthy  9m4s (x3 over 10m)    kubelet, 10.0.10.11  Liveness probe failed: Get http://10.244.0.131:8080/health/live: dial tcp 10.244.0.131:8080: connect: connection refused
  Normal   Killing    8m24s (x3 over 10m)   kubelet, 10.0.10.11  Container api-helidon-container failed liveness probe, will be restarted
  Warning  Unhealthy  5m54s (x12 over 10m)  kubelet, 10.0.10.11  Liveness probe failed: HTTP probe failed with statuscode: 503
```

<br>

## ■ Open Tracing デモ (oracle.demo.tracing パッケージ)

Kubernetes に デモのPodを4つと、jaegerのPodをデプロイします。

```bash
# export environment variable as appropriate
export REMOTE_REPO_PREFIX=iad.ocir.io/some-tenant/some-additional-path/

# replace "${REMOTE_REPO_PREFIX}/helidon-demo-mp:latest" in open-tracing.yaml and apply
envsubst < demo/k8s/open-tracing.yaml | kubectl apply -f -
```

次のような状態になっているはずです。

```bash
$ kubectl get all -n demo
NAME                    READY   STATUS    RESTARTS   AGE
pod/helidon-demo-mp-0   1/1     Running   0          5m37s
pod/helidon-demo-mp-1   1/1     Running   0          25s
pod/helidon-demo-mp-2   1/1     Running   0          24s
pod/helidon-demo-mp-3   1/1     Running   0          24s
pod/jaeger              1/1     Running   0          24s

NAME                         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                                                            AGE
service/helidon-demo-mp-0    ClusterIP   10.96.108.118   <none>        8080/TCP                                                           23s
service/helidon-demo-mp-1    ClusterIP   10.96.50.135    <none>        8080/TCP                                                           23s
service/helidon-demo-mp-2    ClusterIP   10.96.98.247    <none>        8080/TCP                                                           23s
service/helidon-demo-mp-3    ClusterIP   10.96.85.112    <none>        8080/TCP                                                           23s
service/helidon-demo-mp-np   NodePort    10.96.49.26     <none>        8080:30080/TCP                                                     23s
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

<br>

## ■ OpenTracing SPAN定義のためのアノテーション (oracle.demo.tracing.interceptor パッケージ)

MicroProfileのOpenTracingの実装の多くはSPANの定義を暗黙的に行っているケースが多く、コーディングしなくてもそれなりのトレース情報が出力されるので便利です。また、明示的にSPANを定義したい場合は@Tracedアノテーション(org.eclipse.microprofile.opentracing.Traced)を使って、メソッドにトレース出力をつけることができます。しかしながら、標準機能では必ずしも欲しい情報を出力してくれるとは限りません。そこで、ここではSPANの定義処理をCDI Interceptorとして実装して、Trace出力の内容をアノテーションである程度コントロールできるようにしてみました。

実装はoracle.demo.tracing.interceptor パッケージにあります。使用例はoracle.demo.jpa.CountryDAOを見て下さい。  
`/jpa/country?error=true` をGETすると以下のメソッドが呼ばれます。

```text
$ curl localhost:8080/jpa/country?error=true
```

```java
@Trace("JPA") 
@TraceTag(key = "JPQL", value = "select c from Countries c")
@TraceTag(key = "comment", value = "An error is expected by the wrong jpql statement.")
public List<Country> getCountriesWithError(){
    List<Country> countries = em.createQuery("select c from Countries c", Country.class).getResultList();
    return countries;
}
```

[Jaegerのトレーシング](doc/images/jaeger-tracing-custom.png)  でも付加情報が追加されていることが分かります。  

* アノテーション  
2つのアノテーションが利用可能です。

| annotation   | 説明 |
|--------------|------|
| @Trace       | 必須 ; SPANを定義するInterceptorを示す |
| @TraceTag    | オプション、key = キー, value = 値 ; SPAN内に定義するTagを追加する、複数使用可 |

@Trace の パラメータ

| parameter  | 説明 |
|------------|------|
| value      | defaul = "" ; SPAN名の接頭辞をつける、指定した場合 "<接頭辞>:<メソッド名>" となる|
| stackTrace | default = false ; Exception発生時にtrace logにstack traceを出力するか否か |

<br>

## ■ Metrics デモ (oracle.demo.metrics パッケージ)

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

<br>

## ■ Fault Tolerance デモ (oracle.demo.ft パッケージ)

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
$ java -cp ./target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester -e http://localhost:8080/ft/bulkhead 4
$ java -cp ./target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester -e http://localhost:8080/ft/circuit-breaker 6
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

<br>

## ■ Open API (oracle.demo.country パッケージ)

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

<br>

## ■ MicroProfile Rest Client (oracle.demo.restclient パッケージ)

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

<br>

## ■ Security (oracle.demo.security パッケージ)

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

<details open="true">
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

<br>

## ■ JPA (Java Persistence API) デモ (oracle.demo.jpa パッケージ)

Helidon は拡張機能として Java Persistence API (JPA) と Java Transaction API (JTA) をサポートしています。   
このデモでは
+ H2 Database JDBC Driver (DataSource)
+ Hikari Connection Pool Extension
+ EclipseLink JPA Extension
+ JTA Extension

の組み合わせで、データベースへの CRUD 処理を行っています。トランザクション処理 (commit/rollback) は コンテナ(Helidon) が管理します。

```java 
    @PersistenceContext(unitName = "CountryDS")
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

1. Maven のプロファイル `db-oracle` を指定して package します。これにより JDBC 関連ライブラリが Oracle JDBC に切り替わります。
```bash
$ mvn -P db-oracle package -DskipTests=true
```
2. システムプロパティ `demo.persistence-unit=Oracle` を指定して Java を実行します (環境変数でも可)。
```bash
$ java -jar -Ddemo.persistence-unit=Oracle target/helidon-demo-mp.jar
```

META-INF/microprofile-config.properties でデータソースを設定することもできます。
この場合、Java 実行時にシステムプロパティ `demo.data-source` を指定する必要はありません。

```
# switch datasource - H2(default), Oracle, MySQL
demo.persistence-unit=Oracle
```

### テスト用の Oracle Database インスタンスの作成するには？ 

デモ用に設定済みの Oracle Database インスタンスを Docker コンテナで実行するためのスクリプトを用意しています。

<details>
<summary>0. (必要に応じて) Oracle コンテナ・レジストリへのログイン</summary>

  
事前に `docker login container-registry.oracle.com` を済ませておいて下さい。  
[ポータル](https://container-registry.oracle.com/) にてソフトウェア利用許諾契約の確認が必要です。
</details>

<details>
<summary>1. demo/oracledb/start-oracledb.sh の実行</summary>
  
Oracle Database の公式コンテナ・イメージを取得して起動します。  
 `docker logs`を確認してデータベースが起動するまで待機して下さい。`Done ! The database is ready for use .` が表示されたらOKです。

```
$ docker logs -f oracledb
...
...
Done ! The database is ready for use .
Fri Jan 15 09:59:46 UTC 2021
User check : root.
Setup Oracle Database
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
<summary>停止、再起動、削除</summary>

```
# 停止
$ docker stop oracledb

# 再起動 docker start して PDB をオープンする
$ demo/oracledb/open-oracledb.sh

$ 削除
$ docker stop oracledb
$ docker rm oracledb
```

</details>

<br>

## ■ gRPC デモ (oracle.demo.grpc パッケージ)

gRPCで転送するデータのフォーマットはprotobuが一般的ですが、仕様上は任意のものが利用可能です。HelidonはgRPCを簡単にプロトタイプできるように、Javaシリアライゼーションを使った実装方法も提供しています。このデモにおいても、**protobufを用いた方法** (oracle.demo.grpc.protobuf パッケージ) と **Javaシリアライゼーションを用いた方法** (oracle.demo.grpc.javaobj パッケージ) の2種類を提供しています。

```bash
# どちらも REST -> gRPC Client -> gRPC Server と呼び出される

$ curl localhost:8080/grpc-protobuf/client # protobuf版
Hello world

$ curl localhost:8080/grpc-javaobj/client?name=OCHaCafe # Javaシリアライゼーション版
Hello OCHaCafe
```

### protobuf版 (oracle.demo.grpc.protobuf パッケージ) に関する補足

[gRPC Java Quickstart](https://grpc.io/docs/languages/java/quickstart/) と同じprotoファイルを用いて、互換性のある実装を行っていますので、Quickstart で作成したクライアントから Helidon の gRPC サーバーを呼び出すことができます。  

protobuf ペイロードを使ったサーバー実装は更に POJO + Annotaion を使った方法と、GrpcMpExtension を使って従来型のサービス実装クラスをデプロイする方法の、2種類を提供しています。おすすめは POJO + Annotaion です。

1. POJO + Annotaion を使った方法（デフォルト 有効）  
Helidonが提供するアノテーションを使って、シンプルなコーディングができます。
```
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
```
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

<br>

## ■ MicroProfile Reactive Messaging デモ (oracle.demo.reactive パッケージ)

JPA/JDBC経由でデータベースにアクセスするデモ(oracle.demo.jpaパッケージ)のバリエーションとして、MicroProfile Reactive Messaging を使ったデータベースの非同期更新(Event Sourcing)処理を実装しています。RESTでリクエストを受け付けた後、非同期更新イベントを発行します。  
[データベースへのアクセスパターン](#データベースへのアクセスパターン) を参照してください。

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

### JMS Connector

更に、Helidonが提供している JMS Connectorを使って WebLogic Server の JMSキューを経由したデータベースの非同期更新(Event Sourcing)処理を実装しています。このデモの実行には WebLogic Server のクライアント・ライブラリが必要なので、デフォルトで無効にしています。

```bash
# insert
curl -X POST -H "Content-Type: application/json" http://localhost:8080/reactive/jms/country \
   -d '[{"countryId":61,"countryName":"Australia"}]'
curl http://localhost:8080/jpa/country/61 # {"countryId":61,"countryName":"Australia"}

# update
curl -X PUT -H "Content-Type: application/x-www-form-urlencoded" http://localhost:8080/reactive/jms/country/61 \
  -d "name=Commonwealth of Australia"
curl http://localhost:8080/jpa/country/61 # {"countryId":61,"countryName":"Commonwealth of Australia"}

# delete
curl -X DELETE http://localhost:8080/reactive/jms/country/61
curl -v http://localhost:8080/jpa/country/61 # 404 Not Found
```

### JMS Connector デモを有効化するには？

<details>
<summary>1. WebLogic Server をインストールし、JMSリソースを構成する</summary>

適当なキューを定義して下さい。  
後述する「テスト用の WebLogic Server Docker インスタンスの作成」の項の手順に従えば、
このデモ用の設定がされた Dockerコンテナ・ベースのWebLogic Serverを準備することができます。
</details>

<details>
<summary>2. Mavenのローカル・リポジトリを作成して、WebLogic Serverのクライアント・ライブラリ(wlthint3client.jar)をデプロイする</summary>

クライアント・ライブラリはパブリックMavenリポジトリからは入手できませんので、ローカル・リポジトリをマニュアルで作成します。
`create-local-repo.sh` を編集してこのシェルを実行してください。`m2repo`フォルダにjarファイルがデプロイされます。  
wlthint3client.jar は後述するWebLogic Serverのコンテナ・イメージから入手するのが簡単かもしれません。

```bash
WL_HOME=${HOME}/opt/wls1411
WL_T3CLIENT_JAR=${WL_HOME}/wlserver/server/lib/wlthint3client.jar # これを正しいパスに

mkdir -p m2repo

mvn deploy:deploy-file \
 -Dfile=$WL_T3CLIENT_JAR \
 -Durl=file:./m2repo \
 -DgroupId=oracle.weblogic \
 -DartifactId=wlthint3client \
 -Dversion=14.1.1.0.0 \
 -Dpackaging=jar \
 -DgeneratePom=true
```
</details>

<details>
<summary>3. Javaソースのコメントアウトを外す</summary>
  
 - src/main/java/oracle/demo/reactive/ReactiveJmsResource.java
```java
    //@Outgoing("to-jms")
    public Publisher<Message<String>> preparePublisher() {
        return ReactiveStreams.fromPublisher(FlowAdapters.toPublisher(publisher)).buildRs();
    }

    //@Incoming("from-jms")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<?> consume(Message<String> message) {
```

 - src/test/java/oracle/demo/reactive/ReactiveJmsResourceTest.java
```java
//@HelidonTest
public class ReactiveJmsResourceTest{

    @Inject private WebTarget webTarget;

    //@Test
    public void testCRUDCountry(){
```
</details>

<details>
<summary>4. src/main/resources/application.yaml を編集して、WebLogic Serverの接続設定を行う</summary>

```yaml
# Reactive Messaging
mp.messaging:

  incoming.from-jms:
    connector: helidon-jms
    destination: ./SystemModule-0!Queue-0 # 確認
    type: queue

  outgoing.to-jms:
    connector: helidon-jms
    destination: ./SystemModule-0!Queue-0 # 確認
    type: queue

  connector:
    helidon-jms:
      user: weblogic # 確認
      password: OCHaCafe6834 # 確認
      jndi:
        jms-factory: weblogic.jms.ConnectionFactory
        env-properties:
          java.naming:
            factory.initial: weblogic.jndi.WLInitialContextFactory
            provider.url: t3://localhost:7001 # 確認
```
</details>

<details>
<summary>5. weblogic プロファイルを指定して Maven ビルドする</summary>

WebLogic クライアントライブラリを依存関係に含めます。Maven の仕様(プロファイルを指定するとデフォルト設定が効かなくなる)上、JPA のデモで使用するデータベースのプロファイルも同時に指定する必要があります。

```bash
$ mvn package -P db-h2,weblogic -DskipTests=true
# or 
$ mvn package -P db-oracale,weblogic -DskipTests=true
```
</details>
<br>

### テスト用の WebLogic Server Docker インスタンスの作成するには？ 

JMS Connector のデモに使うための設定済み WebLogic Server インスタンスを Docker コンテナで実行するためのスクリプトを用意しています。

<details>
<summary>0. (必要に応じて) Oracle コンテナ・レジストリへのログイン</summary>
  
事前に `docker login container-registry.oracle.com` を済ませておいて下さい。  
[ポータル](https://container-registry.oracle.com/) にてソフトウェア利用許諾契約の確認が必要です。
</details>

<details>
<summary>1. demo/weblogic/start-weblogic.sh の実行</summary>
  
WebLogic Server の公式コンテナ・イメージを取得して起動します。  
 `docker logs`を確認してサーバーが起動するまで待機して下さい。`<Server state changed to RUNNING.>` が表示されたらOKです。

```
$ docker logs -f wls1411
...
...
<Jan 6, 2021, 3:29:24,496 PM Greenwich Mean Time> <Notice> <WebLogicServer> <BEA-000331> <Started the WebLogic Server Administration Server "AdminServer" for domain "base_domain" running in development mode.> 
<Jan 6, 2021, 3:29:24,611 PM Greenwich Mean Time> <Notice> <WebLogicServer> <BEA-000360> <The server started in RUNNING mode.> 
<Jan 6, 2021, 3:29:24,651 PM Greenwich Mean Time> <Notice> <WebLogicServer> <BEA-000365> <Server state changed to RUNNING.> 
```
</details>

<details>
<summary>2. demo/weblogic/config-jms.sh の実行</summary>
  
WebLogic Server Deploy Tooling を使ってJMSリソースを追加し、サーバーを再起動します。

尚、デモの実行に必要な wlthint3client.jar は、以下のようにコンテナから取得することが可能です。

```bash
docker cp wls1411:/u01/oracle/wlserver/server/lib/wlthint3client.jar wlthint3client.jar
```
</details>
<br>

## ■ MicroProfile GraphQL デモ (oracle.demo.graphql パッケージ)

JPA経由でデータベースのCRUD操作をRestで公開するコードは既に提供していましたが、これをMicroProfile GraphQL仕様にしたものを追加しました。  
スキーマは `/graphql/schema.graphql` から取得できます。


```text
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

### データベースへのアクセスパターン

結果、JDBC/JPAを使ったデータベースへのアクセスは、以下のバリエーションを実装しています。
+ REST経由の同期参照＆更新処理
+ REST経由 MicroProfile Reactive Messaging を使った非同期更新(Event Sourcing)処理
+ MicroProfile GraphQL を使った Query & Mutation 処理 

![データベースへのアクセス・パターン](doc/images/microprofile-demo-crud.png)

<br>

## ■ （おまけ）Cowsay (oracle.demo.cowweb パッケージ)

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

<br>

---
_Copyright © 2019-2021, Oracle and/or its affiliates. All rights reserved._




