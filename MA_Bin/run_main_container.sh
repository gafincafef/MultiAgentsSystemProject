#!/bin/bash
export CLASS_PATH=../MA_Jade/lib/commons-codec-1.3.jar:../MA_Jade/lib/jade.jar

java -cp $CLASS_PATH jade.Boot -name MainContainer-SS
