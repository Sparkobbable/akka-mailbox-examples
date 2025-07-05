package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;

/**
 * Simple Priority Mailbox that only prioritizes based on message type.
 * This is a simpler implementation compared to CustomPriorityMailbox.
 * Lower numbers = higher priority
 */
public class SimplePriorityMailbox extends UnboundedPriorityMailbox {
    public SimplePriorityMailbox(ActorSystem.Settings settings, Config config) {
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