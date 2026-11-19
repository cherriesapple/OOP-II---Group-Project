---
type: reporting
title: Report Generation
description: Documents report aggregation, four-page PDF composition, chart rendering, JSON export, and implementation constraints.
tags: [reporting, pdf, charts, export]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T02:57:42.280Z
sources:
  - id: openwiki-source-c9519965181826832208bd45
    resource: repo://comp3607/src/main/java/bluej_extension/ChartElement.java
  - id: openwiki-source-20a41b1c8faaf92d76bd4fe2
    resource: repo://comp3607/src/main/java/bluej_extension/JsonExporter.java
  - id: openwiki-source-c98920423ca03671681e171b
    resource: repo://comp3607/src/main/java/bluej_extension/MyExtension.java
  - id: openwiki-source-e3c98e7dc752421d6e163cd1
    resource: repo://comp3607/src/main/java/bluej_extension/PageElement.java
  - id: openwiki-source-0b861be26aa7ad9a89c37741
    resource: repo://comp3607/src/main/java/bluej_extension/PDFReport.java
  - id: openwiki-source-af9d3cf1e619e8f48243561b
    resource: repo://comp3607/src/main/java/bluej_extension/ReportData.java
  - id: openwiki-source-bd8e3891c4c41e3b7acadd7c
    resource: repo://comp3607/src/main/java/bluej_extension/ReportDataAggregator.java
generated: { by: "copilot", at: "2026-11-19T02:57:42.280Z" }
---

## Aggregation boundary

`ReportDataAggregator` converts a populated `SessionModel` into a flatter `ReportData` used by report builders. It rounds session and idle minutes, derives active minutes, copies compilation counters, preserves activity buckets as an ordered map, copies categorized error totals, and flattens class lifecycle events into timeline records.

`ReportData` keeps the report independent of the nested session model, so PDF page builders deal with primitive values, maps, and flat timeline entries.

## PDF structure

`PDFReport.generateReport` creates a PDF and renders four A4/Letter-size pages: an overview with session totals, an activity area chart, compilation outcomes and error-frequency charts, and a class-lifecycle table.

Pages are composed through a composite `PageElement` tree containing text, image, and table implementations. Children render in insertion order with fixed spacing. The PDF timeline table is intentionally limited to the first 15 project events.

## Charts and export constraints

`ChartElement` creates 500 by 300 pixel PNG charts with JFreeChart. It provides fallbacks for an empty activity timeline and empty error totals so chart generation can still produce an image. Activity uses three-minute bucket indexes, compilation outcomes use successful and failed counts, and error frequency uses categorized totals.

`JsonExporter` is a separate output path that writes pretty-printed `SessionModel` JSON to `src/files/Session_Data.json`.

No source call in the reviewed entrypoint or lifecycle currently invokes `PDFReport.generateReport` or `JsonExporter.export`, so report/export behavior is implemented but may remain disconnected until the unresolved merge conflict in `MyExtension` is resolved.
