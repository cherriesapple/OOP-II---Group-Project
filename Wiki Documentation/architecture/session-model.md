---
type: architecture
title: Session Data Model
description: Defines the JSON session contract and its nested lifecycle, telemetry, derived, error, and activity structures.
tags: [data-model, session, telemetry]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-20a41b1c8faaf92d76bd4fe2
    resource: repo://comp3607/src/main/java/bluej_extension/JsonExporter.java
  - id: openwiki-source-973114dc067e86eac6fa6a93
    resource: repo://comp3607/src/main/java/bluej_extension/SessionModel.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Session Data Model

## Responsibility

`SessionModel` is the shared mutable record passed among the BlueJ listeners and trackers. Jackson property annotations give it a stable JSON shape independent of Java field names; `template.json` documents the same initial schema as an empty-session example.

## Session identity and counters

The top level contains schema and status fields, student and project metadata, session duration and activity state, compilation totals, class lifecycle, event records, and derived metrics. Session metadata and run counts live in `SessionData`, including the 180-second idle threshold and successful, failed, run, and exception totals.

## Telemetry and lifecycle structures

`TelemetryEvent` stores a type, timestamp, and heterogeneous data map for compile and other event signals. `ClassLifecycleEvent` stores class name, created/modified/removed state, and timestamp, while `ErrorEntry` adds category, message, line number, and timestamp to compilation error breakdowns.

## Derived metrics

`Derived` holds compile intervals and success rate, categorized errors, a class timeline, and editor activity. `ActivityTimeline` contains three-minute bucket settings and bucket records that describe edit signals and active seconds.

## JSON export

`JsonExporter` creates `src/files` when needed and writes pretty-printed session data to `src/files/Session_Data.json` with Jackson. Export failure propagates to the caller rather than being converted into a fallback file format.
