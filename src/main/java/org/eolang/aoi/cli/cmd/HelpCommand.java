/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.io.PrintStream;
import java.util.List;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Displays help text from resources.
 * <p>
 * This command matches when either no arguments are provided or the {@code --help} flag is present.
 *
 * @since 0.0.4
 */
public final class HelpCommand implements CliCommand {
    /**
     * Command-line arguments.
     */
    private final List<String> args;

    /**
     * The output stream to print the help message to.
     */
    private final PrintStream out;

    /**
     * Primary constructor.
     *
     * @param args Command-line arguments.
     * @param out  The output stream to print the help message to.
     */
    public HelpCommand(final List<String> args, final PrintStream out) {
        this.args = args;
        this.out = out;
    }

    /**
     * Convenience constructor for string array.
     *
     * @param args Command-line arguments.
     * @param out  The output stream to print the help message to.
     */
    public HelpCommand(final String[] args, final PrintStream out) {
        this(List.of(args), out);
    }

    @Override
    public void execute() {
        this.out.print(
            new UncheckedText(new TextOf(new ResourceOf("org/eolang/aoi/help.txt"))).asString()
        );
    }

    @Override
    public boolean matches() {
        return this.args.isEmpty() || this.args.contains("--help");
    }
}
