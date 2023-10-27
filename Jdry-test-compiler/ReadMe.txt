configuration :
vm options : --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED
env var : <path to resources>/<?>.json target
    example : C:/work/myprojects/java/jdry-pro3/Jdry-test-compiler/src/main/resources/iotbackend_analyzer.json target/generated-sources/

java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED -jar target/Jdry-test-compiler-1.0-SNAPSHOT-jar-with-dependencies.jar  C:/work/myprojects/java/jdry-pro3/Jdry-test-compiler/src/main/resources/iotbackend_analyzer.json target/generated-sources/<analyzer>.json target/generated-sources/
