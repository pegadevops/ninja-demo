#!/bin/bash
rm -rf ./target/demo
mkdir -p ./target/demo
cd ./target/demo

cp ../demo-$1.zip .
jar -xf ./demo-$1.zip
chmod ug+x ./deploy.sh
./deploy.sh -nc -env $2