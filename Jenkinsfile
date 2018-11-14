pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven(maven: 'M3', publisherStrategy: 'EXPLICIT', globalMavenSettingsConfig: 'mvn-global-settings') {
                    sh 'mvn -pl demo-parent versions:set@set-build-version -Dbuild.patch.version=$BUILD_NUMBER'
                    withSonarQubeEnv('Sonar') {
                        sh 'mvn clean deploy -U -P env-dev,build-server,code-coverage'
                    }
                }
            }
            post {
                always {
                    junit '*/target/surefire-reports/*.xml'
                }
            }
        }
        stage('QA') {
            steps {
                input 'Proceed to QA with this build?'
                sh 'src/main/scripts/deploy-qa.sh'
            }
        }
        stage('Release') {
            steps {
                input 'Release this build?'
                sh 'src/main/scripts/release.sh'
            }
        }
    }
}