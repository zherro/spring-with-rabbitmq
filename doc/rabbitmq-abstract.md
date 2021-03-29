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
  
<img src="https://github.com/zherro/spring-with-rabbitmq/blob/main/doc/img/Messaging-System-1024x439.png" width="600" height="300">

The basic architecture of a message queue is simple - there are client applications 
called producers that create messages and deliver them to the broker (the message queue). 
Other applications, called consumers, connect to the queue and subscribe to the messages 
to be processed. Software may act as a producer, or consumer, or both a consumer and 
a producer of messages. Messages placed onto the queue are stored until the consumer 
retrieves them.

#### RABBITMQ AND SERVER CONCEPTS
Some important concepts need to be described before we dig deeper into RabbitMQ. 
The default virtual host, the default user, and the default permissions are used 
in the examples, so let’s go over the elements and concepts:

- **Producer:** Application that sends the messages.
- **Consumer:** Application that receives the messages.
- **Queue:** Buffer that stores messages.
- **Message:** Information that is sent from the producer to a consumer through RabbitMQ.
- **Connection:** A TCP connection between your application and the RabbitMQ broker.
- **Channel:** A virtual connection inside a connection. When publishing or consuming messages from a queue - it's all done over a channel.
- **Exchange:** Receives messages from producers and pushes them to queues depending on rules defined by the exchange type. To receive messages, a queue needs to be bound to at least one exchange.
- **Binding:** A binding is a link between a queue and an exchange.
- **Routing key:** A key that the exchange looks at to decide how to route the message to queues. Think of the routing key like an address for the message.
- **AMQP:** Advanced Message Queuing Protocol is the protocol used by RabbitMQ for messaging.
- **Users:** It is possible to connect to RabbitMQ with a given username and password. Every user can be assigned permissions such as rights to read, write and configure privileges within the instance. Users can also be assigned permissions for specific virtual hosts.
- **Vhost, virtual host:** Provides a way to segregate applications using the same RabbitMQ instance. Different users can have different permissions to different vhost and queues and exchanges can be created, so they only exist in one vhost.

## RabbitMQ Exchanges, routing keys and bindings

> What is an exchange? What are routing keys and bindings? 
> How are exchanges and queues associated with each other? 
> When should I use them and how?

Messages are not published directly to a queue. Instead, the producer sends messages 
to an exchange. Exchanges are message routing agents, defined by the virtual host 
within RabbitMQ. An exchange is responsible for routing the messages to different queues 
with the help of header attributes, bindings, and routing keys.

A **binding** is a "link" that you set up to bind a queue to an exchange.

The **routing key** is a message attribute the exchange looks at when deciding how to 
route the message to queues (depending on exchange type).

> Exchanges, connections, and queues can be configured with parameters such as durable, 
temporary, and auto delete upon creation. Durable exchanges survive server restarts and 
last until they are explicitly deleted. Temporary exchanges exist until RabbitMQ is shut 
down. Auto-deleted exchanges are removed once the last bound object is unbound from the 
exchange.

In RabbitMQ, there are four different types of exchanges that route the message differently 
using different parameters and bindings setups. Clients can create their own exchanges or 
use the predefined default exchanges which are created when the server starts for the first 
time.

### Standard RabbitMQ message flow

<div style="width:100%" >
    <div class="col6">
        <ol>
            <li>
                The producer publishes a message to the exchange.
            </li>
            <li>
                The exchange receives the message and is now responsible for the routing of the message.
            </li>
            <li>
                Binding must be set up between the queue and the exchange. In this case, we have bindings to two different queues from the exchange. The exchange routes the message into the queues.
            </li>
            <li>
               The messages stay in the queue until they are handled by a consumer.
            </li>
            <li>
               The consumer handles the message.
            </li>
        </ol>
    </div>
    <div  class="col6">
        <img src="img/exchanges-bidings-routing-keys.png" style="width: 100%; max-width: 460px;">
    </div>
</div>

#### TYPES OF EXCHANGES

- **Direct:** The message is routed to the queues whose binding key exactly matches the routing key of the message. For example, if the queue is bound to the exchange with the binding key pdfprocess, a message published to the exchange with a routing key pdfprocess is routed to that queue.
- **Fanout:** A fanout exchange routes messages to all of the queues bound to it.
- **Topic:** The topic exchange does a wildcard match between the routing key and the routing pattern specified in the binding.
- **Headers:** Headers exchanges use the message header attributes for routing.

<img src="img/exchanges-topic-fanout-direct.png" style="width: 100%; max-width: 700px;">

### Exchange types

#### Direct Exchange

A direct exchange delivers messages to queues based on a message routing key. 
The routing key is a message attribute added to the message header by the producer. 
Think of the routing key as an "address" that the exchange is using to decide how to 
route the message. **A message goes to the queue(s) with the binding key that exactly 
matches the routing key of the message.**

The direct exchange type is useful to distinguish messages published to the same 
exchange using a simple string identifier.

The default exchange AMQP brokers must provide for the direct exchange is "amq.direct".


**SCENARIO 1**

- Exchange: pdf_events
- Queue A: create_pdf_queue
- Binding key between exchange (pdf_events) and Queue A (create_pdf_queue): pdf_create

**SCENARIO 2**

- Exchange: pdf_events
- Queue B: pdf_log_queue
- Binding key between exchange (pdf_events) and Queue B (pdf_log_queue): pdf_log

<img src="img/direct-exchange.png" style="width: 100%; max-width: 460px;">

Example: A message with routing key pdf_log is sent to the exchange pdf_events. 
The messages is routed to pdf_log_queue because the routing key (pdf_log) matches 
the binding key (pdf_log).

> If the message routing key does not match any binding key, the message is discarded.

#### Default exchange

The default exchange is a pre-declared direct exchange with no name, usually referred 
by an empty string. When you use default exchange, your message is delivered to the 
queue with a name equal to the routing key of the message. Every queue is automatically 
bound to the default exchange with a routing key which is the same as the queue name.

#### Topic Exchange

Topic exchanges route messages to queues based on wildcard matches between the routing 
key and the routing pattern, which is specified by the queue binding. **Messages are 
routed to one or many queues based on a matching between a message routing key and 
this pattern.**

The routing key must be a list of words, delimited by a period (.). Examples are 
agreements.us and agreements.eu.stockholm which in this case identifies agreements 
that are set up for a company with offices in lots of different locations. The routing 
patterns may contain an asterisk (“\*”) to match a word in a specific position of the 
routing key (e.g., a routing pattern of "agreements.\*.\*.b.\*" only match routing keys 
where the first word is "agreements" and the fourth word is "b"). A pound symbol (“#”) 
indicates a match of zero or more words (e.g., a routing pattern of 
"agreements.eu.berlin.#" matches any routing keys beginning with "agreements.eu.berlin").

The consumers indicate which topics they are interested in (like subscribing to a feed 
for an individual tag). The consumer creates a queue and sets up a binding with a given 
routing pattern to the exchange. All messages with a routing key that match the routing 
pattern are routed to the queue and stay there until the consumer consumes the message.

The default exchange AMQP brokers must provide for the topic exchange is "amq.topic".

**SCENARIO 1**

The image below shows an example where consumer A is interested in 
all the agreements in Berlin.

- Exchange: agreements
- Queue A: berlin_agreements
- Routing pattern between exchange (agreements) and Queue A (berlin_agreements): agreements.eu.berlin.#
- Example of message routing key that matches: agreements.eu.berlin and agreements.eu.berlin.headstore

**SCENARIO 2**

Consumer B is interested in all the agreements.

- Exchange: agreements
- Queue B: all_agreements
- Routing pattern between exchange (agreements) and Queue B (all_agreements): agreements.#
- Example of message routing key that matches: agreements.eu.berlin and agreements.us
rabbitmq topic exchange

**SCENARIO 3**

Consumer C is interested in all agreements for European head stores.

- Exchange: agreements
- Queue C: headstore_agreements
- Routing pattern between exchange (agreements) and Queue C (headstore_agreements): agreements.eu.*.headstore
- Example of message routing keys that will match: agreements.eu.berlin.headstore and agreements.eu.stockholm.headstore


<img src="img/topic-exchange.png" style="width: 100%; max-width: 460px;">

**EXAMPLE**

A message with routing key agreements.eu.berlin is sent to the exchange agreements. 
The messages are routed to the queue berlin_agreements because the routing pattern of 
"agreements.eu.berlin.#" matches the routing keys beginning with "agreements.eu.berlin". 
The message is also routed to the queue all_agreements because the routing key 
(agreements.eu.berlin) matches the routing pattern (agreements.#).

#### Fanout Exchange

A fanout exchange copies and routes a received message to all queues that are bound to 
it regardless of routing keys or pattern matching as with direct and topic exchanges. 
The keys provided will simply be ignored.

Fanout exchanges can be useful when the same message needs to be sent to one or more 
queues with consumers who may process the same message in different ways.

The image below (Fanout Exchange) shows an example where a message received by the 
exchange is copied and routed to all three queues bound to the exchange. It could 
be sport or weather updates that should be sent out to each connected mobile device 
when something happens, for instance.

The default exchange AMQP brokers must provide for the topic exchange is "amq.fanout".

**SCENARIO 1**

- Exchange: sport_news
- Queue A: Mobile client queue A
- Binding: Binding between the exchange (sport_news) and Queue A (Mobile client queue A)

<img src="img/fanout-exchange.png" style="width: 100%; max-width: 460px;">

**EXAMPLE**

A message is sent to the exchange sport_news. The message is routed to all queues 
(Queue A, Queue B, Queue C) because all queues are bound to the exchange. 
Provided routing keys are ignored.

#### Headers Exchange

A headers exchange routes messages based on arguments containing headers and optional 
values. Headers exchanges are very similar to topic exchanges, but route messages based 
on header values instead of routing keys. A message matches if the value of the header 
equals the value specified upon binding.

A special argument named "x-match", added in the binding between exchange and queue, 
specifies if all headers must match or just one. Either any common header between the 
message and the binding count as a match, or all the headers referenced in the binding 
need to be present in the message for it to match. The "x-match" property can have two 
different values: "any" or "all", where "all" is the default value. A value of "all" 
means all header pairs (key, value) must match, while value of "any" means at least one 
of the header pairs must match. Headers can be constructed using a wider range of data 
types, integer or hash for example, instead of a string. The headers exchange type 
(used with the binding argument "any") is useful for directing messages which contain a 
subset of known (unordered) criteria.

The default exchange AMQP brokers must provide for the topic exchange is "amq.headers".


**SCENARIO 1**

Message 1 is published to the exchange with header arguments 
(key = value): "format = pdf", "type = report".

Message 1 is delivered to Queue A because all key/value pairs match, and Queue B since 
"format = pdf" is a match (binding rule set to "x-match =any").

**SCENARIO 2**

Message 2 is published to the exchange with header arguments of (key = value): 
"format = pdf".

Message 2 is only delivered to Queue B. Because the binding of Queue A requires both 
"format = pdf" and "type = report" while Queue B is configured to match any key-value pair (x-match = any) as long as either "format = pdf" or "type = log" is present.

**SCENARIO 3**

Message 3 is published to the exchange with header arguments of (key = value): 
"format = zip", "type = log".

Message 3 is delivered to Queue B since its binding indicates that it accepts 
messages with the key-value pair "type = log", it doesn't mind that "format = zip" 
since "x-match = any".

Queue C doesn't receive any of the messages since its binding is configured to match 
all of the headers ("x-match = all") with "format = zip", "type = pdf". No message in 
this example lives up to these criterias.

It's worth noting that in a header exchange, the actual order of the key-value pairs 
in the message is irrelevant.

<img src="img/headers-exchange.png" style="width: 100%; max-width: 460px;">

**EXAMPLE**
- Exchange: Binding to Queue A with arguments (key = value): format = pdf, type = report, x-match = all
- Exchange: Binding to Queue B with arguments (key = value): format = pdf, type = log, x-match = any
- Exchange: Binding to Queue C with arguments (key = value): format = zip, type = report, x-match = all

#### Dead Letter Exchange

If no matching queue can be found for the message, the message is silently dropped. 
RabbitMQ provides an AMQP extension known as the "Dead Letter Exchange", which provides 
the functionality to capture messages that are not deliverable.