# Kafka Custom Serializer Examples with JSON, Smile and Kryo

## Building

You can create a runnable jar with Maven: just run mvn clean install and you should find a runnable jar
in the target directory.

## Running



## Command line options:

The command line has 3 mandatory options (produce/consume, the serializer and the Kafka topic) and 1 option option
(Kafka host:port, default is Kafka default localhost:9092).

| Serializer | Description                                                                           |
|------------|---------------------------------------------------------------------------------------|
| string     | StringReaderSerializer: uses basic spring concatenation / splitting                   |
| json       | JacksonReaderSerializer: uses the jackson library to serialize to JSON                |
| smile      | JacksonReaderSerializer: uses the jackson library to serialize to Smile (binary JSON) |
| kryo       | KryoReaderSerializer: uses the kryo library                                           |

Examples:

java -jar target/kafka-serializer-1.0-SNAPSHOT-jar-with-dependencies.jar produce string test-string
    Run a producer with StringReaderSerializer writing to the test-string topic
    
java -jar target/kafka-serializer-1.0-SNAPSHOT-jar-with-dependencies.jar consume json test-json localhost:9100
    Run a consumer with JacksonReaderSerializer reading from the test-json topic connecting to Kafka on port 9100


