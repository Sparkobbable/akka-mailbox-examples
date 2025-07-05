package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class StablePriorityMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "stable-priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.StablePriorityMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("StablePriorityMailboxExample", config);

        ColoredOutput.printHeader("=== Stable Priority Mailbox Example ===");

        ActorRef stablePriorityActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("stable-priority-mailbox"), "stablePriorityActor");
        ColoredOutput.printActorCreation("Created stablePriorityActor with UnboundedStablePriorityMailbox");

        ColoredOutput.printMessageSending("\n--- Built-in Stable Priority Actor (UnboundedStablePriorityMailbox) ---");
        stablePriorityActor.tell(new PriorityMessage("First with priority 5", 5), ActorRef.noSender());
        stablePriorityActor.tell(new PriorityMessage("Second with priority 5", 5), ActorRef.noSender());
        stablePriorityActor.tell(new PriorityMessage("Third with priority 5", 5), ActorRef.noSender());
        stablePriorityActor.tell(new PriorityMessage("High priority", 1), ActorRef.noSender());

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