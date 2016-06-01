#!/bin/sh

rm -r ./build
gradle clean assemble deploy

set Pathname="build\output"
pushd build/output
java -jar jersey-guice-api-gradle-0.1.jar
popd
popd

