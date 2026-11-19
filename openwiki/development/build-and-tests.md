---
type: development
title: Build and Tests
description: Describes Maven configuration, Java and BlueJ dependencies, test coverage, packaging, and verification commands.
tags: [build, maven, testing]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-0c29b572e8633720d6824f7b
    resource: repo://comp3607/manifest.txt
  - id: openwiki-source-a891e24bbc55d5ed22987216
    resource: repo://comp3607/pom.xml
  - id: openwiki-source-7cc5dcf39e8c11b1366055a1
    resource: repo://comp3607/src/test/java/bluej_extension/ActivityTrackerTest.java
  - id: openwiki-source-0ee339aab5c0a4a60088f6eb
    resource: repo://comp3607/src/test/java/bluej_extension/ClassTrackerTest.java
  - id: openwiki-source-67cca62c271f5a4270bce0f2
    resource: repo://comp3607/src/test/java/bluej_extension/CompileTrackerTest.java
  - id: openwiki-source-5d0d3d93c2489bf2da558e33
    resource: repo://comp3607/src/test/java/bluej_extension/InvocationTrackerTest.java
  - id: openwiki-source-aeecdb5a3ff5a7b7f9149683
    resource: repo://comp3607/src/test/java/bluej_extension/PackageTrackerTest.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Build and Tests

## Build setup

The Maven module is under `comp3607` and targets Java 21. It combines PDFBox, JFreeChart, iText layout, Jackson, BlueJ APIs, JavaFX controls, JUnit Jupiter, and Mockito; BlueJ is resolved from `lib/bluej.jar` because it is a system-scoped dependency.

Maven Compiler Plugin and Surefire run compilation and JUnit tests. Maven JAR Plugin uses `manifest.txt` for packaging metadata, whose main class identifies `bluej_extension.MyExtension`.

## Test coverage

The suite covers activity polling and idle buckets, compile success and failure state, class lifecycle events, package tracking, and invocation tracking. Tests use Mockito for BlueJ event and editor interfaces and JUnit assertions for session-model effects.

## Verification

Run `mvn test` from `comp3607` to execute the unit suite. Run `mvn package` to compile, test, and produce the configured JAR. The tests require Maven dependencies and the repository-local BlueJ jar to resolve.
