/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eolang.aoi.cli.CliException;
import org.eolang.aoi.cli.cmd.CliCommand;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Application}.
 *
 * @since 0.0.5
 */
final class ApplicationTest {
    @Test
    void executesCommand() {
        final FakeCommand command = new FakeCommand();
        try (PrintStream err = new PrintStream(OutputStream.nullOutputStream())) {
            new Application(command, err).run();
            MatcherAssert.assertThat(
                "The command should have been executed, but it was not.",
                command.wasExecuted(),
                Matchers.is(true)
            );
        }
    }

    @Test
    void returnsZeroOnSuccess() {
        try (PrintStream err = new PrintStream(OutputStream.nullOutputStream())) {
            MatcherAssert.assertThat(
                "Application should return exit code 0 on successful execution, but it did not.",
                new Application(new FakeCommand(), err).run(),
                Matchers.is(0)
            );
        }
    }

    @Test
    void doesNotWriteToErrorStreamOnSuccess() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (PrintStream err = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new Application(new FakeCommand(), err).run();
            MatcherAssert.assertThat(
                "Application should not write to the error stream on success, but it did.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.emptyString()
            );
        }
    }

    @Test
    void returnsOneOnCliException() {
        try (PrintStream err = new PrintStream(OutputStream.nullOutputStream())) {
            MatcherAssert.assertThat(
                "Application should return exit code 1 on a CLI exception, but it did not.",
                new Application(
                    new FakeCommand(new CliException("Fake cli exception")),
                    err
                ).run(),
                Matchers.is(1)
            );
        }
    }

    @Test
    void printsMessageOnCliException() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (PrintStream err = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new Application(
                new FakeCommand(new CliException("Fake cli exception")),
                err
            ).run();
            MatcherAssert.assertThat(
                "Application should write to the error stream on a CLI exception, but it did not.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.not(Matchers.emptyString())
            );
        }
    }

    @Test
    void returnsOneOnUnexpectedException() {
        try (PrintStream err = new PrintStream(OutputStream.nullOutputStream())) {
            MatcherAssert.assertThat(
                "Application should return exit code 1 on an unexpected exception, but it did not.",
                new Application(
                    new FakeCommand(new IllegalStateException("Fake unexpected exception")),
                    err
                ).run(),
                Matchers.is(1)
            );
        }
    }

    @Test
    void printsMessageOnUnexpectedException() {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (PrintStream err = new PrintStream(stream, true, StandardCharsets.UTF_8)) {
            new Application(
                new FakeCommand(new IllegalStateException("Fake unexpected exception")),
                err
            ).run();
            MatcherAssert.assertThat(
                "Application should write to the error stream on an unexpected exception, but it did not.",
                stream.toString(StandardCharsets.UTF_8),
                Matchers.not(Matchers.emptyString())
            );
        }
    }

    /**
     * A fake command that can be configured to succeed or fail, and tracks its execution.
     *
     * @since 0.0.5
     */
    private static final class FakeCommand implements CliCommand {
        /**
         * The exception to be thrown on execute, or null for success.
         */
        private final RuntimeException error;

        /**
         * A flag to record if execute() was called.
         */
        private final AtomicBoolean executed;

        /**
         * Primary constructor.
         *
         * @param err The exception to throw.
         */
        private FakeCommand(final RuntimeException err) {
            this.error = err;
            this.executed = new AtomicBoolean(false);
        }

        /**
         * Convenience constructor for a successful command.
         */
        private FakeCommand() {
            this(null);
        }

        @Override
        public void execute() {
            this.executed.set(true);
            if (this.error != null) {
                throw this.error;
            }
        }

        @Override
        public boolean matches() {
            return true;
        }

        /**
         * Checks if the execute() method was called.
         *
         * @return Flag indicating if execute() was called.
         */
        private boolean wasExecuted() {
            return this.executed.get();
        }
    }
}
