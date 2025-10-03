/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link HelpCommand}.
 *
 * @since 0.0.5
 */
final class HelpCommandTest {
    @ParameterizedTest
    @MethodSource("matchingArguments")
    @SuppressWarnings("PMD.UseVarargs")
    void matchesOnHelpRequest(final String[] args) {
        MatcherAssert.assertThat(
            "HelpCommand should have matched on a help request, but it did not.",
            new HelpCommand(args, System.out).matches(),
            Matchers.is(true)
        );
    }

    @ParameterizedTest
    @MethodSource("nonMatchingArguments")
    @SuppressWarnings("PMD.UseVarargs")
    void doesNotMatchOnOtherArguments(final String[] args) {
        MatcherAssert.assertThat(
            "HelpCommand should not have matched on other arguments, but it did.",
            new HelpCommand(args, System.out).matches(),
            Matchers.is(false)
        );
    }

    @Test
    void printsHelpContentToOutputStream() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final String expected = "help message\n";
        try (PrintStream out = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new HelpCommand(new String[]{}, out).execute();
            MatcherAssert.assertThat(
                "HelpCommand did not print the expected help content.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.equalTo(expected)
            );
        }
    }

    private static Stream<Arguments> matchingArguments() {
        return Stream.of(
            Arguments.of((Object) new String[]{}),
            Arguments.of((Object) new String[]{"--help"}),
            Arguments.of((Object) new String[]{"--help", "extra-arg"}),
            Arguments.of((Object) new String[]{"extra-arg", "--help"})
        );
    }

    private static Stream<Arguments> nonMatchingArguments() {
        return Stream.of(
            Arguments.of((Object) new String[]{"some-arg"}),
            Arguments.of((Object) new String[]{"--version"}),
            Arguments.of((Object) new String[]{"/tmp/in", "/tmp/out"})
        );
    }
}
