pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven(maven: 'M3', publisherStrategy: 'EXPLICIT', globalMavenSettingsConfig: 'mvn-global-settings') {
                    sh 'mvn -pl demo-parent versions:set@set-build-version -Dbuild.patch.version=$BUILD_NUMBER'
                    withSonarQubeEnv('Sonar') {
                        sh 'mvn clean deploy -U -P env-dev,build-server,nightly-build,code-coverage'
                    }
                }
            }
            post {
                always {
                    junit '*/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('QA') {
            steps {
                input 'Proceed to QA with this build?'
                sh './src/main/scripts/deploy-qa.sh'
            }
        }
        stage('Release') {
            steps {
                input 'Release this build?'
                withMaven(maven: 'M3', publisherStrategy: 'EXPLICIT', globalMavenSettingsConfig: 'mvn-global-settings') {
                    sh 'mvn deploy-maven-plugin:deploy-artifacts@deploy-release-cli -P build-server'
                }
            }
        }
    }
}