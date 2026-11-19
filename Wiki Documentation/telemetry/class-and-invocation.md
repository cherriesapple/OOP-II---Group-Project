---
type: telemetry
title: Class and Invocation Telemetry
description: Explains BlueJ package, class lifecycle, and program invocation event capture in the shared session model.
tags: [telemetry, bluej, lifecycle]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-1d505d6466b8f6cad7445102
    resource: repo://comp3607/src/main/java/bluej_extension/ClassTracker.java
  - id: openwiki-source-3ce5f90a408ecb1e371b9e3a
    resource: repo://comp3607/src/main/java/bluej_extension/InvocationTracker.java
  - id: openwiki-source-e1479614fb97260267fc4920
    resource: repo://comp3607/src/main/java/bluej_extension/PackageTracker.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Class and Invocation Telemetry

## Class lifecycle

`ClassTracker` implements BlueJ's `ClassListener`. Name changes, removals, and state changes append `ClassLifecycleEvent` records to `SessionModel.classLifecycle`. State changes also determine whether a class is new or modified and record compiled-attempt and waiting-to-compile information in the event description.

The tracker retains a list of known `BClass` instances for new-class detection. It records removal count separately and stores timestamps on every lifecycle event.

## Package lifecycle

`PackageTracker` implements `PackageListener`. Opening or closing a package resolves the project name and appends a package-created/opened or deleted/closed description to `SessionModel.events`. BlueJ extension exceptions are recorded as package retrieval errors instead of being swallowed.

## Invocation completion

`InvocationTracker` implements `InvocationFinishedListener`. Each finished run increments the execution count and appends an `EXECUTION_INITIATED` telemetry event with a status for normal, unknown, exception, or user-terminated exits.

## Testing

Focused JUnit tests verify tracker instantiation, lifecycle event storage, package open/close/error descriptions, and invocation exit classifications using mocked BlueJ events.
