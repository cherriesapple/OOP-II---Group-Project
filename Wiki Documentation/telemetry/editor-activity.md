---
type: telemetry
title: Editor Activity Tracking
description: Documents editor polling, edit-signal detection, idle detection, and three-minute activity buckets.
tags: [telemetry, editor, activity]
verified:
  - by: openwiki/0.9.0
    at: 2026-11-19T04:33:22.309Z
sources:
  - id: openwiki-source-92062e7cfa9a0992cc4b13b6
    resource: repo://comp3607/src/main/java/bluej_extension/ActivityListener.java
  - id: openwiki-source-831d5a0d9d4c5b1f86edbe49
    resource: repo://comp3607/src/main/java/bluej_extension/ActivityTracker.java
  - id: openwiki-source-7cc5dcf39e8c11b1366055a1
    resource: repo://comp3607/src/test/java/bluej_extension/ActivityTrackerTest.java
generated: { by: "copilot", at: "2026-11-19T04:33:22.309Z" }
---

# Editor Activity Tracking

## Editor activation

`ActivityListener` accepts BlueJ extension events, filters for `ClassEvent`, obtains the class Java editor, and asks `ActivityTracker` to begin tracking it. Starting the same editor again is idempotent; switching editors stops the existing JavaFX polling timeline and starts a fresh session interval.

## Polling and edit signals

`ActivityTracker` polls every 500 ms. It considers text length or caret location changes to be edit signals and records the elapsed JavaFX timeline time for active-bucket calculations. If polling encounters an editor exception, it stops tracking.

## Idle and active time

Gaps of at least 180 seconds are idle time. Gaps below the threshold contribute active seconds to the bucket containing the gap start. When tracking stops, the tracker writes total session minutes, idle minutes, active status, edit count, bucket size, and buckets into `SessionModel`.

## Bucketing

Buckets cover three-minute intervals. Each bucket stores its index, ISO timestamp start, edit-signal count, and active seconds. On stop, the tracker ensures the current bucket exists even if no further edit occurred.

## Tests

`ActivityTrackerTest` verifies leading and inter-edit idle thresholds, correct bucket placement, active-second accumulation, stop-state fields, and editor exceptions.
