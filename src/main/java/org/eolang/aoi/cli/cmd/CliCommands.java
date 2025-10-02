/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.util.List;

/**
 * A composite of multiple CLI commands that acts as a single command.
 * <p>
 * It delegates execution to the first matching command from its collection.
 *
 * @since 0.0.4
 */
public final class CliCommands implements CliCommand {
    /**
     * A collection of commands to delegate to.
     */
    private final List<CliCommand> commands;

    /**
     * Primary constructor.
     *
     * @param commands The list of commands to manage.
     */
    public CliCommands(final List<CliCommand> commands) {
        this.commands = commands;
    }

    /**
     * Convenience constructor for varargs.
     *
     * @param commands The commands to manage.
     */
    public CliCommands(final CliCommand... commands) {
        this(List.of(commands));
    }

    @Override
    public void execute() {
        this.commands.stream()
            .filter(CliCommand::matches)
            .findFirst()
            .orElseThrow(this::noMatchException)
            .execute();
    }

    @Override
    public boolean matches() {
        return this.commands.stream().anyMatch(CliCommand::matches);
    }

    /**
     * Creates an exception to be thrown when no command matches.
     *
     * @return A new {@link IllegalArgumentException} with a descriptive message.
     */
    private RuntimeException noMatchException() {
        final List<String> names = this.commands.stream()
            .map(c -> c.getClass().getSimpleName())
            .toList();
        return new IllegalArgumentException(
            String.format("No matching CLI command found among %s", names)
        );
    }
}
