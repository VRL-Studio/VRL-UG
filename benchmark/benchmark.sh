START=`date "+%s"`
$UG4_ROOT/bin/ugshell -ex elder.lua -numRefs 5 -numTimeSteps 30
STOP=`date "+%s"`
echo "Elapsed Time:    $(echo "$STOP - $START"|bc ) seconds."
