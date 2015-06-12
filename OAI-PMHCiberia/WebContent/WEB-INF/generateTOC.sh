#! /bin/tcsh -f

rm -rf ../TOC
rm -rf TOC
mkdir TOC
java -classpath lib/oaiharvester.jar:$TOMCAT_HOME/xerces.jar ORG.oclc.oai.harvester.app.TOCGenerator $1 oai_dc
mv TOC ..


