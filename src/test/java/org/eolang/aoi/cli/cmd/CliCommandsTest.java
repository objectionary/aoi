/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli.cmd;

import java.util.concurrent.atomic.AtomicBoolean;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CliCommands}.
 *
 * @since 0.0.5
 */
final class CliCommandsTest {
    @Test
    void matchesIfAtLeastOneCommandMatches() {
        MatcherAssert.assertThat(
            "CliCommands should have matched because one of the commands matched, but it did not.",
            new CliCommands(
                new FakeCommand(false),
                new FakeCommand(true)
            ).matches(),
            Matchers.is(true)
        );
    }

    @Test
    void doesNotMatchIfNoCommandsProvided() {
        MatcherAssert.assertThat(
            "CliCommands should not have matched because no commands were provided, but it did.",
            new CliCommands().matches(),
            Matchers.is(false)
        );
    }

    @Test
    void doesNotMatchIfNoCommandsMatch() {
        MatcherAssert.assertThat(
            "CliCommands should not have matched because no commands matched, but it did.",
            new CliCommands(
                new FakeCommand(false),
                new FakeCommand(false)
            ).matches(),
            Matchers.is(false)
        );
    }

    @Test
    void executesFirstMatchingCommand() {
        final FakeCommand first = new FakeCommand(true);
        new CliCommands(first, new FakeCommand(true)).execute();
        MatcherAssert.assertThat(
            "The first matching command should have been executed, but it was not.",
            first.wasExecuted(),
            Matchers.is(true)
        );
    }

    @Test
    void doesNotExecuteCommandsAfterTheFirstMatch() {
        final FakeCommand second = new FakeCommand(true);
        new CliCommands(new FakeCommand(true), second).execute();
        MatcherAssert.assertThat(
            "The second command should not have been executed, but it was.",
            second.wasExecuted(),
            Matchers.is(false)
        );
    }

    @Test
    void throwsExceptionWhenNoCommandsProvided() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new CliCommands().execute(),
            "CliCommands should have thrown an exception when no commands were provided, but it did not."
        );
    }

    @Test
    void throwsExceptionWhenNoCommandMatches() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new CliCommands(
                new FakeCommand(false),
                new FakeCommand(false)
            ).execute(),
            "CliCommands should have thrown an exception when no command matched, but it did not."
        );
    }

    /**
     * A fake command that can be configured to match or not, and tracks its execution.
     *
     * @since 0.0.5
     */
    private static final class FakeCommand implements CliCommand {
        /**
         * The value to be returned by matches().
         */
        private final boolean matching;

        /**
         * A flag to record if execute() was called.
         */
        private final AtomicBoolean executed;

        /**
         * Constructor.
         *
         * @param matching The value to be returned by matches().
         */
        private FakeCommand(final boolean matching) {
            this.matching = matching;
            this.executed = new AtomicBoolean(false);
        }

        @Override
        public void execute() {
            this.executed.set(true);
        }

        @Override
        public boolean matches() {
            return this.matching;
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
