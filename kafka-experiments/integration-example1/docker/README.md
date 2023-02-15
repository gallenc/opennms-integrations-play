
```
docker build -t kafka-client -f .\Dockerfile-client .

docker run -t -i kafka-client

```

```
docker build -t kafka-web -f .\Dockerfile-web .

docker run -t -i kafka-web

```

```
mvn clean install
 docker run -t -i opennms:kafka-client
 
  docker run -t -i opennms:kafka-web
```