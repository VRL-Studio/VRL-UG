#!/bin/bash

source ./PROJECT_PATH

echo -e ">> creating header file: VRL-UG4.h"
javah -o VRL-UG4.h -classpath VRL-UG4/build/classes edu.gcsc.vrl.ug4.UG4
echo -e ">> copying header file \"VRL-UG4.h\" to \"$UG4/bindings_vrl/\" folder"
cp VRL-UG4.h $UG4/ugbase/bindings_vrl/
