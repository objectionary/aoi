/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi.cli;

/**
 * A custom runtime exception for command-line interface (CLI) errors.
 *
 * @since 0.0.5
 */
public final class CliException extends RuntimeException {

    /**
     * The serialization version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param message The detail message.
     */
    public CliException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The detail message.
     * @param cause   The cause of this exception.
     */
    public CliException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
