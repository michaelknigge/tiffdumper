package de.textmode.tiffdumper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.HexDump;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import mil.nga.tiff.FieldType;
import mil.nga.tiff.FileDirectory;
import mil.nga.tiff.FileDirectoryEntry;
import mil.nga.tiff.TIFFImage;
import mil.nga.tiff.TiffReader;
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

/**
 * The {@link TiffDumper} parses a TIFF file and prints contained tags.
 */
@SuppressWarnings("nls")
public final class TiffDumper {

    private static final Package TIFFDUMPER_PACKAGE = TiffDumper.class.getPackage();
    private static final String TIFFDUMPER_DEFAULT_VERSION = "0.0";
    private static final String TIFFDUMPER_DEFAULT_URL = "https://github.com/michaelknigge/tiffdumper";

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

        if (!this.quiet) {
            out.println("TIFF-Dumper " + this.getImplementationVersion() + " - " + this.getImplementationVendor());
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
        out.println("Directory #" + directoryNumber +
                " (" +  directory.size() + " bytes" +
                ", " +  directory.numEntries() + " entries)");
        out.println("----------------------------------------------------------------------");

        for (final FileDirectoryEntry entry : directory.getEntries()) {
            dumpFileDirectoryEntry(entry, out);
        }
    }

    private static void dumpFileDirectoryEntry(final FileDirectoryEntry entry, final PrintStream out) {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s", entry.getFieldTag()));
        sb.append(String.format("%-12s : ", entry.getFieldType()));
        out.print(sb.toString());

        if (entry.getFieldTag().isArray() || entry.getTypeCount() != 1) {
            dumpArray((List<Object>) entry.getValues(), entry.getFieldType(), out);
        } else {
            dumpSingleValue(entry.getValues(), entry.getFieldType(), out);
        }

    }

    private static void dumpArray(final List<Object> values, final FieldType type, final PrintStream out) {

        if (type == FieldType.RATIONAL || type == FieldType.SRATIONAL) {
            out.println("Tag contains " + values.size() + " entries (" + values.size() / 2 + " rationals)");

            int ix = 0;
            final Iterator<Object> iterator = values.iterator();
            final ArrayList<Long> longs = new ArrayList<>(2);

            longs.add(null); // create entry at index = 0
            longs.add(null); // create entry at index = 1

            while (iterator.hasNext()) {
                longs.set(0, (Long) iterator.next());
                longs.set(1, (Long) iterator.next());

                out.print(String.format("%7d: ", ++ix));
                dumpSingleValue(longs, type, out);
            }
        } else if (type == FieldType.UNDEFINED) {
            out.println("Tag contains " + values.size() + " bytes");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            for (final Object value : values) {
                baos.write((Short) value);
            }

            outputHexDump(baos.toByteArray(), out);

        } else {
            out.print("Tag contains " + values.size());
            out.println(values.size() > 1 ? " entries" : " entry");

            for (int ix = 0; ix < values.size(); ++ix) {
                out.print(String.format("%7d: ", ix + 1));
                dumpSingleValue(values.get(ix), type, out);
            }
        }
    }

    private static void dumpSingleValue(final Object value, final FieldType type, final PrintStream out) {

        switch (type) {
        case BYTE:
        case SBYTE:
            out.println(((Number) value).intValue());
            break;
        case ASCII:
            out.println(value.toString());
            break;
        case SHORT:
        case SSHORT:
            out.println(((Number) value).intValue());
            break;
        case LONG:
        case SLONG:
            out.println(((Number) value).longValue());
            break;
        case RATIONAL:
        case SRATIONAL:
            if (value instanceof List) {
                final List<Long> list = (List<Long>) value;
                final long fraction = list.get(0).longValue();
                final long denominator = list.get(1).longValue();
                final double result = (double) fraction / (double) denominator;

                out.println(String.format("%1$d / %2$d = %3$.4f", fraction, denominator, result));
            } else {
                out.println(((Number) value).longValue());
            }
            break;
        case UNDEFINED:
            out.println(((Number) value).shortValue());
            break;
        case FLOAT:
            out.println(((Number) value).floatValue());
            break;
        case DOUBLE:
            out.println(((Number) value).doubleValue());
            break;
        default:
            out.println("??? Unknown type " + type.toString());
        }
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

    private static void outputHexDump(final byte[] data, final PrintStream out) {
        if (data.length == 0) {
            return;
        }

        // Let's be lazy here.... Let all the work be done by Apache Commons... This is not really
        // a high-performance way to create the hex dump for us, but note that TIFF-Dumper is not
        // required to be a high-performance tool... So I guess it is "ok" here to be lazy...
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            HexDump.dump(data, 0, baos, 0);
        } catch (final IOException e) {
            // An IOException should never ever been thrown because we write to memory...
            return;
        }

        final byte[] hex = baos.toByteArray();
        IOUtils.closeQuietly(baos);

        final ByteArrayInputStream bais = new ByteArrayInputStream(hex);
        final InputStreamReader reader = new InputStreamReader(bais, Charset.defaultCharset());
        final LineIterator iter = new LineIterator(reader);

        while (iter.hasNext()) {
            out.println("      " + iter.next());
        }

        iter.close();
    }
}
