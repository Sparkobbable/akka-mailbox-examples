package de.fhmuenster.mailboxexamples.models.actors;

import akka.actor.AbstractActor;

// Einfache Actor-Klasse
public class SimpleActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    // sleep 10ms
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Empfangen: " + msg + " von " + getSelf());
                })
                .build();
    }
}