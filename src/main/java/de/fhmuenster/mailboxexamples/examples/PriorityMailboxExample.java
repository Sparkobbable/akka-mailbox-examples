package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class PriorityMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.SimplePriorityMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("PriorityMailboxExample", config);

        ColoredOutput.printHeader("=== Priority Mailbox Example ===");

        ActorRef priorityActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("priority-mailbox"), "priorityActor");
        ColoredOutput.printActorCreation("Created priorityActor with UnboundedPriorityMailbox");

        ColoredOutput.printMessageSending("\n--- Built-in Priority Actor (UnboundedPriorityMailbox) ---");
        priorityActor.tell("low priority", ActorRef.noSender());
        priorityActor.tell("high priority", ActorRef.noSender());
        priorityActor.tell("medium priority", ActorRef.noSender());
        priorityActor.tell(new PriorityMessage("Priority 2", 2), ActorRef.noSender());

        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }
}