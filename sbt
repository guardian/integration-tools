#!/bin/bash

java -Xmx512M -XX:MaxPermSize=250m -jar `dirname $0`/sbt-launch-0.11.0.jar  "$@"
