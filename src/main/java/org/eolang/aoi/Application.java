/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Command-line interface for the AOI (Abstract Object Inference) tool.
 *
 * <p>This class handles command-line argument parsing and coordinates the execution
 * of the AOI tool.</p>
 *
 * @since 0.4
 */
public final class Application {

    /**
     * Command-line arguments passed to the application.
     */
    private final String[] args;

    /**
     * Output stream for printing messages and help text.
     */
    private final PrintStream out;

    /**
     * Creates a new application instance.
     *
     * <p>The arguments array is defensively copied to prevent external modification.</p>
     *
     * @param args Command-line arguments
     * @param out  Output stream for messages
     */
    public Application(final String[] args, final PrintStream out) {
        this.args = Arrays.copyOf(args, args.length);
        this.out = out;
    }

    /**
     * Executes the main application logic.
     *
     * <p>Note: {@code --help} and {@code --version} flags take precedence over other arguments and
     * will be processed even if other arguments are invalid.</p>
     *
     * @throws IllegalArgumentException if the number of arguments is not exactly 2 (when neither
     *  {@code --help} nor {@code --version} is specified)
     */
    public void run() {
        final List<String> arguments = Arrays.asList(this.args);
        if (arguments.contains("--help")) {
            this.out.println(new Resource("/org/eolang/aoi/help.txt").content());
        } else if (arguments.contains("--version")) {
            this.out.printf(
                "aoi version %s%n",
                new Resource("/org/eolang/aoi/version.txt").content()
            );
        } else {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException(
                    "Expected 2 arguments, but got %d argument(s)".formatted(arguments.size())
                );
            }
            this.out.printf(
                "Input directory: %s, Output directory: %s%n", this.args[0], this.args[1]
            );
        }
    }
}
