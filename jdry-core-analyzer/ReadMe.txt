
vm options : --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED
             --add-opens java.base/java.lang=ALL-UNNAMED
             --add-opens=java.base/java.util=ALL-UNNAMED
             --add-opens=java.base/java.net=ALL-UNNAMED
env var : <path to analyzer.yaml>
    example : C:\\work\\myprojects\\java\\iotback\\analyzer.yaml

java --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED -jar target/jdry-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar  C:/work/myprojects/java/iotback/analyzer.yaml target/generated-sources target/generated-sources
