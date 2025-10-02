/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

/**
 * A command for the Command-Line Interface (CLI).
 *
 * @since 0.0.4
 */
public interface CliCommand {
    /**
     * Executes the command's logic.
     */
    void execute();

    /**
     * Checks if this command should handle arguments provided during its instantiation.
     *
     * @return Flag indicating if the command should be executed.
     */
    boolean matches();
}
