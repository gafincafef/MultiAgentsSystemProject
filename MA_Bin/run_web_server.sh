rm -rf ../apache-tomcat-6.0.41/webapps/agents-app*
cp ../MA_Webapp/target/agents-app.war ../apache-tomcat-6.0.41/webapps
cd ../apache-tomcat-6.0.41/bin
./catalina.sh run

