/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import picocli.CommandLine;

/**
 * Main entry point for the AOI (Abstract Object Inference) application.
 *
 * @since 0.0.2
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class Main {
    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
    }

    /**
     * Executes the CLI and terminates the JVM with the resulting exit code.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(final String[] args) {
        final int exit = new CommandLine(new Application()).execute(args);
        System.exit(exit);
    }
}
