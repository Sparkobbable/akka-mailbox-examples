package de.fhmuenster.mailboxexamples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.fhmuenster.mailboxexamples.models.actors.SimpleActor;

public class MailboxExamplesMain {
    public static void main(String[] args) {
        // Akka-Konfiguration mit verschiedenen Mailboxen
        Config config = ConfigFactory.parseString(
                "akka.actor.default-mailbox.mailbox-type = \"akka.dispatch.UnboundedMailbox\"\n" +
                        "bounded-mailbox.mailbox-type = \"akka.dispatch.BoundedMailbox\"\n" +
                        "bounded-mailbox.mailbox-capacity = 3\n" +
                        "priority-mailbox.mailbox-type = \"CustomPriorityMailbox\""
        );

        ActorSystem system = ActorSystem.create("MailboxExample", config);

        // Standard-Mailbox (Unbounded)
        ActorRef defaultActor = system.actorOf(Props.create(SimpleActor.class), "defaultActor");

        // Begrenzte Mailbox (Bounded)
        ActorRef boundedActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("bounded-mailbox"), "boundedActor");

        // Prioritäts-Mailbox
        ActorRef priorityActor = system.actorOf(Props.create(SimpleActor.class).withMailbox("priority-mailbox"), "priorityActor");

        // Nachrichten an die Actors senden
        defaultActor.tell("Standard Nachricht 1", ActorRef.noSender());
        boundedActor.tell("Bounded 1", ActorRef.noSender());
        boundedActor.tell("Bounded 2", ActorRef.noSender());
        boundedActor.tell("Bounded 3", ActorRef.noSender());
        boundedActor.tell("Bounded 4 (sollte verworfen werden)", ActorRef.noSender()); // Diese Nachricht wird evtl. verworfen

        priorityActor.tell("low", ActorRef.noSender());
        priorityActor.tell("high", ActorRef.noSender());
        priorityActor.tell("medium", ActorRef.noSender());

        // System nach kurzer Verzögerung herunterfahren
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system.terminate();
    }
}
