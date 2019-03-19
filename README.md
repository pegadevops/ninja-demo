# ninja-demo
Demo project showing Ninja in action

# quick start

    mvn test -Ptest-bulk-mode,env-dev -Ddev-env-pega-url=http://localhost:8080/prweb/PRServlet -Dpega.version=<pega version>

# tested with

    mvn test -Ptest-bulk-mode,env-dev -Ddev-env-pega-url=http://localhost:8080/prweb/PRServlet -Dpega.version=7.3.1
    mvn test -Ptest-bulk-mode,env-dev -Ddev-env-pega-url=http://localhost:8080/prweb/PRServlet -Dpega.version=7.4

# obtaining pega version with curl, xmllint and cut (bash only)

    PEGA_VERSION=$(curl --silent -L https://pega.ethiclab.online/prweb|grep Pega|grep span|xmllint --xpath '/span/text()' - | cut -d ' ' -f 2)
    mvn test -Ptest-bulk-mode,env-dev -Ddev-env-pega-url=http://localhost:8080/prweb/PRServlet -Dpega.version=$PEGA_VERSION

# test API javadoc

https://docs.pegadevops.com/ninja-ut-client/LATEST/
