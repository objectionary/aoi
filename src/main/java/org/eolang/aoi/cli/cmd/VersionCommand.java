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
 * Displays the application version from a resource file.
 * <p>
 * This command matches when the {@code --version} flag is present.
 *
 * @since 0.0.4
 */
public final class VersionCommand implements CliCommand {
    /**
     * Command-line arguments.
     */
    private final List<String> args;

    /**
     * The output stream to print the version to.
     */
    private final PrintStream out;

    /**
     * Primary constructor.
     *
     * @param args Command-line arguments.
     * @param out  The output stream for the version info.
     */
    public VersionCommand(final List<String> args, final PrintStream out) {
        this.args = args;
        this.out = out;
    }

    /**
     * Convenience constructor for string array.
     *
     * @param args Command-line arguments.
     * @param out  The output stream for the version info.
     */
    public VersionCommand(final String[] args, final PrintStream out) {
        this(List.of(args), out);
    }

    @Override
    public void execute() {
        this.out.printf(
            "aoi version %s",
            new UncheckedText(new TextOf(new ResourceOf("org/eolang/aoi/version.txt"))).asString()
        );
    }

    @Override
    public boolean matches() {
        return this.args.contains("--version");
    }
}
