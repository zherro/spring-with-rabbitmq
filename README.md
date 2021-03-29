# Spring with RabbitMQ

## Configurations

- Java 11
- Spring Boot 2.4.4
- Gradle
- RabbitMQ 3

## What is RabbitMQ?

> [See abstract](doc/rabbitmq-abstract.md)

We will create a Spring Boot multi-module project in order to perform messaging with RabbitMQ.

The application consists of two services:

- Producer Service that produces messages to RabbitMQ.
- Consumer Service that consumes messages from RabbitMQ.

## Getting Started

### RabbitMQ in docker 

> [Reference](https://hub.docker.com/_/rabbitmq)

``` 
$ docker run -d --hostname my-rabbit --name some-rabbit -p 8080:5672 -p 8081:15672 rabbitmq:3-management
```
There is a second set of tags provided with the [management plugin](https://www.rabbitmq.com/management.html) installed and enabled by default, 
which is available on the standard management port of 15672, with the default username and password of `guest` / `guest`:

You can then go to `http://localhost:8081` or `http://host-ip:8081` in a browser to access de managment panel.

Use `http://localhost:8080` or `http://host-ip:8080` to send and consumer rabbit queue.

### Reference Documentation

Part of this project is based in the following docs:

- [Spring Boot Messaging with RabbitMQ](https://springframework.guru/spring-boot-messaging-with-rabbitmq/)

*cloudamqp.com/blog/*
- [RabbitMQ for beginners - What is RabbitMQ?](https://www.cloudamqp.com/blog/part1-rabbitmq-for-beginners-what-is-rabbitmq.html)
- [RabbitMQ Exchanges, routing keys and bindings](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html)