package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class BoundedMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "bounded-mailbox.mailbox-type = \"akka.dispatch.BoundedMailbox\"\n" +
                "bounded-mailbox.mailbox-capacity = 3\n" +
                "bounded-mailbox.mailbox-push-timeout-time = 0ms\n"
        );

        ActorSystem system = ActorSystem.create("BoundedMailboxExample", config);

        ColoredOutput.printHeader("=== Bounded Mailbox Example ===");

        ActorRef boundedActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("bounded-mailbox"), "boundedActor");
        ColoredOutput.printActorCreation("Created boundedActor with BoundedMailbox");

        ColoredOutput.printMessageSending("\n--- Bounded Actor (BoundedMailbox) ---");
        boundedActor.tell(new SimpleMessage("Bounded message 1"), ActorRef.noSender());
        boundedActor.tell(new SimpleMessage("Bounded message 2"), ActorRef.noSender());
        boundedActor.tell(new SimpleMessage("Bounded message 3"), ActorRef.noSender());
        boundedActor.tell(new SimpleMessage("Bounded message 4 (might be dropped)"), ActorRef.noSender());

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
