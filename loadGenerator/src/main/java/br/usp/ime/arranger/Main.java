package br.usp.ime.arranger;

import java.io.IOException;

public final class Main {
    private static AbstractLoadGenerator loadGen = null;

    private Main() {
    }

    public static void main(final String[] args) throws IOException {
        try {
            readArgs(args);
            loadGen.generateLoad(args, 2);
        } catch (IllegalArgumentException e) {
            printHelp();
        }
    }

    private static void readArgs(final String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }

        loadGen = getLoadGenerator(args[0]);
        AbstractClient.setWsdl(args[1]);
    }

    private static AbstractLoadGenerator getLoadGenerator(final String type) {
        AbstractLoadGenerator loadGen;

        if ("simultaneous".equals(type)) {
            loadGen = new Simultaneous();
        } else if ("frequency".equals(type)) {
            loadGen = new Frequency();
        } else {
            throw new IllegalArgumentException();
        }

        return loadGen;
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private static void printHelp() {
        System.out.println("Parameters: {simultaneous,frequency} wsdl");
        System.out.println("Extra parameters for simultaneous client mode:");
        System.out.println("\t" + Simultaneous.getHelpMessage());
        System.out.println("Extra parameters for frequency mode:");
        System.out.println("\t" + Frequency.getHelpMessage());
    }
}