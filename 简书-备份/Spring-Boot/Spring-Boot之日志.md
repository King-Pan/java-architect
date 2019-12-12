```
logging:
  pattern:
    console: "%d - %msg%n"
  #path: /Users/king-pan/
  file: /Users/king-pan/training.log
  # 日志级别可以精确到类
  level:
    club.javalearn.training.test.LoggerTest: error
  # 默认级别info
  #level: debug
```

### logback日志详细配置,需要把application.yml或者application.properties文件中日志相关配置注释掉

```
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <appender name="consoleLog" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>
                %d - %msg%n
            </pattern>
        </layout>
    </appender>
    <appender name="fileInfoLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--根据范围过滤日志-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!--直接-->
            <level>INFO</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <encoder>
            <pattern>
                %msg%n
            </pattern>
        </encoder>
        <!--滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--路径-->
            <fileNamePattern>/Users/king-pan/training.info.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <appender name="fileErrorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--根据范围过滤日志-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>
                %msg%n
            </pattern>
        </encoder>
        <!--滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--路径-->
            <fileNamePattern>/Users/king-pan/training.error.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <root level="info">
        <appender-ref ref="consoleLog" />
        <appender-ref ref="fileInfoLog" />
        <appender-ref ref="fileErrorLog" />
    </root>
</configuration>
```
