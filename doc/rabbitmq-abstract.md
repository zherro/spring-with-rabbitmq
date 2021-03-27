# RabbitMQ

## What is RabbitMQ

RabbitMQ is a common messaging broker which allows applications to connect and communicate.
It’s common for services in microservices-based systems to communicate asynchronously 
through messaging.

In order to create such a message-based system, you need a message broker, aka messaging
server.

A message broker can support several messaging patterns. Typically, a message sender
sends a message to the message broker. The broker stores the message until a message 
receiver application connects and consumes the message. The receiving application then 
further processes the message, as per business requirements.

To exchange messages between different applications, we can use RabbitMQ as a message broker.

In this post, I will discuss how to perform messaging with RabbitMQ.

I will take you through the steps to produce and consume messages with RabbitMQ in Spring Boot 
microservices.

### Overview
RabbitMQ is an open-source message broker that allows communication between different services
of enterprise applications. It is typically used in microservices to send and receive messages
for inter-service communication.

Messaging In RabbitMQ involves:

- A producer is a user application that sends messages to a RabbitMQ message broker. Messages 
are not directly sent to a queue. Here, the producer sends messages to an exchange. Exchanges
are message routing agents that are responsible for routing the message to different queues.
- A queue is a buffer that resides inside RabbitMQ to store messages that a producer sends and a
receiver receives.
- A consumer is a user application that receives messages from the RabbitMQ message broker and
then processes them further.
This image shows how messages are communicated in RabbitMQ.
  
![](doc/Messaging-System-1024x439.png =600x300)