package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedStablePriorityMailbox;
import com.typesafe.config.Config;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;

/**
 * Stable Priority Mailbox that maintains the order of messages with the same priority.
 * Unlike regular PriorityMailbox, this ensures that messages with equal priority
 * are processed in FIFO order (first in, first out).
 * Lower numbers = higher priority
 */
public class StablePriorityMailbox extends UnboundedStablePriorityMailbox {
    public StablePriorityMailbox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message instanceof PriorityMessage) {
                    return ((PriorityMessage) message).getPriority();
                }

                if (message instanceof String) {
                    String msg = (String) message;
                    if (msg.contains("high")) return 0;
                    if (msg.contains("medium")) return 5;
                    if (msg.contains("low")) return 10;
                }
                
                // Default priority
                return 5;
            }
        });
    }
}