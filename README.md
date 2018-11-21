# tiffdumper [![Build Status](https://travis-ci.org/michaelknigge/tiffdumper.svg?branch=master)](https://travis-ci.org/michaelknigge/tiffdumper) [![codecov.io](https://codecov.io/github/michaelknigge/tiffdumper/coverage.svg?branch=master)](https://codecov.io/github/michaelknigge/tiffdumper?branch=master) [![Coverity Status](https://scan.coverity.com/projects/11868/badge.svg)](https://scan.coverity.com/projects/11868) [![Download](https://api.bintray.com/packages/michaelknigge/maven/tiffdumper/images/download.svg) ](https://bintray.com/michaelknigge/maven/tiffdumper/_latestVersion)

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
$ java -jar tiffdumper-1.0-all.jar -q myfile.tif
TODO
```
