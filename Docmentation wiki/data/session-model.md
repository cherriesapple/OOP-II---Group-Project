---
type: data-model
title: Session Data Model
description: Documents the structured session state, serialized JSON schema, derived metrics, and tracker-owned fields.
tags: [session, json, telemetry, data-model]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-831d5a0d9d4c5b1f86edbe49
    resource: repo://comp3607/src/main/java/bluej_extension/ActivityTracker.java
  - id: openwiki-source-7560cb6cdbae133536322c2c
    resource: repo://comp3607/src/main/java/bluej_extension/CompileTracker.java
  - id: openwiki-source-20a41b1c8faaf92d76bd4fe2
    resource: repo://comp3607/src/main/java/bluej_extension/JsonExporter.java
  - id: openwiki-source-973114dc067e86eac6fa6a93
    resource: repo://comp3607/src/main/java/bluej_extension/SessionModel.java
  - id: openwiki-source-091ee7c78f3b56eac0a5b13d
    resource: repo://comp3607/src/main/java/bluej_extension/template.json
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Schema ownership

`SessionModel` is the central mutable representation of a telemetry session. Jackson property names and ordering define the JSON shape, while `template.json` provides the initial schema values.

Top-level fields cover schema/status metadata, user identity, session duration, active/idle state, compilation totals, nested session details, class lifecycle records, event records, and derived metrics.

## Initialized and populated fields

The model initializes status as `"IN PROGRESS"`, an activity state of `"yes"`, idle thresholds and counters at zero, and empty lifecycle/event/derived collections. Several values such as user ID, session ID, project name, and timestamps remain null until code populates them.

`CompileTracker` owns compilation counters, compile intervals, error breakdown, and success-rate derived values. `ActivityTracker` owns active/idle minutes and three-minute edit-activity buckets. Class, package, and invocation trackers are represented as intended/implemented producers of the lifecycle and event collections.

## Nested structures

`SessionData` contains session/project identity, start/end timestamps, the idle threshold, successful/failed compilations, run count, and exception count. `ClassLifecycleEvent` and `TelemetryEvent` preserve class lifecycle changes and typed events with timestamps and payload data.

The `Derived` structure holds compile intervals and mean, success rate, categorized errors, class timeline, and activity timeline. The activity timeline contains ordered buckets with edit signals and active seconds.

## JSON export

`JsonExporter` creates `src/files` if needed, serializes `SessionModel` with pretty-print JSON, and writes `Session_Data.json`. The implementation provides file-based export; it does not add database persistence or a separate storage service.
