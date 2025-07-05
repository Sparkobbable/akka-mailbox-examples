package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class DeadLetterMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "dead-letter-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.DeadLetterMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("DeadLetterMailboxExample", config);

        ColoredOutput.printHeader("=== Dead Letter Mailbox Example ===");

        ActorRef deadLetterActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("dead-letter-mailbox"), "deadLetterActor");
        ColoredOutput.printActorCreation("Created deadLetterActor with DeadLetterMailbox");

        ColoredOutput.printMessageSending("\n--- Dead Letter Actor (DeadLetterMailbox) ---");
        deadLetterActor.tell(new SimpleMessage("Message to dead letters"), ActorRef.noSender());
        deadLetterActor.tell("Another message to dead letters", ActorRef.noSender());

        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }
}