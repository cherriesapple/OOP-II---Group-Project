---
type: telemetry
title: Telemetry Collection
description: Explains compile, package, class, invocation, and editor-activity event collection and their updates to SessionModel.
tags: [telemetry, listeners, bluej, activity]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-831d5a0d9d4c5b1f86edbe49
    resource: repo://comp3607/src/main/java/bluej_extension/ActivityTracker.java
  - id: openwiki-source-1d505d6466b8f6cad7445102
    resource: repo://comp3607/src/main/java/bluej_extension/ClassTracker.java
  - id: openwiki-source-7560cb6cdbae133536322c2c
    resource: repo://comp3607/src/main/java/bluej_extension/CompileTracker.java
  - id: openwiki-source-3ce5f90a408ecb1e371b9e3a
    resource: repo://comp3607/src/main/java/bluej_extension/InvocationTracker.java
  - id: openwiki-source-e1479614fb97260267fc4920
    resource: repo://comp3607/src/main/java/bluej_extension/PackageTracker.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Compile events

`CompileTracker` responds to compile start, error, warning, success, and failure callbacks. It records compile intervals, updates counters, and creates typed `COMPILE` telemetry events with status, duration, file count, and error count. At session finalization, `populateSession` copies totals and intervals, computes the mean interval and success rate, and categorizes compile errors into the derived error maps.

## Package, class, and invocation events

`PackageTracker` records package open/close events and package lookup errors as general telemetry events. `ClassTracker` records class name changes, removals, and state changes as timestamped class-lifecycle events. It also tracks removed classes and maintains a list used to distinguish newly observed classes.

`InvocationTracker` records normal, unknown, exception, and user-terminated executions as `EXECUTION_INITIATED` events and maintains an execution count.

## Editor activity

`ActivityListener` accepts a BlueJ `ExtensionEvent` but processes only `ClassEvent` instances. It obtains the class Java editor and starts or reuses `ActivityTracker`, while reporting package or project lookup failures to standard error.

`ActivityTracker` polls the active editor every 500 milliseconds and compares text length and caret location. A change counts as an edit signal in a three-minute bucket. Gaps of at least 180 seconds are accumulated as idle time rather than active time.

On stop, the tracker copies session duration, idle minutes, active state, edit-signal totals, and bucket data into `SessionModel`.

## Merge-conflict caveat

`ClassTracker` contains unresolved merge-conflict markers in the getter for deleted-class count. Like the entrypoint conflict, this affects source validity and must be resolved before collection behavior can be verified through a successful build.
