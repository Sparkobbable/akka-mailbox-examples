package de.fhmuenster.mailboxexamples.models.mailboxes;

import akka.actor.ActorSystem;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;

import java.io.ObjectInputFilter;

// Eigene Priority-Mailbox (höhere Priorität = niedrigere Zahl)
class CustomPriorityMailbox extends UnboundedPriorityMailbox {
    public CustomPriorityMailbox(ActorSystem.Settings settings, ObjectInputFilter.Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message.equals("high")) return 0; // Höchste Priorität
                if (message.equals("medium")) return 5;
                if (message.equals("low")) return 10; // Niedrigste Priorität
                return 7; // Standard-Priorität
            }
        });
    }
}