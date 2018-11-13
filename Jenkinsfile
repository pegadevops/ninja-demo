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
        stage('Quality Gate') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    def qualityGate = waitForQualityGate()
                    if (qualityGate.status == 'ERROR') {
                        error "Sonar Quality Gate failed: ${qualityGate.status}"
                    }
                }
            }
        }
    }
}