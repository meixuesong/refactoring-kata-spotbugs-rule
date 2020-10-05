# My SpotBugs plugin

This spotbugs plugin can check feature envy rule.

Feature envy is a method that uses more methods or variables from another (unrelated) class than from its own class violates the principle of putting data and behavior in the same place.

To use this spotbugs plugin with Maven and sonarqube, sample pom.xml as following:

```xml
    <properties>
        <sonar.java.spotbugs.reportPaths>./target/spotbugsXml.xml</sonar.java.spotbugs.reportPaths>
    </properties>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>com.github.spotbugs</groupId>
                    <artifactId>spotbugs-maven-plugin</artifactId>
                    <version>4.0.4</version>
                    <configuration>
                        <plugins>
                            <plugin>
                                <groupId>refactoring</groupId>
                                <artifactId>kata-spotbugs-rule</artifactId>
                                <version>1.0</version>
                            </plugin>
                        </plugins>
                        <includeFilterFile>${project.basedir}/src/main/resources/spotbugs.xml</includeFilterFile>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
```

```shell script
mvn clean verify spotbugs:spotbugs
```
