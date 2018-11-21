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

/**
 * The {@link TiffDumperBuilder} is used to build a {@link TiffDumper}.
 */
public final class TiffDumperBuilder {

    private boolean quiet = false;
    private boolean verbose = false;

    /**
     * Constructor of the {@link TiffDumperBuilder}.
     */
    public TiffDumperBuilder() {
    }

    /**
     * Sets the <i>quiet</i> switch. If <i>quiet</i> is set to true, the output {@link TiffDumper} will
     * not contain the header line. The flag is initially set to <b>false</b>.
     *
     * @param value   the value of the <i>quiet</i> switch.
     *
     * @return the {@link TiffDumperBuilder}.
     */
    public TiffDumperBuilder quiet(final boolean value) {
        quiet = value;
        return this;
    }

    /**
     * Sets the <i>verbose</i> switch. If <i>verbose</i> is set to true, the output {@link TiffDumper} will
     * contain more details of several commands. The flag is initially set to <b>false</b>.
     *
     * @param value   the value of the <i>verbose</i> switch.
     *
     * @return the {@link TiffDumperBuilder}.
     */
    public TiffDumperBuilder verbose(final boolean value) {
        verbose = value;
        return this;
    }

    /**
     * Builds the {@link TiffDumper}.
     *
     * @return the {@link TiffDumper}.
     */
    public TiffDumper build() {
        return new TiffDumper(quiet, verbose);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());

        sb.append(":");
        sb.append("quiet=");
        sb.append(quiet);

        sb.append(",");
        sb.append("verbose=");
        sb.append(verbose);

        return sb.toString();
    }
}
