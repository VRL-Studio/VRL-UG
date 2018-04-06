#!/bin/bash

source ./PROJECT_PATH
echo -e ">> compiling project:"

CWD="$(pwd)"

#TMP_DIR="/Users/christianpoliwoda/tmp"

## position where to find VRL
VRL_DIR="/Users/christianpoliwoda/Apps/github/VRL/VRL"

## name of the header file that should be generated
## do NOT rename it
## (rename it only if you want temporary a file to look on
## which should not be used. Than you need also to comment out
## the line which copies the file into native UG)
#HEADER_NAME="TEST2-bindings_vrl_native.h"
HEADER_NAME="bindings_vrl_native.h"


cd VRL-UG

#ant clean
#ant compile
#ant jar

cd "$CWD"

echo -e ">> creating header file:"
javah -o $HEADER_NAME -classpath "VRL-UG/build/classes:$VRL_DIR/dist/VRL.jar" edu.gcsc.vrl.ug.UG 


echo -e ">> copying header file to \"$UG4/bindings/vrl/\" folder"
cp $HEADER_NAME $UG4/ugbase/bindings/vrl/

