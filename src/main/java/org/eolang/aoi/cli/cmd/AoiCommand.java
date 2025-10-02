/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.io.PrintStream;
import java.util.List;
import org.eolang.aoi.cli.CliException;

/**
 * Command that executes the AOI tool logic.
 * <p>
 * This command acts as a fallback, always matching the provided arguments.
 * It requires exactly two arguments: an input directory and an output directory.
 * Throws a {@link CliException} during execution if the argument count is not 2.
 *
 * @since 0.0.4
 */
public final class AoiCommand implements CliCommand {
    /**
     * Command-line arguments.
     */
    private final List<String> args;

    /**
     * The output stream for logging or results.
     */
    private final PrintStream out;

    /**
     * Primary constructor.
     *
     * @param args Command-line arguments.
     * @param out  The output stream.
     */
    public AoiCommand(final List<String> args, final PrintStream out) {
        this.args = args;
        this.out = out;
    }

    /**
     * Convenience constructor for string array.
     *
     * @param args Command-line arguments.
     * @param out  The output stream.
     */
    public AoiCommand(final String[] args, final PrintStream out) {
        this(List.of(args), out);
    }

    @Override
    public void execute() {
        if (this.args.size() != 2) {
            throw this.invalidArgCountException();
        }
        this.out.printf(
            "Input directory: %s, Output directory: %s%n",
            this.args.get(0),
            this.args.get(1)
        );
    }

    @Override
    public boolean matches() {
        return true;
    }

    /**
     * Creates an exception for an invalid number of arguments.
     *
     * @return A new {@link CliException} with a descriptive message.
     */
    private CliException invalidArgCountException() {
        return new CliException(
            String.format(
                "Expected 2 arguments (input dir and output dir), but got %d: %s",
                this.args.size(),
                this.args
            )
        );
    }
}
