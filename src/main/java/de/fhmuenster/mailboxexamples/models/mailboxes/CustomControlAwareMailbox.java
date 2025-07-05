package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.ProducesMessageQueue;
import akka.dispatch.UnboundedControlAwareMailbox;
import com.typesafe.config.Config;
import scala.Option;

/**
 * Control Aware Mailbox that prioritizes control messages over regular messages.
 */
public class CustomControlAwareMailbox implements MailboxType, ProducesMessageQueue<UnboundedControlAwareMailbox.MessageQueue> {

    public CustomControlAwareMailbox(ActorSystem.Settings settings, Config config) {
        // No configuration needed
    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new UnboundedControlAwareMailbox().create(owner, system);
    }
}
