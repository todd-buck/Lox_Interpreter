# KLox Interpreter

By Todd Buck, Mithul Nallaka, and Caroline Lewis.

## Description

An implementation of the Lox programming language as outlined by Robert Nystorm in Crafting Interpreters. This implementation was written using the Kotlin programming language.

## Project Layout

### Main Folder

This is where you will find the Kotlin code for the interpreter. To execute the interpreter, use the Lox.kt file.

### Testing Folder

This is where you will find a testing suite for the interpreter. It contains code that will execute both unit tests and program tests. In the root directory of the Testing folder, you will find the unit tests (ClassesTests.kt, ScannerTests.kt, etc). 

If you would like to view the code segments for the unit tests, view the .txt file with the same name under the TestFiles folder (classesTests.txt, functionTests.txt, etc). 

All JUnit testing outputs have been collected into an HTML document in the TestOutput folder (Test Results - ScannerTests.html, Test Results - ClassesTests.html, etc). All tests from the .txt files can be verified by opening these html files. To view the results of testing, open these html files in your browser.

If a test has passed, it will say "passed" in green on the right side of the page. If it has failed, it will open a drop down with error information.

The InputFiles folder is the source of file inputs for the ScannerTests, and the ProgramTesting folder is a working folder of programattic testing.
