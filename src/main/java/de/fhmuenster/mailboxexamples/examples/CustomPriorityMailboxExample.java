package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class CustomPriorityMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "custom-priority-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.CustomPriorityMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("CustomPriorityMailboxExample", config);

        ColoredOutput.printHeader("=== Custom Priority Mailbox Example ===");

        ActorRef customPriorityActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("custom-priority-mailbox"), "customPriorityActor");
        ColoredOutput.printActorCreation("Created customPriorityActor with CustomPriorityMailbox");

        ColoredOutput.printMessageSending("\n--- Custom Priority Actor (CustomPriorityMailbox) ---");
        customPriorityActor.tell("low", ActorRef.noSender());
        customPriorityActor.tell("high", ActorRef.noSender());
        customPriorityActor.tell("medium", ActorRef.noSender());
        customPriorityActor.tell(new PriorityMessage("Explicit priority 1", 1), ActorRef.noSender());
        customPriorityActor.tell(new PriorityMessage("Explicit priority 8", 8), ActorRef.noSender());
        customPriorityActor.tell(new SimpleMessage("urgent message"), ActorRef.noSender());

        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }
}