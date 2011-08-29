#!/bin/bash

source ./PROJECT_PATH
echo -e ">> compiling project:"

CWD="$(pwd)"

cd VRL-UG

ant clean
ant jar

cd "$CWD"

echo -e ">> creating header file:"
javah -o bindings_vrl_native.h -classpath VRL-UG/build/classes edu.gcsc.vrl.ug.UG edu.gcsc.vrl.ug.MemoryManager
echo -e ">> copying header file to \"$UG4/bindings_vrl/\" folder"
cp bindings_vrl_native.h $UG4/ugbase/bindings_vrl/
