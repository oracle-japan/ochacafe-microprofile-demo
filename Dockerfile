# 1st stage, build the app
FROM maven:3.6.3-jdk-11 as build

WORKDIR /helidon
RUN mkdir -p /helidon/target/classes # for weaving

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD pom.xml .
#RUN mvn package -DskipTests # this causes JPA error

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn -P protoc generate-sources
RUN mvn package -DskipTests
RUN echo "done!"

# 2nd stage, build the runtime image
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /helidon

# Copy the binary built in the 1st stage
COPY --from=build /helidon/target/helidon-demo-mp.jar ./
COPY --from=build /helidon/target/libs ./libs

CMD ["java", "-jar", "helidon-demo-mp.jar"]

EXPOSE 8080
EXPOSE 50051

