# ODF Toolkit Samples

This project collects samples on using the
[ODF Toolkit](https://github.com/tdf/odftoolkit), a library for reading,
creating and manipulating Open Document Format (ISO/IEC 26300 == ODF)
documents.

There are currently two modules: `samples` and `cli`. The `samples`
module contains example code that is supposed to be inspected, modified
and run from within an IDE. The `cli` module contains example code that
is ready to be run from the command line for quick testing and
experimentation with the toolkit's capabilities.

## Building and Running

To set up the project for experimentation from within Eclipse, run

    ./gradlew cleanEclipse eclipse

You can then import the projects into your Eclipse workspace and run
the classes that contain a `main`-method.

To execute the scripts in the `scripts` directory, build the project:

    ./gradlew clean createRuntime

### CLI examples

Printing a ODT file's content as plain text:

    ./scripts/print-plaintext samples/src/main/resources/main.odt
    ./scripts/print-plaintext samples/src/main/resources/letter.odt

Performing a mail merge, i.e. replace some `${variable}` content within
a document based on values in a CSV file and produce a number of output
files. The number of files produced equals the number of rows in the CSV
file:

    ./scripts/mailmerge samples/src/main/resources/letter.odt
        samples/src/main/resources/recipients.csv test.odt

## License

This project is licensed under the Apache License, Version 2.0.
