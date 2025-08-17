/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.util.logging.Logger;

/**
 * Main entry point for the AOI (Abstract Object Inference) application.
 *
 * @since 0.2
 */
@SuppressWarnings("PMD.ProhibitPublicStaticMethods")
public final class Main {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
    }

    /**
     * Runs the application.
     *
     * @param args Command-line arguments
     */
    public static void main(final String[] args) {
        Main.LOGGER.info("Abstract Object inference for EO Programs");
    }
}
