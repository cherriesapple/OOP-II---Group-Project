---
type: quickstart
title: Quickstart
description: Orients contributors to the BlueJ telemetry extension, its build and test workflow, and the repository wiki's main topics.
tags: [quickstart, build, bluej]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-a891e24bbc55d5ed22987216
    resource: repo://comp3607/pom.xml
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Quickstart

## Repository purpose

This project is a Java BlueJ extension named `Telemetry Extension`. It registers listeners for BlueJ compile, class, package, invocation, and editor events, stores collected values in a shared session model, exports JSON, and generates a four-page PDF report.

The Maven module is `comp3607`. It requires Java 21 and depends on the repository-local `comp3607/lib/bluej.jar` BlueJ API.

## Build and test

From `comp3607`, run:

- `mvn test` to compile and execute JUnit tests.
- `mvn package` to test and package the extension JAR.

The suite uses JUnit Jupiter and Mockito to test activity, compile, class, package, and invocation tracking.

## Documentation map

- Start with [Extension Lifecycle](architecture/extension-lifecycle.md) to follow startup, listener registration, and shutdown.
- Read [Session Data Model](architecture/session-model.md) before changing the exported JSON or report data contract.
- Use [Compilation Telemetry](telemetry/compilation.md), [Editor Activity Tracking](telemetry/editor-activity.md), and [Class and Invocation Telemetry](telemetry/class-and-invocation.md) for event capture behavior.
- Use [PDF Reports and Data Export](reporting/pdf-and-data-export.md) for output generation.
- Use [Build and Tests](development/build-and-tests.md) for Maven and verification details.
