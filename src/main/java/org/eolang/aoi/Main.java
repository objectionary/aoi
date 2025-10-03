/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import org.eolang.aoi.cli.cmd.AoiCommand;
import org.eolang.aoi.cli.cmd.CliCommands;
import org.eolang.aoi.cli.cmd.HelpCommand;
import org.eolang.aoi.cli.cmd.VersionCommand;

/**
 * The main entry point for the AOI (Abstract Object Inference) application.
 * <p>
 * This class is responsible for composing the application's command structure and launching the
 * command-line interface.
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
     * Runs the application, exiting with the resulting status code.
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        System.exit(
            new Application(
                new CliCommands(
                    new HelpCommand(args, System.out),
                    new VersionCommand(args, System.out),
                    new AoiCommand(args, System.out)
                ),
                System.err
            ).run()
        );
    }
}
