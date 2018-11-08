pipeline {
    options {
        disableConcurrentBuilds()
    }
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven(maven: 'M3', publisherStrategy: 'EXPLICIT', globalMavenSettingsConfig: 'mvn-global-settings') {
                    sh 'mvn -pl demo-parent versions:set@set-build-version -Dbuild.patch.version=$BUILD_NUMBER'
                    sh 'mvn clean verify deploy:deploy -U -P env-dev'
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'rm target/surefire-reports/*.xml'
                }
            }
        }
    }
}