---
type: telemetry
title: Compilation Telemetry
description: Explains how BlueJ compile callbacks update compile counts, durations, intervals, errors, categories, and the session model.
tags: [telemetry, compile, bluej]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-7560cb6cdbae133536322c2c
    resource: repo://comp3607/src/main/java/bluej_extension/CompileTracker.java
  - id: openwiki-source-67cca62c271f5a4270bce0f2
    resource: repo://comp3607/src/test/java/bluej_extension/CompileTrackerTest.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Compilation Telemetry

## Callback lifecycle

`CompileTracker` implements BlueJ's `CompileListener`. `compileStarted` records the start timestamp, calculates the interval since the prior start, and appends a `COMPILE` telemetry event with status `STARTED`. Success and failure callbacks increment outcome counters, append file sets, and add completion events containing duration, file count, and error count.

Compile errors and warnings are stored separately. Warnings do not increment the error counter; each error record retains line number, message, file positions, and timestamp.

## Session population

`populateSession` copies compile totals and successful/failed counts into `SessionModel`, derives compile intervals and mean interval, adds categorized `ErrorEntry` records, and updates derived compile fields. Error messages are classified as missing symbol, type mismatch, syntax, uninitialized, missing return, or other.

The tracker preserves compile data only in the shared session model until population is called. The extension calls this method during shutdown so derived values are current before export or report generation.

## Tests and edge cases

Compile tests verify started/successful/failed event sequences, error categorization, warning exclusion, interval calculation, repeated population, and mixed compile outcomes using mocked `CompileEvent` instances.
