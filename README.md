# logback-encoder

This project contains a very simple [logback](https://logback.qos.ch/) JSON encoder and custom configurator. The idea behind [encoder](https://logback.qos.ch/manual/encoders.html) is to output logs in following format as per document in [link](https://borobudur.atlassian.net/wiki/spaces/XX/pages/2708472960/WIP+RFC+015+Standardized+Application+Logging+Enhancing+Scalability+Usefulness+and+Efficiency)

```
{
  "timestamp": "2021-09-09T01:46:40.235Z",
  "level": "INFO",
  "x_request_id": "abcdef12-3456-7890-ghij-klmnopqrst",
  "source":"com.example.MyApplication",
  "thread": "4567",
  "sdc":{
    "order_id": "ord9676",
    "booking_id": "cust456",
    "transaction_id": "txn9876"
  },
  "message": "Processing user request for user ID: 12345"
}
```
And the custom configurator has been added to avoid logback [configuration](https://logback.qos.ch/manual/configuration.html) using logback.xml and reduce application startup time by approx 100 ms. This project also contains a custom [layout](https://logback.qos.ch/manual/layouts.html) but that will not be used. It is only meant for experimenting with few things.

# How to use?

For Maven projects add this dependency in `pom.xml` file of your project and add version as per your need. 

```
<dependencies>
  <dependency>
    <groupId>com.tix.logback.logging</groupId>
    <artifactId>custom-config</artifactId>
    <version></version>
  </dependency>
</dependencies>
```

To use `CustomLogbackConfigurator` for removing logback configuration using `logback.xml` we will be using [service provider](https://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html) loading facility provided by Java. All you have to do is to create a file named `ch.qos.logback.classic.spi.Configurator` on path `src/main/resources/META-INF/services` and add `com.tix.logback.logging.CustomLogbackConfigurator` as the content of the file. This is actually the recommended way to configure logback for your application but if you want to do it in your own way then simply add `logback.xml` file in path `src/main/resources` of your Maven project. To use `CustomJsonEncoder` defined in this project with `ConsoleAppender` you can add the following configuration in `logback.xml`:

```
<configuration>

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="com.tix.logback.logging.CustomJsonEncoder" />
  </appender>

  <root level="warn">
    <appender-ref ref="STDOUT" />
  </root>
</configuration>
```

As far as passing `x_request_id` is concerned your application will have to pass the value using [Mapped Diagnostic Context(MDC)](https://logback.qos.ch/manual/mdc.html). Other contextual information to be logged in field `sdc` is also supposed to be provided via MDC.

```
MDC.put("x_request_id", "49a3d54a-7094-4e37-9ac8-2bad5623dcde")
```


