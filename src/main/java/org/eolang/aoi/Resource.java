/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.aoi;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Represents a classpath resource that can be read as text.
 *
 * <p>A resource encapsulates access to files stored in the application's classpath
 * and provides their content as UTF-8 encoded strings.</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * String description = new Resource("/description.txt").content();
 * }</pre>
 *
 * @since 0.4
 */
public final class Resource {
    /**
     * The classpath to the resource.
     */
    private final String path;

    /**
     * Creates a new resource for the specified classpath.
     *
     * <p>The path should be an absolute classpath reference starting with '/'
     * for resources in the root of the classpath, or a relative path for resources relative to the
     * package of the calling class.</p>
     *
     * @param path The classpath to the resource
     */
    public Resource(final String path) {
        this.path = path;
    }

    /**
     * Reads the resource content as a UTF-8 encoded string.
     *
     * <p>Performance note: This method loads the entire resource into memory,
     * so it should not be used for very large files.</p>
     *
     * @return The complete content of the resource as a UTF-8 string
     * @throws IllegalArgumentException if the resource is not found in the classpath
     * @throws IllegalStateException    if an I/O error occurs while reading the resource
     */
    public String content() {
        try (InputStream stream = this.getClass().getResourceAsStream(this.path)) {
            if (stream == null) {
                throw new IllegalArgumentException(
                    "Classpath resource '%s' not found.".formatted(this.path)
                );
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException ex) {
            throw new IllegalStateException(
                "Failed to read classpath resource '%s'. Possible I/O error: %s"
                    .formatted(this.path, ex.getMessage()),
                ex
            );
        }
    }
}
