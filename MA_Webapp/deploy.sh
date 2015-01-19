export DEPLOY_PATH=/home/pham/servers/apache-tomcat-6.0.41/webapps
ant -buildfile Deploy.xml clean-war
ant -buildfile Deploy.xml create-war
rm -rf $DEPLOY_PATH/agents-app*
cp target/agents-app.war $DEPLOY_PATH
find . -name *~ | xargs rm -rf
