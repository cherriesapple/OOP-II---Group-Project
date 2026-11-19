---
type: architecture
title: BlueJ Extension Lifecycle
description: Describes the extension entrypoint, listener registration, shutdown responsibilities, and unresolved source conflicts affecting startup behavior.
tags: [bluej, lifecycle, telemetry, extension]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-0c29b572e8633720d6824f7b
    resource: repo://comp3607/manifest.txt
  - id: openwiki-source-92062e7cfa9a0992cc4b13b6
    resource: repo://comp3607/src/main/java/bluej_extension/ActivityListener.java
  - id: openwiki-source-7560cb6cdbae133536322c2c
    resource: repo://comp3607/src/main/java/bluej_extension/CompileTracker.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Entry point

`MyExtension` is the BlueJ extension entrypoint and is declared by the Maven JAR manifest as `bluej_extension.MyExtension`. It owns one shared `SessionModel` plus tracker instances for packages, classes, invocations, compilation, and activity.

The extension reports API compatibility by requiring a BlueJ extensions API major version of at least 3. It identifies itself as "Telemetry Extension" with version `1.2.3` and describes its purpose as collecting telemetry and generating a summary report.

## Startup

The intended `startup(BlueJ)` flow registers the compile, class, package, and invocation trackers on the corresponding BlueJ listeners. One branch of `MyExtension` also intends to register `ActivityListener` as an extension event listener so class-editor activity can start tracking. The source currently contains merge-conflict markers around tracker fields and startup registration, so no merged startup implementation can be compiled until those conflicts are resolved.

## Shutdown

The alternate lifecycle branch contains shutdown responsibilities for stopping the activity tracker, finalizing compilation data with `populateSession()`, marking the session `COMPLETE`, and setting the session end time. The resulting session state should be consumed by report or export code. Because this branch remains inside merge-conflict markers, it should be treated as intended behavior rather than verified runtime behavior.

## Listener behavior

`CompileTracker` receives compile start, error, warning, success, and failure callbacks and accumulates counters, intervals, errors, and per-outcome telemetry events. `ActivityListener` reacts only to `ClassEvent` instances, obtains the class Java editor, and starts or reuses `ActivityTracker`; editor lookup failures are reported to `stderr`.
