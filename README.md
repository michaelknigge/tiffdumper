# tiffdumper [![Build Status](https://travis-ci.org/michaelknigge/tiffdumper.svg?branch=master)](https://travis-ci.org/michaelknigge/tiffdumper) [![codecov.io](https://codecov.io/github/michaelknigge/tiffdumper/coverage.svg?branch=master)](https://codecov.io/github/michaelknigge/tiffdumper?branch=master) [![Coverity Status](https://scan.coverity.com/projects/17279/badge.svg)](https://scan.coverity.com/projects/17279) [![Download](https://api.bintray.com/packages/michaelknigge/maven/tiffdumper/images/download.svg) ](https://bintray.com/michaelknigge/maven/tiffdumper/_latestVersion)

Java command line tool (and library as well) for dumping [TIFF](https://en.wikipedia.org/wiki/TIFF) images. [TIFF Java](https://github.com/ngageoint/tiff-java) is used for parsing the TIFF image.

# Dependencies
[Apache Commons-IO](http://commons.apache.org/proper/commons-io/) is required in any case. If tiffdumper is used as a command line tool then [Apache Commons-CLI](http://commons.apache.org/proper/commons-cli/) is also required.

# Usage (command line tool)
For your convenience tiffdumper comes in two flavours - as a regular JAR and as a single runnable JAR (called a "fat JAR") that contains all required dependencies. This runnable JAR is very handy if tiffdumper is used as a command line tool because you do not have to specify the Java CLASSPATH with all the required dependencies yourself.

Just invoke the tiffdumper from a command line without any additional parameter to get a list of all available options:

```
$ java -jar tiffdumper-1.0-all.jar -h
usage: tiffdumper [OPTION]... [FILE]

TIFF-Dumper reads a TIFF file and dumps every tag.

 -f,--file <arg>   output file for the dump
 -h,--help         shows this help
 -q,--quiet        do not show the PCL-Dumper header
 -v,--verbose      show more details

Please report issues at https://github.com/michaelknigge/tiffdumper/issues
```

# Usage (Java library)
TODO

# Examples
TODO
Dump of a PCL file without offsets and without details:

```
$ java -jar tiffdumper-0.2-all.jar -q myfile.tif
TIFF-Dumper 0.2 - https://github.com/michaelknigge/tiffdumper
------------------------------------------------------------------------------
 
Directories : 1
Header size : 326
 
Directory #1 (318 bytes, 26 entries)
----------------------------------------------------------------------
NewSubfileType                LONG         : 0
ImageWidth                    LONG         : 160
ImageLength                   LONG         : 160
BitsPerSample                 SHORT        : Tag contains 3 entries
      1: 8
      2: 8
      3: 8
Compression                   SHORT        : 6
PhotometricInterpretation     SHORT        : 6
StripOffsets                  LONG         : Tag contains 1 entry
      1: 610
SamplesPerPixel               SHORT        : 3
RowsPerStrip                  LONG         : 160
StripByteCounts               LONG         : Tag contains 1 entry
      1: 3447
XResolution                   RATIONAL     : 200 / 2 = 100,0000
YResolution                   RATIONAL     : 200 / 2 = 100,0000
PlanarConfiguration           SHORT        : 1
ResolutionUnit                SHORT        : 2
Software                      ASCII        : Tag contains 1 entry
      1: HP IL v1.1
JPEGProc                      SHORT        : 1
JPEGInterchangeFormat         LONG         : 8
JPEGInterchangeFormatLength   LONG         : 4608
JPEGRestartInterval           SHORT        : 0
JPEGQTables                   LONG         : Tag contains 3 entries
      1: 34
      2: 103
      3: 103
JPEGDCTables                  LONG         : Tag contains 3 entries
      1: 172
      2: 205
      3: 205
JPEGACTables                  LONG         : Tag contains 3 entries
      1: 238
      2: 421
      3: 421
YCbCrCoefficients             RATIONAL     : Tag contains 6 entries (3 rationals)
      1: 2990 / 10000 = 0,2990
      2: 5870 / 10000 = 0,5870
      3: 1140 / 10000 = 0,1140
YCbCrSubSampling              SHORT        : Tag contains 2 entries
      1: 2
      2: 2
YCbCrPositioning              SHORT        : 1
ReferenceBlackWhite           LONG         : Tag contains 6 entries
      1: 0
      2: 255
      3: 128
      4: 255
      5: 128
      6: 255
```
