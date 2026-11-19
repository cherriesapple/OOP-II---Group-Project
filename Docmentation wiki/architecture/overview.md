---
type: architecture
title: System Overview
description: Describes the BlueJ telemetry extension, its session data pipeline, and the reporting and export boundaries.
tags: [architecture, bluej, telemetry, reporting]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-20a41b1c8faaf92d76bd4fe2
    resource: repo://comp3607/src/main/java/bluej_extension/JsonExporter.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
  - id: openwiki-source-0b861be26aa7ad9a89c37741
    resource: repo://comp3607/src/main/java/bluej_extension/PDFReport.java
  - id: openwiki-source-bd8e3891c4c41e3b7acadd7c
    resource: repo://comp3607/src/main/java/bluej_extension/ReportDataAggregator.java
  - id: openwiki-source-973114dc067e86eac6fa6a93
    resource: repo://comp3607/src/main/java/bluej_extension/SessionModel.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Responsibility

This repository contains a BlueJ extension that observes development activity and represents the resulting session in a structured data model. The system is designed to collect compile, package, class, invocation, and editor-activity signals, derive summary metrics, and turn the finished session into PDF or JSON output.

The central boundary is `SessionModel`. Trackers mutate it as BlueJ callbacks occur, and reporting code consumes it after the session has been populated.

## Runtime flow

`MyExtension` is the BlueJ entry point. At startup, it is intended to register listener implementations with the BlueJ compile, class, package, invocation, and extension-event APIs. The listeners convert external events into state in the shared `SessionModel`.

At shutdown, the intended lifecycle is to stop activity sampling, finalize compile and session metrics, mark the session complete, and make the populated model available for report generation or JSON export.

## Major subsystems

- **Event collection:** compile callbacks, package/class/invocation listeners, and class-editor events update the session and its derived data.
- **Session state:** `SessionModel` defines serialized metadata, counters, lifecycle events, telemetry events, error details, and activity buckets.
- **Report aggregation:** `ReportDataAggregator` flattens selected session fields into a PDF-oriented `ReportData`.
- **Report rendering:** `PDFReport` builds a composite four-page report and uses JFreeChart and the PDFBox rendering stack for activity, compilation, and error visuals.
- **JSON export:** `JsonExporter` writes the populated model to `src/files/Session_Data.json`.

## Current integration state

`MyExtension.java` contains unresolved merge-conflict markers in both its tracker fields/startup registration and its shutdown finalization. This prevents the runtime architecture from being considered complete until one branch is reconciled. Outside that entrypoint, the model, trackers, aggregation, and export/report classes show separate implementation boundaries, but a successful end-to-end invocation should not be assumed without resolving and building the source.
