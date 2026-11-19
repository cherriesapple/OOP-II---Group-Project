---
type: quickstart
title: Quickstart
description: Provides the repository entry point, Maven prerequisites, build/test commands, current blockers, and navigation to the system documentation.
tags: [quickstart, maven, bluej, setup]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-0c29b572e8633720d6824f7b
    resource: repo://comp3607/manifest.txt
  - id: openwiki-source-a891e24bbc55d5ed22987216
    resource: repo://comp3607/pom.xml
  - id: openwiki-source-1d505d6466b8f6cad7445102
    resource: repo://comp3607/src/main/java/bluej_extension/ClassTracker.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Repository layout

The Maven project is `comp3607/`. Production Java sources are under `comp3607/src/main/java/bluej_extension`, JUnit tests are under `comp3607/src/test/java/bluej_extension`, and the BlueJ JAR used by the POM is at `comp3607/lib/bluej.jar`.

The project declares `bluej_extension.MyExtension` as the JAR main/extension entry point.

## Prerequisites

Use a Java 21 JDK and Maven with access to the dependencies declared in `comp3607/pom.xml`. The project also depends on the checked-in BlueJ JAR rather than downloading a BlueJ artifact.

## Build and test

From the repository root:

```powershell
cd comp3607
mvn test
```

Package the extension with:

```powershell
mvn package
```

Before expecting a successful build or test run, reconcile unresolved merge-conflict markers in `MyExtension.java` and `ClassTracker.java`. In this environment, `mvn` was not available on `PATH`, so neither build nor test verification could be executed.

## Where to read next

- Read the [System Overview](architecture/overview.md) for subsystem boundaries and the intended data flow.
- Read [BlueJ Extension Lifecycle](architecture/extension-lifecycle.md) for startup and shutdown behavior.
<!-- openwiki: broken internal link [../telemetry/collection.md] file "../telemetry/collection.md" does not exist. Fix the href or restore the target, then delete this comment. -->
- Read [Telemetry Collection](../telemetry/collection.md) for listener and tracker responsibilities.
<!-- openwiki: broken internal link [../data/session-model.md] file "../data/session-model.md" does not exist. Fix the href or restore the target, then delete this comment. -->
- Read [Session Data Model](../data/session-model.md) for serialized state.
<!-- openwiki: broken internal link [../reporting/report-generation.md] file "../reporting/report-generation.md" does not exist. Fix the href or restore the target, then delete this comment. -->
- Read [Report Generation](../reporting/report-generation.md) for PDF and JSON outputs.
- Read [Testing and Build](testing/tests.md) for Maven configuration and JUnit coverage.
