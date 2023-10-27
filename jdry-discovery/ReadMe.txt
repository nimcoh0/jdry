
vm options : --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED
env var : <path to discovery.yaml>/<app>
    example : C:\\work\\myprojects\\java\\iotback\\discovery.yaml

java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED -jar target/jdry-discovery-1.0-SNAPSHOT-jar-with-dependencies.jar  C:/work/myprojects/java/iotback/discovery.yaml target/generated-sources
