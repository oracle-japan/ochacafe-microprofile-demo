![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/oracle-japan/ochacafe-microprofile-demo)

# [Helidon](https://helidon.io/) を使って [Eclipse MicroProfile](https://microprofile.io/) の仕様や拡張機能を確認するデモ

For [OCHaCafe 2 - #4 Cloud Native時代のモダンJavaの世界](https://ochacafe.connpass.com/event/155389/)

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
│           │   ├── TracingResource.java
│           │   └── interceptor [SPAN定義 Interceptor & アノテーション]
│           │       ├── TraceInterceptor.java
│           │       ├── Trace.java
│           │       ├── TraceTagHolder.java
│           │       └── TraceTag.java
│           ├── reactive [Reactive Messaging]
│           │   └── ReactiveResource.java
│           ├── graphql [GraphQL]
│           │   ├── Country.java
│           │   └── CountryGraphQLApi.java
│           ├── jpa [拡張機能 JPA/JTA]
│           │   ├── Country.java
│           │   ├── CountryResource.java
│           │   ├── Greeting.java
│           │   └── JPAExampleResource.java
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
    ├── application.yaml [Helidonで使う設定ファイル]
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

## Docker イメージの作成

Dockerfileを使わずに、[Jib](https://github.com/GoogleContainerTools/jib) を使ってMavenから直接イメージをビルドします.  
ルートディレクトリにあるDockerfileを使ってもイメージの作成は可能です.

### 通常（ローカル）のタグを付与する場合

```
mvn post-integration-test # 便宜上post-integration-testにアサインしているだけ
```

### リモート用のタグを付与する場合

pom.xmlを以下のように設定し、
```xml
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
helidon-demo-mp                                   2.0-SNAPSHOT        1b4d2e82f64a        49 years ago        125MB
helidon-demo-mp                                   latest              1b4d2e82f64a        49 years ago        125MB
(remote docker repository path/)helidon-demo-mp   2.0-SNAPSHOT        116de0207be6        49 years ago        125MB
(remote docker repository path/)helidon-demo-mp   latest              116de0207be6        49 years ago        125MB

$ docker push (remote docker repository path/)helidon-demo-mp
```

## gRPC 関連の補足

protobuf ペイロードを使ったサーバー実装は、POJO + Annotaion を使った方法と、GrpcMpExtensionを使って従来型のサービス実装クラスをデプロイする方法の、2種類を提供しています。おすすめは POJO + Annotaion です。

### POJO + Annotaion を使った方法（デフォルト 有効）

```
oracle.demo.grpc.protobuf.GreeterSimpleService
```

### GrpcMpExtensionを使って従来型のサービス実装クラスをデプロイする方法

```
oracle.demo.grpc.protobuf.GreeterService
oracle.demo.grpc.protobuf.GrpcExtension
META-INF/services/io.helidon.microprofile.grpc.server.spi.GrpcMpExtension
```

### 実装の切り替え方

1. META-INF/services/io.helidon.microprofile.grpc.server.spi.GrpcMpExtension を編集する
```
# コメントアウトを外す
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

### gRPC - protoファイルのコンパイルについて

pom.xmlの通常ビルドフェーズとは独立してprotoファイルのコンパイルを行うプロファイルを定義しています。
以下のコマンドを使って、まず最初にソースを生成して、srcディレクトリにコピーをします。詳細は、pom.xml の内容を確認して下さい。

```
mvn -P protoc generate-sources
```

## OpenTracing SPAN定義のためのアノテーション

MicroProfileのOpenTracingの実装の多くはSPANの定義を暗黙的に行っているケースが多く、コーディングしなくてもそれなりのトレース情報が出力されるので便利です。また、明示的にSPANを定義したい場合は@Tracedアノテーション(org.eclipse.microprofile.opentracing.Traced)を使って、メソッドにトレース出力をつけることができます。しかしながら、標準機能では必ずしも欲しい情報を出力してくれるとは限りません。そこで、ここではSPANの定義処理をCDI Interceptorとして実装して、Trace出力の内容をアノテーションである程度コントロールできるようにしてみました。

実装はoracle.demo.tracing.interceptor パッケージにあります。使用例はoracle.demo.jpa.CountryDAOを見て下さい。  
`/jpa/country?error=true` をGETすると以下のメソッドが呼ばれます。

```java
@Trace("JPA") 
@TraceTag(key = "JPQL", value = "select c from Countries c")
@TraceTag(key = "comment", value = "An error is expected by the wrong jpql statement.")
public List<Country> getCountriesWithError(){
    List<Country> countries = em.createQuery("select c from Countries c", Country.class).getResultList();
    return countries;
}
```

2つのアノテーションが利用可能です(oracle.demo.tracing.interceptorパッケージ)。

| annotation   | 説明 |
|--------------|------|
| @Trace       | 必須 ; SPANを定義するInterceptorを示す |
| @TraceTag    | オプション、key = キー, value = 値 ; SPAN内に定義するTagを追加する、複数使用可 |

@Trace の パラメータ
| parameter  | 説明 |
|------------|------|
| value      | defaul = "" ; SPAN名の接頭辞をつける、指定した場合 "<接頭辞>:<メソッド名>" となる|
| stackTrace | default = false ; Exception発生時にtrace logにstack traceを出力するか否か |

## GraphQL

JPA経由でデータベースのCRUD操作をRestで公開するコードは既に提供していましたが、これをGraphQL仕様にしたものを追加しました。  
スキーマは以下のURLで取得できます。  
http://localhost:8080/graphql/schema.graphql


```
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

curlでテストする場合は、以下を参考にして下さい。  
同様の操作は、GrapghQLのテストケース(CountryGraphQLApiTest.java)でも行っていますので、そちらも参考にしてください。

```
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "query { countries { countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "query { country(countryId: 1) { countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { insertCountry (country:{countryId:86,countryName:\"China\"}) { countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { insertCountries (countries:[{countryId:82,countryName:\"Korea\"},{countryId:91,countryName:\"India\"}]) { countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { updateCountry (countryId:1,countryName:\"United States\") { countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql \
  -d '{ "query" : "mutation { deleteCountry (countryId:86) }" }'
```

---
_Copyright © 2019-2020, Oracle and/or its affiliates. All rights reserved._


