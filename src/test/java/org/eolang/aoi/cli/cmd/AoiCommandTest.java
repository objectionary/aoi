/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.eolang.aoi.cli.CliException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link AoiCommand}.
 *
 * @since 0.0.5
 */
final class AoiCommandTest {
    @ParameterizedTest
    @MethodSource("matchingArguments")
    @SuppressWarnings("PMD.UseVarargs")
    void matchesAnyArguments(final String[] args) {
        MatcherAssert.assertThat(
            "AoiCommand should have matched any arguments, but it did not.",
            new AoiCommand(args, System.out).matches(),
            Matchers.is(true)
        );
    }

    @Test
    void executesSuccessfullyOnTwoArguments() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final String expected = "Input directory: tmp/in, Output directory: tmp/out\n";
        try (PrintStream out = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new AoiCommand(new String[]{"tmp/in", "tmp/out"}, out).execute();
            MatcherAssert.assertThat(
                "AoiCommand did not produce the expected output for a valid execution.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.equalTo(expected)
            );
        }
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    void throwsExceptionOnIncorrectNumberOfArguments(final List<String> args) {
        Assertions.assertThrows(
            CliException.class,
            () -> new AoiCommand(args, System.out).execute(),
            String.format(
                "AoiCommand should have thrown an exception for %d arguments, but it did not.",
                args.size()
            )
        );
    }

    private static Stream<Arguments> matchingArguments() {
        return Stream.of(
            Arguments.of((Object) new String[]{}),
            Arguments.of((Object) new String[]{"--help"}),
            Arguments.of((Object) new String[]{"--unknown"}),
            Arguments.of((Object) new String[]{"one", "two", "three"})
        );
    }

    private static Stream<Arguments> invalidArguments() {
        return Stream.of(
            Arguments.of(Collections.emptyList()),
            Arguments.of(List.of("one")),
            Arguments.of(List.of("one", "two", "three"))
        );
    }
}
