---
type: architecture
title: Extension Lifecycle
description: Describes how the BlueJ extension initializes telemetry listeners, shares session state, and finalizes the session on shutdown.
tags: [architecture, bluej, lifecycle]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-7560cb6cdbae133536322c2c
    resource: repo://comp3607/src/main/java/bluej_extension/CompileTracker.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
  - id: openwiki-source-973114dc067e86eac6fa6a93
    resource: repo://comp3607/src/main/java/bluej_extension/SessionModel.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Extension Lifecycle

## Responsibility and ownership

`MyExtension` is the BlueJ extension entrypoint. It constructs one shared `SessionModel` and passes it to the package, class, invocation, compile, and activity trackers, so all telemetry sources populate the same session state.

During `startup`, the extension registers its trackers with BlueJ compile, class, package, invocation-finished, and extension-event APIs. This registration is the bridge between BlueJ IDE operations and the extension's in-process telemetry model.

## Startup and shutdown

Startup only registers listeners; collection begins as BlueJ invokes those listeners. The activity listener opens an editor tracker when a class event supplies an editable class, while the compile tracker receives compile lifecycle callbacks.

On `terminate`, the extension stops the activity tracker, asks the compile tracker to populate derived compile fields, marks the session complete, records the shutdown time, and prints a termination message. Shutdown therefore consolidates derived state before the session model is consumed or exported.

## Shared session state

The shared `SessionModel` owns the schema version, completion status, timing, compilation counters, class lifecycle, telemetry events, derived compile data, and editor activity buckets. Trackers mutate this model and do not need to coordinate through separate persistent stores.
