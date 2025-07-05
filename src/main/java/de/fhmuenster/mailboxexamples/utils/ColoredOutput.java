package de.fhmuenster.mailboxexamples.utils;

public class ColoredOutput {
    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // ANSI escape codes for text styles
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    public static void printHeader(String text) {
        System.out.println(BOLD + BLUE + text + RESET);
    }

    public static void printActorCreation(String text) {
        System.out.println(GREEN + text + RESET);
    }

    public static void printMessageSending(String text) {
        System.out.println(YELLOW + text + RESET);
    }

    public static void printReceivedMessage(String text) {
        System.out.println(CYAN + text + RESET);
    }

    public static void printPriorityMessage(String text) {
        System.out.println(RED + text + RESET);
    }

    public static void printSimpleMessage(String text) {
        System.out.println(WHITE + text + RESET);
    }

    public static void printSystemInfo(String text) {
        System.out.println(BOLD + YELLOW + text + RESET);
    }

    public static void printControlMessage(String text) {
        System.out.println(BOLD + PURPLE + text + RESET);
    }
}