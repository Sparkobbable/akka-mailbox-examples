package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.dispatch.Envelope;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import akka.dispatch.UnboundedMailbox;
import com.typesafe.config.Config;
import scala.Option;

/**
 * Dead Letter Mailbox that forwards messages to the system's dead letters.
 * This is useful for handling messages that would otherwise be dropped or lost.
 */
public class DeadLetterMailbox implements MailboxType, ProducesMessageQueue<MessageQueue> {

    public DeadLetterMailbox(ActorSystem.Settings settings, Config config) {
        // No initialization needed
    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new UnboundedMailbox.MessageQueue() {
            @Override
            public void enqueue(ActorRef receiver, Envelope handle) {
                System.out.println("DeadLetterMailbox: Forwarding message to dead letters: " + handle.message());

                if (system.isDefined()) {
                    ActorSystem actorSystem = system.get();
                    actorSystem.deadLetters().tell(
                        new DeadLetter(handle.message(), handle.sender(), receiver), 
                        handle.sender()
                    );
                }

                super.enqueue(receiver, handle);
            }
        };
    }
}
