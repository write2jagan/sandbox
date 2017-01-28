Prerequisite:
To run this project is necessary have installed Maven version => 3.0 and JDK 1.8.

Run project types 

mvn clean package
java -jar target/photocopier-port-1.0-SNAPSHOT-fat.jar

Run project with properties

java -jar target/photocopier-port-1.0-SNAPSHOT-fat.jar -conf src/main/conf/photocopier.json



Launch from eclipse:

From run/debug configuration create e new "java application" as run configuration and has
main class use io.vertx.core.Launcher.

In the main parameter type 
run --redeploy="src/**/*.java" --launcher-class=it.balyfix.experiments.main.Main

This configuration permit redeploy that is Good goal. to reboot porocess is necessary kill the
process from console with kill -9 <pid>