package de.textmode.tiffdumper;

/*
 * Copyright 2018 Michael Knigge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.FileDirectoryEntry;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;


/**
 * The {@link TiffDumper} parses a TIFF file and prints contained tags.
 */
public final class TiffDumper {

    private static final Package TIFFDUMPER_PACKAGE = TiffDumper.class.getPackage();
    private static final String TIFFDUMPER_DEFAULT_VERSION = "0.0";
    private static final String TIFFDUMPER_DEFAULT_URL = "https://github.com/michaelknigge/tiffdumper";

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private final boolean quiet;
    private final boolean verbose; // TODO: remove verbose-tag if it's not used when TIFF-Dumper is released....


    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the TIFF data stream.
     *
     * @param quiet   true if no header line should be printed.
     * @param verbose   true if more details should be printed.
     */
    TiffDumper(final boolean quiet, final boolean verbose) {
        this.quiet = quiet;
        this.verbose = verbose;
    }

    /**
     * Performs the parsing and dumping of the TIFF image.
     *
     * @param in    the input TIFF image to be parsed.
     * @param out   the output stream to which the dump will be written.
     *
     * @throws IOException if an I/O error occurs
     */
    public void dump(final InputStream in, final PrintStream out) throws IOException {

        if (!quiet) {
            out.println("TIFF-Dumper " + getImplementationVersion() + " - " + getImplementationVendor());
            out.println("------------------------------------------------------------------------------");
            out.println(" ");
        }

        final TIFFImage tiff = TiffReader.readTiff(in);
        final List<FileDirectory> directories = tiff.getFileDirectories();

        out.println("Directories : " + directories.size());
        out.println("Header size : " + tiff.sizeHeaderAndDirectories());

        for (int ix = 0; ix < directories.size(); ++ix) {
            dumpFileDirectory(directories.get(ix), ix + 1, out);
        }
    }

    private static void dumpFileDirectory(
            final FileDirectory directory,
            final int directoryNumber,
            final PrintStream out) {

        out.println(" ");
        out.println("Directory #" + directoryNumber);
        out.println("----------------------------------------------------------------------");

        out.println("Number of entries : " + directory.numEntries());
        out.println("Size of directory : " + directory.size());
        out.println(" ");

        for (final FileDirectoryEntry entry : directory.getEntries()) {
            dumpFileDirectoryEntry(entry, out);
        }
    }

    private static void dumpFileDirectoryEntry(final FileDirectoryEntry entry, final PrintStream out) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s", entry.getFieldTag()));
        sb.append(String.format("%-12s", entry.getFieldType()));
        sb.append("value here...."); // TODO... Determine and format the real value... be aware of arrays!

        out.println(sb.toString());
    }

    private static String bytesToHexString(final byte[] data) {
        final char[] hexChars = new char[data.length * 2];

        for (int ix = 0; ix < data.length; ix++) {
            final int value = data[ix] & 0xFF;
            hexChars[ix * 2] = HEX_DIGITS[value >>> 4];
            hexChars[ix * 2 + 1] = HEX_DIGITS[value & 0x0F];
        }

        return new String(hexChars);
    }

    private String getImplementationVersion() {
        if (TIFFDUMPER_PACKAGE == null || TIFFDUMPER_PACKAGE.getImplementationVersion() == null) {
            // This is only used for our JUnit tests because if we run them out of our IDE there is no
            // ready to use MANIFEST.MF where we can get the current version from....
            return TIFFDUMPER_DEFAULT_VERSION;
        } else {
            return TIFFDUMPER_PACKAGE.getImplementationVersion();
        }
    }

    private String getImplementationVendor() {
        if (TIFFDUMPER_PACKAGE == null || TIFFDUMPER_PACKAGE.getImplementationVendor() == null) {
            // This is only used for our JUnit tests because if we run them out of our IDE there is no
            // ready to use MANIFEST.MF where we can get the current URL from....
            return TIFFDUMPER_DEFAULT_URL;
        } else {
            return TIFFDUMPER_PACKAGE.getImplementationVendor();
        }
    }
}
