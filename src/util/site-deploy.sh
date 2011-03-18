#! /bin/bash -i

ROOT=$PWD/../..;
PROJECT="crawler"

SITE_DIR=$ROOT/target/tubaina/html/$PROJECT;

REPORTS_DIR=$ROOT/target/site;

UTIL_DIR=$ROOT/src/util;

SERVER=vidageek@vidageek.net:/home/vidageek/projetos.vidageek.net/$PROJECT/;

cd $ROOT;

mvn clean -o;

#Generate english version of project's site
mvn tubaina:build -o -Pdocs;

#copy redirect file (Needed due to tubaina's structure)
cp $UTIL_DIR/index.html $SITE_DIR/.;

#copy files to server
scp -rC $SITE_DIR/* $SERVER/.;

echo "Please check the site and make sure everything is ok!";
echo "Site deploy successful";
