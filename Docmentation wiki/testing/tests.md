---
type: testing
title: Testing and Build
description: Documents the Maven/JUnit setup, focused tracker tests, and repository build constraints.
tags: [testing, maven, junit, build]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-a891e24bbc55d5ed22987216
    resource: repo://comp3607/pom.xml
  - id: openwiki-source-1d505d6466b8f6cad7445102
    resource: repo://comp3607/src/main/java/bluej_extension/ClassTracker.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
  - id: openwiki-source-0ee339aab5c0a4a60088f6eb
    resource: repo://comp3607/src/test/java/bluej_extension/ClassTrackerTest.java
  - id: openwiki-source-5d0d3d93c2489bf2da558e33
    resource: repo://comp3607/src/test/java/bluej_extension/InvocationTrackerTest.java
  - id: openwiki-source-aeecdb5a3ff5a7b7f9149683
    resource: repo://comp3607/src/test/java/bluej_extension/PackageTrackerTest.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Build setup

The Maven project is in `comp3607`. It uses Java 21, JUnit Jupiter 5.11.4, and Maven Surefire 3.5.4. Runtime dependencies include the local BlueJ 5.0 JAR, PDFBox, JFreeChart, iText, Jackson, JavaFX, and json-simple.

The Maven POM is the source of build and test configuration. No Maven wrapper is present at the repository root or project level.

## Unit coverage

The existing tests cover direct tracker behavior that can be exercised with a `SessionModel`:

- `PackageTrackerTest` verifies instantiation and package open, close, error, and generic telemetry event storage.
- `InvocationTrackerTest` verifies all four execution exit categories and execution counting.
- `ClassTrackerTest` verifies lifecycle event storage and class-state description formatting.

These tests use JUnit 5 assertions and inspect counters or records in the shared session model.

## Current verification state

An attempt to run `mvn test` in this environment failed because `mvn` is not available on `PATH`. Therefore this repository does not have a current successful test result from this session.

Source validity is also a prerequisite: merge-conflict markers remain in `MyExtension.java` and `ClassTracker.java`, and a Maven build cannot be considered successful while those markers are unresolved.
