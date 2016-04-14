#!/bin/bash -e
THIS_DIR="$(basename $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"
ARCHIVE=${THIS_DIR}/${THIS_DIR}.zip

echo "Executing tests and creating archive ${ARCHIVE}"

mvn -q test
mvn -q clean
pushd .. > /dev/null
jar cf ${ARCHIVE} \
       ${THIS_DIR}/pom.xml \
       ${THIS_DIR}/src \
       ${THIS_DIR}/coding-rules \
       ${THIS_DIR}/package.sh \
       ${THIS_DIR}/run-ws.sh \
       ${THIS_DIR}/assignment.md \
       ${THIS_DIR}/README.md \
       ${THIS_DIR}/.gitignore \
       ${THIS_DIR}/.editorconfig

popd > /dev/null
