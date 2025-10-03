/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.io.PrintStream;
import org.eolang.aoi.cli.CliException;
import org.eolang.aoi.cli.cmd.CliCommand;

/**
 * Command-line interface for the AOI (Abstract Object Inference) tool.
 * <p>
 * This class coordinates the execution of a given command and handles any resulting exceptions,
 * translating them into a process exit code.
 *
 * @since 0.0.5
 */
public final class Application {
    /**
     * The command to be executed.
     */
    private final CliCommand cmd;

    /**
     * The stream for reporting error messages.
     */
    private final PrintStream err;

    /**
     * Primary constructor.
     *
     * @param cmd The command to execute.
     * @param err The error output stream.
     */
    public Application(final CliCommand cmd, final PrintStream err) {
        this.cmd = cmd;
        this.err = err;
    }

    /**
     * Runs the application's command and translates the outcome into an exit code.
     * <p>
     * It executes the command and handles both controlled {@link CliException} and unexpected
     * generic {@link Exception} failures by printing a message to the error stream.
     *
     * @return An exit code, which is 0 for success and 1 for any error.
     */
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public int run() {
        int code;
        try {
            this.cmd.execute();
            code = 0;
        } catch (final CliException ex) {
            this.err.printf("Error: %s%n", ex.getMessage());
            code = 1;
            // @checkstyle IllegalCatch (1 line)
        } catch (final Exception ex) {
            this.err.printf("Unexpected error: %s%n", ex.getMessage());
            ex.printStackTrace(this.err);
            code = 1;
        }
        return code;
    }
}
