package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.MailboxType;
import akka.dispatch.MessageQueue;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.ProducesMessageQueue;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.ControlMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import scala.Option;

/**
 * Custom Priority Mailbox that prioritizes messages based on their type and content.
 * Lower numbers = higher priority
 */
public class CustomPriorityMailbox implements MailboxType, ProducesMessageQueue<MessageQueue> {
    private final PriorityGenerator priorityGenerator;

    public CustomPriorityMailbox(ActorSystem.Settings settings, Config config) {
        this.priorityGenerator = new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message instanceof PriorityMessage) {
                    return ((PriorityMessage) message).getPriority();
                }

                if (message instanceof ControlMessage) {
                    return 0;
                }

                if (message instanceof SimpleMessage) {
                    String content = ((SimpleMessage) message).getContent();
                    if (content.contains("urgent")) {
                        return 2; // Urgent simple messages get higher priority
                    }
                    return 5;
                }

                if (message instanceof String) {
                    String msg = (String) message;
                    if (msg.equals("high")) return 1;
                    if (msg.equals("medium")) return 5;
                    if (msg.equals("low")) return 10;

                    if (msg.contains("urgent")) return 2;
                    if (msg.contains("important")) return 3;
                    if (msg.contains("later")) return 8;
                }

                // Default priority for unknown messages
                return 5;
            }
        };
    }

    @Override
    public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
        return new UnboundedPriorityMailbox(priorityGenerator).create(owner, system);
    }
}
