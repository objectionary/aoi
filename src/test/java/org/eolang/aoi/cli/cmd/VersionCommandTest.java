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
 * Tests for {@link VersionCommand}.
 *
 * @since 0.0.5
 */
final class VersionCommandTest {
    @ParameterizedTest
    @MethodSource("matchingArguments")
    @SuppressWarnings("PMD.UseVarargs")
    void matchesOnVersionRequest(final String[] args) {
        MatcherAssert.assertThat(
            "VersionCommand should have matched on a version request, but it did not.",
            new VersionCommand(args, System.out).matches(),
            Matchers.is(true)
        );
    }

    @ParameterizedTest
    @MethodSource("nonMatchingArguments")
    @SuppressWarnings("PMD.UseVarargs")
    void doesNotMatchOnOtherArguments(final String[] args) {
        MatcherAssert.assertThat(
            "VersionCommand should not have matched on other arguments, but it did.",
            new VersionCommand(args, System.out).matches(),
            Matchers.is(false)
        );
    }

    @Test
    void printsVersionContentToOutputStream() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final String expected = "aoi version 0.0.0-test\n";
        try (PrintStream out = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new VersionCommand(new String[]{}, out).execute();
            MatcherAssert.assertThat(
                "VersionCommand did not print the expected version content.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.equalTo(expected)
            );
        }
    }

    private static Stream<Arguments> matchingArguments() {
        return Stream.of(
            Arguments.of((Object) new String[]{"--version"}),
            Arguments.of((Object) new String[]{"--version", "extra-arg"}),
            Arguments.of((Object) new String[]{"extra-arg", "--version"})
        );
    }

    private static Stream<Arguments> nonMatchingArguments() {
        return Stream.of(
            Arguments.of((Object) new String[]{}),
            Arguments.of((Object) new String[]{"some-arg"}),
            Arguments.of((Object) new String[]{"--help"}),
            Arguments.of((Object) new String[]{"/tmp/in", "/tmp/out"})
        );
    }
}
