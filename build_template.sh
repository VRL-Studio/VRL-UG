#!/bin/sh
##########################################################################################
# This script is supposed to facilitate the compilation of the VRL-UG-API plugin.        #
# It will compile UG in an existing build folder which is supposed to already be         #
# configured to produce the vrl target. The following setup is recommended:              #
#                                                                                        #
# -- Info: TARGET:            vrl                                                        #
# -- Info: DIM:               ALL                                                        #
# -- Info: CPU:               1                                                          #
# -- Info: PRECISION:         double                                                     #
# -- Info: STATIC_BUILD:      OFF                                                        #
# -- Info: DEBUG:             OFF                                                        #
# -- Info: DEBUG_LOGS:        OFF                                                        #
# -- Info: PARALLEL:          OFF                                                        #
# -- Info: PCL_DEBUG_BARRIER: OFF                                                        #
# -- Info: PROFILER:          None                                                       #
# -- Info: PROFILE_PCL:       OFF                                                        #
# -- Info: CPU_FREQ:          OFF                                                        #
# -- Info: PROFILE_BRIDGE:    OFF                                                        #
# -- Info: LAPACK:            OFF                                                        #
# -- Info: BLAS:              OFF                                                        #
# -- Info: INTERNAL_BOOST:    ON                                                         #
# -- Info: EMBEDDED_PLUGINS   ON                                                         #
# -- Info: COMPILE_INFO       ON                                                         #
# -- Info: USE_LUA2C          OFF                                                        #
#                                                                                        #
# The built library will then be zipped and copied to the VRL-UG project which is        #
# subsequently built using ant.                                                          #
# Finally, the VRL-UG jar is copied to the VRL plugin-updates folder and VRL-Studio is   #
# called in order to evoke compilation of the VRL-UG-API plugin.                         #
#                                                                                        #
# IMPORTANT NOTE:                                                                        #
# If you want to use this script, you will first have to personalize the settings below  #
# according to your system and directory setup. Please do so by creating a new file      #
# "build.sh" with the appropriate changes and then add this file to your git exclude     #
# file located at .git/info/exclude.                                                     #
# Please do not commit any personalized version of this template to the repository!      #
#                                                                                        #
# author: mbreit                                                                         #
# date:   20-02-2017                                                                     #
##########################################################################################


## SETTINGS ##############################################################################
# VRL-UG java project location
VRL_UG_JAVA_PROJECT_FOLDER=/some/path/to/VRL-UG/VRL-UG

# position of ug build folder 
UG_BUILD_FOLDER=/some/path/to/ug4/build_vrl

# VRL home folder
VRL_DIR="${HOME}/.vrl/0.4.3/default"

# path to the library produced by ug build
UG_LIB_FOLDER=/some/path/to/ug4/lib

#ug library name
UG_LIB_NAME=libug4.dylib

# how many threads compile ug
THREADS=1

# Which OS do we operate on?
# on windows 64bit use   windows/x64
# on windows 32bit use   windows/x86
# on windows 64bit use   linux/x64
# on windows 32bit use   linux/x86
# on mac           use   osx
VRL_UG_JAVA_OS_SYSTEM_FOLDER=osx

# VRL-Studio run script
VRL_STUDIO_OPEN_SCRIPT=/some/path/to/VRL-Studio/VRL-Studio/dist-final/osx/VRL-Studio.app/Contents/MacOS/run.sh
##########################################################################################



## now build the damned thing ##

# build ug vor vrl #
echo "cd $UG_BUILD_FOLDER"
cd $UG_BUILD_FOLDER
# maybe, for the future, execute cmake with the required parameters
echo "make -j$THREADS"
make -j$THREADS 


# zip and move ug library to vrl-ug folder #
echo "cd ${UG_LIB_FOLDER}"
cd ${UG_LIB_FOLDER}
echo "zip -r natives.zip ${UG_LIB_NAME}"
zip -r natives.zip "${UG_LIB_NAME}"

nativesFolder="${VRL_UG_JAVA_PROJECT_FOLDER}/src/eu/mihosoft/vrl/plugin/content/natives/${VRL_UG_JAVA_OS_SYSTEM_FOLDER}"
if [ ! -d $nativesFolder ] ; then
    mkdir -p $nativesFolder
fi
echo "mv natives.zip ${nativesFolder}/natives.zip"
mv natives.zip ${nativesFolder}/natives.zip


# build VRL-UG using ant #
echo "cd $VRL_UG_JAVA_PROJECT_FOLDER"
cd $VRL_UG_JAVA_PROJECT_FOLDER

echo "ant clean"
ant clean

echo "ant compile"
ant compile

echo "ant jar"
ant jar


# copy the .jar file to vrl plugin updates and open VRL-Studio #
echo "cp ${VRL_UG_JAVA_PROJECT_FOLDER}/dist/VRL-UG.jar ${VRL_DIR}/plugin-updates"
cp ${VRL_UG_JAVA_PROJECT_FOLDER}/dist/VRL-UG.jar ${VRL_DIR}/plugin-updates

# remove current VRL-UG-API to force rebuild
echo "rm -r ${VRL_DIR}/plugins/VRL-UG-API*"
rm -r ${VRL_DIR}/plugins/VRL-UG-API*

echo "Opening VRL-Studio ..."
$($VRL_STUDIO_OPEN_SCRIPT) &> /dev/null

