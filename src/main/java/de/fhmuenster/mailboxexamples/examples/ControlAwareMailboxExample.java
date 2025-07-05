package de.fhmuenster.mailboxexamples.examples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages.AkkaControlMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

public class ControlAwareMailboxExample {
    public static void main(String[] args) {
        // Akka configuration
        Config config = ConfigFactory.parseString(
                "control-aware-mailbox.mailbox-type = \"de.fhmuenster.mailboxexamples.models.mailboxes.CustomControlAwareMailbox\"\n"
        );

        ActorSystem system = ActorSystem.create("ControlAwareMailboxExample", config);

        ColoredOutput.printHeader("=== Control Aware Mailbox Example ===");

        ActorRef controlAwareActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("control-aware-mailbox"), "controlAwareActor");
        ColoredOutput.printActorCreation("Created controlAwareActor with UnboundedControlAwareMailbox");

        ColoredOutput.printMessageSending("\n--- Built-in Control Aware Actor (UnboundedControlAwareMailbox) ---");
        controlAwareActor.tell(new SimpleMessage("Regular message 1"), ActorRef.noSender());
        controlAwareActor.tell(new SimpleMessage("Regular message 2"), ActorRef.noSender());
        controlAwareActor.tell(new AkkaControlMessage("Control message - should be processed first"), ActorRef.noSender());
        controlAwareActor.tell(new SimpleMessage("Regular message 3"), ActorRef.noSender());

        try {
            ColoredOutput.printSystemInfo("\nWaiting for messages to be processed...");
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ColoredOutput.printHeader("\n=== Shutting down actor system ===");
        system.terminate();
    }
}