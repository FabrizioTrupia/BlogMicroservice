FROM amazoncorretto:17-alpine-jdk
EXPOSE 8761
ADD target/discovery-0.0.1-SNAPSHOT.jar discovery-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/discovery-0.0.1-SNAPSHOT.jar"]