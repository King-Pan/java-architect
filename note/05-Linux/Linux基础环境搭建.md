



# mvn jdk配置



```sh
export JAVA_HOME=/var/java/jdk1.8.0_261/
export JRE_HOME=$JAVA_HOME/jre
export MAVEN_HOME=/var/maven/apache-maven-3.6.3
export PATH=$PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin
export CLASSPATH=$CLASSPATH:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```







​        sh 'mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy --settings /var/jenkins_home/apache-maven-3.6.3/conf/settings.xml -DskipTests'