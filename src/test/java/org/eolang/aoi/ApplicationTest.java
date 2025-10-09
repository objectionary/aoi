/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

/**
 * Tests for {@link Application}.
 *
 * @since 0.0.5
 */
final class ApplicationTest {
    @Test
    void applicationExitsWithZeroCodeOnSuccessfulExecution(
        @TempDir final Path input,
        @TempDir final Path output
    ) {
        final CommandLine cmd = new CommandLine(new Application());
        final int exit = cmd.execute(input.toString(), output.toString());
        MatcherAssert.assertThat(
            "Application did not exit with a zero code on successful execution",
            exit,
            Matchers.is(0)
        );
    }

    @Test
    void applicationPrintsNothingToStderrOnSuccessfulExecution(
        @TempDir final Path input,
        @TempDir final Path output
    ) {
        final CommandLine cmd = new CommandLine(new Application());
        final StringWriter stderr = new StringWriter();
        cmd.setErr(new PrintWriter(stderr));
        cmd.execute(input.toString(), output.toString());
        MatcherAssert.assertThat(
            "Application printed to stderr during a successful execution",
            stderr.toString(),
            Matchers.emptyString()
        );
    }

    @Test
    void applicationPrintsHelpMessageWhenHelpFlagIsPassed() {
        final CommandLine cmd = new CommandLine(new Application());
        final StringWriter stdout = new StringWriter();
        cmd.setOut(new PrintWriter(stdout));
        cmd.execute("--help");
        MatcherAssert.assertThat(
            "Application did not print the help message to stdout for the --help flag",
            stdout.toString(),
            Matchers.containsString("Usage:")
        );
    }

    @Test
    void applicationExitsWithZeroCodeWhenHelpFlagIsPassed() {
        final CommandLine cmd = new CommandLine(new Application());
        final int exit = cmd.execute("--help");
        MatcherAssert.assertThat(
            "Application did not exit with a zero code when the --help flag was used",
            exit,
            Matchers.is(0)
        );
    }

    @Test
    void applicationExitsWithNonZeroCodeWhenNoArgumentsAreProvided() {
        final CommandLine cmd = new CommandLine(new Application());
        cmd.setErr(new PrintWriter(new StringWriter()));
        final int exit = cmd.execute();
        MatcherAssert.assertThat(
            "Application did not exit with a non-zero code when required arguments were missing",
            exit,
            Matchers.not(0)
        );
    }

    @Test
    void applicationExitsWithNonZeroCodeWhenOneArgumentIsMissing() {
        final CommandLine cmd = new CommandLine(new Application());
        cmd.setErr(new PrintWriter(new StringWriter()));
        final int exit = cmd.execute("input/dir");
        MatcherAssert.assertThat(
            "Application did not exit with a non-zero code when one argument was missing",
            exit,
            Matchers.not(0)
        );
    }
}
