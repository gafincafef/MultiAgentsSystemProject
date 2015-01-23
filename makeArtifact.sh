#!/bin/bash

#Clean junk files
find . -name *~ | xargs rm -rf

#Clean binary files
cd MA_Webapp
ant -buildfile Deploy.xml clean-dependencies
ant -buildfile Deploy.xml clean-war
cd ..
rm -rf apache-tomcat-6.0.41/webapps/agents-app*
rm MA_Bin/*.txt

#Packing src to zip file
tar -zcvf NashEquilibriumApp.tar.gz apache-tomcat-6.0.41 MA_Algorithm MA_Bin MA_Common MA_Jade MA_Theory MA_Webapp Readme.txt

#Rebuild and packing to war file
cd MA_Bin
./build_all.sh
