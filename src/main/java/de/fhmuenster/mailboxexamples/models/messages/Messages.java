package de.fhmuenster.mailboxexamples.models.messages;

public class Messages {

    // Base message interface
    public interface Message {
        String getContent();
    }

    // Priority message with explicit priority level
    public static class PriorityMessage implements Message {
        private final String content;
        private final int priority;

        public PriorityMessage(String content, int priority) {
            this.content = content;
            this.priority = priority;
        }

        public String getContent() {
            return content;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return "PriorityMessage(" + content + ", priority=" + priority + ")";
        }
    }

    // Control message for ControlAwareMailbox
    public static class ControlMessage implements Message {
        private final String content;

        public ControlMessage(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "ControlMessage(" + content + ")";
        }
    }

    // Akka Control message that will be prioritized by ControlAwareMailbox
    public static class AkkaControlMessage implements Message, akka.dispatch.ControlMessage {
        private final String content;

        public AkkaControlMessage(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "AkkaControlMessage(" + content + ")";
        }
    }

    // Regular message
    public static class SimpleMessage implements Message {
        private final String content;

        public SimpleMessage(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "SimpleMessage(" + content + ")";
        }
    }
}
