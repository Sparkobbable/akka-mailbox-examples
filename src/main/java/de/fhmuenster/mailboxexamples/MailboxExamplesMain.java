package de.fhmuenster.mailboxexamples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.*;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class MailboxExamplesMain {
    public static void main(String[] args) {
        // Akka configuration with different mailboxes
        Config config = ConfigFactory.parseString(
                "akka.actor.default-mailbox.mailbox-type = \"akka.dispatch.UnboundedMailbox\"\n" +
                        "bounded-mailbox.mailbox-type = \"akka.dispatch.BoundedMailbox\"\n" +
                        "bounded-mailbox.mailbox-capacity = 3\n" +
                        "bounded-mailbox.mailbox-push-timeout-time = 0ms\n" +
                        "priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.SimplePriorityMailbox\"\n" +
                        "stable-priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.StablePriorityMailbox\"\n" +
                        "control-aware-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.CustomControlAwareMailbox\"\n" +
                        "custom-priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.CustomPriorityMailbox\"\n" +
                        "dead-letter-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.DeadLetterMailbox\""
        );

        ActorSystem system = ActorSystem.create("MailboxExample", config);

        ColoredOutput.printHeader("=== Starting Mailbox Examples ===");

        ActorRef defaultActor = createDefaultActor(system);
        ActorRef boundedActor = createBoundedActor(system);
        ActorRef customPriorityActor = createCustomPriorityActor(system);
        ActorRef priorityActor = createPriorityActor(system);
        ActorRef stablePriorityActor = createStablePriorityActor(system);
        ActorRef controlAwareActor = createControlAwareActor(system);
        ActorRef deadLetterActor = createDeadLetterActor(system);

        ColoredOutput.printHeader("\n=== Sending messages to actors ===");

        runDefaultMailboxExample(defaultActor);
        waitForProcessing(2000);

        runBoundedMailboxExample(boundedActor);
        waitForProcessing(5000);

        runCustomPriorityMailboxExample(customPriorityActor);
        waitForProcessing(7000);

        runPriorityMailboxExample(priorityActor);
        waitForProcessing(5000);

        runStablePriorityMailboxExample(stablePriorityActor);
        waitForProcessing(5000);

        runControlAwareMailboxExample(controlAwareActor);
        waitForProcessing(6000);

        runDeadLetterMailboxExample(deadLetterActor);
        waitForProcessing(3000);

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }

    private static ActorRef createDefaultActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class), "defaultActor");
        ColoredOutput.printActorCreation("Created defaultActor with UnboundedMailbox");
        return actor;
    }

    private static ActorRef createBoundedActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("bounded-mailbox"), "boundedActor");
        ColoredOutput.printActorCreation("Created boundedActor with BoundedMailbox");
        return actor;
    }

    private static ActorRef createCustomPriorityActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("custom-priority-mailbox"), "customPriorityActor");
        ColoredOutput.printActorCreation("Created customPriorityActor with CustomPriorityMailbox");
        return actor;
    }

    private static ActorRef createPriorityActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("priority-mailbox"), "priorityActor");
        ColoredOutput.printActorCreation("Created priorityActor with UnboundedPriorityMailbox");
        return actor;
    }

    private static ActorRef createStablePriorityActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("stable-priority-mailbox"), "stablePriorityActor");
        ColoredOutput.printActorCreation("Created stablePriorityActor with UnboundedStablePriorityMailbox");
        return actor;
    }

    private static ActorRef createControlAwareActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("control-aware-mailbox"), "controlAwareActor");
        ColoredOutput.printActorCreation("Created controlAwareActor with UnboundedControlAwareMailbox");
        return actor;
    }

    private static ActorRef createDeadLetterActor(ActorSystem system) {
        ActorRef actor = system.actorOf(Props.create(SimpleActor.class).withMailbox("dead-letter-mailbox"), "deadLetterActor");
        ColoredOutput.printActorCreation("Created deadLetterActor with DeadLetterMailbox");
        return actor;
    }

    private static void runDefaultMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Default Actor (UnboundedMailbox) ---");
        actor.tell(new SimpleMessage("Standard message"), ActorRef.noSender());
    }

    private static void runBoundedMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Bounded Actor (BoundedMailbox) ---");
        actor.tell(new SimpleMessage("Bounded message 1"), ActorRef.noSender());
        actor.tell(new SimpleMessage("Bounded message 2"), ActorRef.noSender());
        actor.tell(new SimpleMessage("Bounded message 3"), ActorRef.noSender());
        actor.tell(new SimpleMessage("Bounded message 4 (might be dropped)"), ActorRef.noSender());
    }

    private static void runCustomPriorityMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Custom Priority Actor (CustomPriorityMailbox) ---");
        actor.tell("low", ActorRef.noSender());
        actor.tell("high", ActorRef.noSender());
        actor.tell("medium", ActorRef.noSender());
        actor.tell(new PriorityMessage("Explicit priority 1", 1), ActorRef.noSender());
        actor.tell(new PriorityMessage("Explicit priority 8", 8), ActorRef.noSender());
        actor.tell(new SimpleMessage("urgent message"), ActorRef.noSender());
    }

    private static void runPriorityMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Built-in Priority Actor (UnboundedPriorityMailbox) ---");
        actor.tell("low priority", ActorRef.noSender());
        actor.tell("high priority", ActorRef.noSender());
        actor.tell("medium priority", ActorRef.noSender());
        actor.tell(new PriorityMessage("Priority 2", 2), ActorRef.noSender());
    }

    private static void runStablePriorityMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Built-in Stable Priority Actor (UnboundedStablePriorityMailbox) ---");
        actor.tell(new PriorityMessage("First with priority 5", 5), ActorRef.noSender());
        actor.tell(new PriorityMessage("Second with priority 5", 5), ActorRef.noSender());
        actor.tell(new PriorityMessage("Third with priority 5", 5), ActorRef.noSender());
        actor.tell(new PriorityMessage("High priority", 1), ActorRef.noSender());
    }

    private static void runControlAwareMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Built-in Control Aware Actor (UnboundedControlAwareMailbox) ---");
        actor.tell(new SimpleMessage("Regular message 1"), ActorRef.noSender());
        actor.tell(new SimpleMessage("Regular message 2"), ActorRef.noSender());
        actor.tell(new AkkaControlMessage("Control message - should be processed first"), ActorRef.noSender());
        actor.tell(new SimpleMessage("Regular message 3"), ActorRef.noSender());
        actor.tell("control message as string", ActorRef.noSender());
    }

    private static void runDeadLetterMailboxExample(ActorRef actor) {
        ColoredOutput.printMessageSending("\n--- Dead Letter Actor (DeadLetterMailbox) ---");
        actor.tell(new SimpleMessage("Message to dead letters"), ActorRef.noSender());
        actor.tell("Another message to dead letters", ActorRef.noSender());
    }

    private static void waitForProcessing(int millis) {
        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
