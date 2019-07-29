#!/bin/bash
rm -rf ./ninja-demo-app/target/app
mkdir -p ./ninja-demo-app/target/app
cd ./ninja-demo-app/target/app

cp ../ninja-demo-app-$1.gbl.zip .
jar -xf ./ninja-demo-app-$1.gbl.zip
chmod ug+x ./deploy.sh
./deploy.sh -nc -env $2