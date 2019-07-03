# About Us

[www.lxisoft.com] | [www.divisosofttech.com]
We want to increase the life chances of millions of people. Specifically, “The Fortune at the Bottom of the Pyramid,” using the power of technology. We wish to forge associations with people like you. Powered by you and by leveraging synergies, creativity, competitiveness, grace, and entrepreneurship, together, let us Script the Future.

## Java Development Services

From an incubating Idea to a rapid prototype, followed by continuous integration and continuous delivery of upgrades, launch to a production release, and additional production upgrades – we are here to help. We are a team of Internationally Certified Java Developers who can transform your ideas into a Web Application or Cross-Platform Mobile Apps – Android, iPhone, iPad, Windows, and various others or SOA / Microservices based Architecture or Java Web Portals. You will set the time and pace, and our team will collaborate and lend you our expertise.

[Contact Us](http://www.lxisoft.com/contact-us-2/)


## Continous Delivery

Agile application delivery is leveraged to transform a roadmap into a production release. Our experts in consultation with you would make specific recommendations for our Software Delivery Strategy. Continuous Integration leverages Git, SVN, Maven, Gradle, Cloud, Nexus, Travis, Docker.

[Contact Us](http://www.lxisoft.com/contact-us-2/)


# Internationally Certified Developers

Our Internationally Certified Java Consultants are ready to collaborate with your team. Their experience working with technologies like J2EE, Struts, JSF, Spring, Hibernate, JPA, Microservices, Cloud, Portals, Docker, Kubernetes would be an asset.

[Contact Us](http://www.lxisoft.com/contact-us-2/)

# Product Microservice

This is a "microservice" application intended to be part of a microservice architecture.

This application is configured for Service Discovery and Configuration with the JHipster-Registry. On launch, it will refuse to start if it is not able to connect to the JHipster-Registry at [http://localhost:8761](http://localhost:8761). 

## Development

To start your application in the dev profile, simply run:

    ./mvnw

## Building for production

To optimize the productmicroservice application for production, run:

    ./mvnw -Pprod clean package

To ensure everything worked, run:

    java -jar target/*.war

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```
./mvnw -Pprod clean test sonar:sonar
```

## Using Docker to simplify development (optional)


To start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod verify jib:dockerBuild

Then run:

    docker-compose -f src/main/docker/app.yml up -d


## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator.
