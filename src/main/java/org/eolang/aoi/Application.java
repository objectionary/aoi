/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Command-line interface for the AOI tool.
 * <p>
 * Implementing {@link Callable} allows Picocli to treat this class as a command that returns an
 * exit code.
 * </p>
 *
 * @since 0.0.4
 */
@Command(
    name = "aoi",
    description = {
        "AOI analyzes EO programs to infer object types. It finds .xmir files in the",
        "input directory and generates .xml files with type information."
    },
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nArguments:%n",
    optionListHeading = "%nOptions:%n"
)
public final class Application implements Callable<Integer> {
    /**
     * A standard help option that triggers Picocli's built-in help display.
     */
    @Option(
        names = "--help",
        usageHelp = true,
        description = "Print this message and exit."
    )
    private boolean help;

    /**
     * The first positional argument, representing the input directory.
     */
    @Parameters(
        index = "0",
        paramLabel = "<input dir>",
        description = "Directory with .xmir files (searches recursively)"
    )
    private Path input;

    /**
     * The second positional argument, representing the output directory.
     */
    @Parameters(
        index = "1",
        paramLabel = "<output dir>",
        description = "Directory for .xml files with inferred types"
    )
    private Path output;

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
