export DEPLOY_PATH=../apache-tomcat-6.0.41/webapps
export SRC_PATH=target/war

ant -buildfile Deploy.xml copy-war
ant -buildfile Deploy.xml package-war

#rm -rf $DEPLOY_PATH/agents-app.war
rm -rf $DEPLOY_PATH/agents-app/resources
rm -rf $DEPLOY_PATH/agents-app/jsp

cp -r $SRC_PATH/resources $DEPLOY_PATH/agents-app
cp -r $SRC_PATH/jsp $DEPLOY_PATH/agents-app

find . -name *~ | xargs rm -rf

