#!/usr/bin/env bash
# reading one object
wrk -t10 -c10 -d300s --latency http://localhost:50503/abcd
