pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven(maven: 'M3', publisherStrategy: 'EXPLICIT', globalMavenSettingsConfig: 'mvn-global-settings') {
                    sh 'mvn -pl demo-parent versions:set@set-build-version -Dbuild.patch.version=$BUILD_NUMBER'
                    sh 'mvn clean verify deploy:deploy -U -P env-dev,build-server'
                }
            }
            post {
                always {
                    junit '*/target/surefire-reports/*.xml'
                }
            }
        }
    }
}