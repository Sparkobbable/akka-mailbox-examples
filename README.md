# Akka Mailbox Examples

This project demonstrates different types of mailboxes in Akka and how they can be used. It serves as a reference for a presentation on mailbox usage. The implementation is kept simple by using built-in mailbox types via configuration where possible.

## Mailbox Types Used

### 1. Built-in Mailbox Types (via Configuration)

#### UnboundedMailbox
The default mailbox type in Akka:
- Unlimited capacity
- FIFO (First In, First Out) processing order

#### BoundedMailbox
A mailbox with a limited capacity:
- Configurable capacity (set to 3 in our example)
- Configurable push timeout
- Useful for backpressure and resource management

#### UnboundedPriorityMailbox
A mailbox that processes messages based on priority:
- Lower numbers = higher priority
- Built-in priority handling
- Useful for processing important messages first

#### UnboundedStablePriorityMailbox
A priority mailbox that maintains FIFO order for messages with the same priority:
- Unlike regular priority mailboxes, this ensures that messages with equal priority are processed in the order they were received
- Useful when order matters within priority levels

#### UnboundedControlAwareMailbox
A mailbox that prioritizes control messages over regular messages:
- Messages implementing akka.dispatch.ControlMessage interface are processed first
- Useful for system management and control operations

### 2. Custom Mailbox Implementations

#### CustomPriorityMailbox
A sophisticated priority mailbox that prioritizes messages based on their type and content:
- Handles PriorityMessage with explicit priority values
- Gives high priority to ControlMessage
- Prioritizes SimpleMessage based on content (e.g., "urgent" gets higher priority)
- Maintains backward compatibility with string messages

#### DeadLetterMailbox
A mailbox that forwards messages to the system's dead letters:
- Logs messages before forwarding them to dead letters
- Also processes messages normally
- Useful for handling messages that would otherwise be dropped or lost

## Message Types

### 1. PriorityMessage
A message with an explicit priority level.

### 2. ControlMessage
A custom control message.

### 3. AkkaControlMessage
A message that implements Akka's ControlMessage interface for use with ControlAwareMailbox.

### 4. SimpleMessage
A regular message.

## Usage

The `MailboxExamplesMain` class demonstrates how to:
1. Configure different mailbox types using Akka's configuration system
2. Use built-in mailbox types directly via configuration
3. Create actors with specific mailbox types
4. Send different types of messages to demonstrate mailbox behavior

### Configuring Mailboxes

The example shows how to configure mailboxes using Akka's configuration system:

```java
Config config = ConfigFactory.parseString(
        "akka.actor.default-mailbox.mailbox-type = \"akka.dispatch.UnboundedMailbox\"\n" +
                "bounded-mailbox.mailbox-type = \"akka.dispatch.BoundedMailbox\"\n" +
                "bounded-mailbox.mailbox-capacity = 3\n" +
                "priority-mailbox.mailbox-type = \"akka.dispatch.UnboundedPriorityMailbox\"\n" +
                "stable-priority-mailbox.mailbox-type = \"akka.dispatch.UnboundedStablePriorityMailbox\"\n" +
                "control-aware-mailbox.mailbox-type = \"akka.dispatch.UnboundedControlAwareMailbox\"\n" +
                "custom-priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.CustomPriorityMailbox\"\n" +
                "dead-letter-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.DeadLetterMailbox\""
);
```

### Assigning Mailboxes to Actors

Actors can be assigned specific mailboxes using the `withMailbox` method:

```java
// Built-in Priority Mailbox
ActorRef priorityActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("priority-mailbox"), "priorityActor");
```

## Running the Examples

### Running All Examples Together

To run all examples together:

```bash
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.MailboxExamplesMain"
```

The main class has been refactored to run each mailbox example separately with clear separation between them, making the logs more understandable.

### Running Individual Examples

You can also run examples for specific mailbox types independently:

```bash
# Default Unbounded Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.DefaultMailboxExample"

# Bounded Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.BoundedMailboxExample"

# Custom Priority Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.CustomPriorityMailboxExample"

# Priority Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.PriorityMailboxExample"

# Stable Priority Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.StablePriorityMailboxExample"

# Control Aware Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.ControlAwareMailboxExample"

# Dead Letter Mailbox
mvn clean compile exec:java -Dexec.mainClass="de.fhmuenster.mailboxexamples.examples.DeadLetterMailboxExample"
```

## Expected Output

The output will show how different mailboxes process messages:
- Priority mailboxes will process messages in priority order (lower numbers first)
- Stable priority mailbox will maintain order within priority levels
- Control-aware mailbox will process control messages before regular messages
- Dead letter mailbox will log messages before processing them

This demonstrates the different behaviors and use cases for each mailbox type.

Running examples individually provides clearer logs focused on a specific mailbox type, which is helpful for understanding each mailbox's behavior in isolation.
