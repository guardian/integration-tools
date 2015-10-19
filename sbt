#!/bin/bash

java -Xmx512M -XX:MaxPermSize=250m -jar `dirname $0`/sbt-launch.jar  "$@"
