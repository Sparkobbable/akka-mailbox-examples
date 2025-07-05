package de.fhmuenster.mailboxexamples.models.actors;

import akka.actor.AbstractActor;
import de.fhmuenster.mailboxexamples.models.messages.Messages;
import de.fhmuenster.mailboxexamples.models.messages.Messages.Message;
import de.fhmuenster.mailboxexamples.models.messages.Messages.PriorityMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.ControlMessage;
import de.fhmuenster.mailboxexamples.models.messages.Messages.SimpleMessage;
import de.fhmuenster.mailboxexamples.utils.ColoredOutput;

// Einfache Actor-Klasse
public class SimpleActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    // For backward compatibility
                    processMessage(msg);
                })
                .match(PriorityMessage.class, msg -> {
                    processMessage("Priority(" + msg.getPriority() + "): " + msg.getContent());
                })
                .match(ControlMessage.class, msg -> {
                    processMessage("Control: " + msg.getContent());
                })
                .match(SimpleMessage.class, msg -> {
                    processMessage("Simple: " + msg.getContent());
                })
                .match(Messages.AkkaControlMessage.class, msg -> {
                    processMessage("Akka Control: " + msg.getContent());
                })
                .matchAny(msg -> {
                    processMessage("Unknown message type: " + msg);
                })
                .build();
    }

    private void processMessage(String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Extract mailbox type from actor name
        String actorName = getSelf().path().name();
        String mailboxType = getMailboxTypeFromActorName(actorName);

        // Add mailbox type marker to the output message
        String outputMessage = "[" + mailboxType + "] Empfangen: " + message + " von " + getSelf();

        if (message.startsWith("Priority")) {
            ColoredOutput.printPriorityMessage(outputMessage);
        } else if (message.startsWith("Control")) {
            ColoredOutput.printControlMessage(outputMessage);
        } else if (message.startsWith("Simple")) {
            ColoredOutput.printSimpleMessage(outputMessage);
        } else if (message.contains("high") || message.contains("medium") || message.contains("low")) {
            ColoredOutput.printPriorityMessage(outputMessage);
        } else {
            ColoredOutput.printReceivedMessage(outputMessage);
        }
    }

    /**
     * Determines the mailbox type based on the actor name.
     * @param actorName The name of the actor
     * @return The mailbox type as a string
     */
    private String getMailboxTypeFromActorName(String actorName) {
        switch (actorName) {
            case "defaultActor":
                return "UNBOUNDED";
            case "boundedActor":
                return "BOUNDED";
            case "customPriorityActor":
                return "CUSTOM-PRIORITY";
            case "priorityActor":
                return "PRIORITY";
            case "stablePriorityActor":
                return "STABLE-PRIORITY";
            case "controlAwareActor":
                return "CONTROL-AWARE";
            case "deadLetterActor":
                return "DEAD-LETTER";
            default:
                return "UNKNOWN";
        }
    }
}
