---
type: reporting
title: PDF Reports and Data Export
description: Documents JSON export, report aggregation, chart rendering, and the four-page PDF composite layout.
tags: [reporting, pdf, export]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-c9519965181826832208bd45
    resource: repo://comp3607/src/main/java/bluej_extension/ChartElement.java
  - id: openwiki-source-20a41b1c8faaf92d76bd4fe2
    resource: repo://comp3607/src/main/java/bluej_extension/JsonExporter.java
  - id: openwiki-source-e3c98e7dc752421d6e163cd1
    resource: repo://comp3607/src/main/java/bluej_extension/PageElement.java
  - id: openwiki-source-0b861be26aa7ad9a89c37741
    resource: repo://comp3607/src/main/java/bluej_extension/PDFReport.java
  - id: openwiki-source-af9d3cf1e619e8f48243561b
    resource: repo://comp3607/src/main/java/bluej_extension/ReportData.java
  - id: openwiki-source-bd8e3891c4c41e3b7acadd7c
    resource: repo://comp3607/src/main/java/bluej_extension/ReportDataAggregator.java
  - id: openwiki-source-9187967b4ee28e2e52f9b925
    resource: repo://comp3607/src/main/java/bluej_extension/ReportElement.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# PDF Reports and Data Export

## Data export

`JsonExporter` creates a `src/files` directory if absent and writes the complete `SessionModel` as pretty JSON to `Session_Data.json`. This export preserves the model's nested session, event, derived, and activity structure.

## Report aggregation

`ReportDataAggregator` converts the nested session model into a flatter `ReportData` object for the PDF package. It rounds session and idle durations, derives active minutes, copies activity buckets and error totals into ordered maps, and flattens class lifecycle events into report timeline rows.

## Page construction

`PDFReport.generateReport` creates a PDF and renders four physical pages: overview, user activity, compilation outcomes and errors, and project timeline. Each page is a `PageElement` composite containing text, image, or table elements.

`ReportElement` defines the rendering and height contract. `PageElement` walks children in insertion order and advances the PDF y-coordinate by each element's height plus fixed spacing, allowing text, chart, and table implementations to share one page renderer.

## Chart rendering

`ChartElement` uses JFreeChart to generate 500 by 300 PNG byte arrays for activity, compile outcomes, and categorized errors. PDF activity and error charts fall back to zero or "No errors" datasets when no measurements exist, while compile outcomes come directly from successful and failed session counters.
