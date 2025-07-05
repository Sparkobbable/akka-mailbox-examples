package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class DefaultMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "akka.actor.default-mailbox.mailbox-type = \"akka.dispatch.UnboundedMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("DefaultMailboxExample", config);

        ColoredOutput.printHeader("=== Default Mailbox Example ===");

        ActorRef defaultActor = system.actorOf(Props.create(SimpleActor.class), "defaultActor");
        ColoredOutput.printActorCreation("Created defaultActor with UnboundedMailbox");

        ColoredOutput.printMessageSending("\n--- Default Actor (UnboundedMailbox) ---");
        defaultActor.tell(new SimpleMessage("Standard message"), ActorRef.noSender());

        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }
}