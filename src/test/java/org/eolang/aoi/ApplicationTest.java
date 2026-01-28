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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

/**
 * Tests for {@link Application}.
 *
 * @since 0.0.5
 * @todo #152:15min Enable help flag tests after adding support for the short `-h` flag.
 *  Once implemented, remove @Disabled from the following tests:
 *  {@link ApplicationTest#applicationPrintsHelpMessageWhenHelpFlagIsPassed},
 *  {@link ApplicationTest#applicationExitsWithZeroCodeWhenHelpFlagIsPassed}
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

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"--help", "-h"})
    void applicationPrintsHelpMessageWhenHelpFlagIsPassed(final String flag) {
        final CommandLine cmd = new CommandLine(new Application());
        final StringWriter stdout = new StringWriter();
        cmd.setOut(new PrintWriter(stdout));
        cmd.execute(flag);
        MatcherAssert.assertThat(
            "Application did not print the help message to stdout for the %s flag".formatted(flag),
            stdout.toString(),
            Matchers.containsString("Usage:")
        );
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"--help", "-h"})
    void applicationExitsWithZeroCodeWhenHelpFlagIsPassed(final String flag) {
        final CommandLine cmd = new CommandLine(new Application());
        final int exit = cmd.execute(flag);
        MatcherAssert.assertThat(
            "Application did not exit with a zero code when the %s flag was used".formatted(flag),
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
