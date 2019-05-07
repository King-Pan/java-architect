# Maven把源码打包进jar包

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>2.3</version>
    <executions>
        <execution>
            <id>copy-resources</id>
            <phase>process-resources</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                <resources>
                    <resource>
                        <directory>src/main/java</directory>
                        <includes>
                            <include>**/*</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

