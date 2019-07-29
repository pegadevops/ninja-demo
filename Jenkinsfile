pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -pl demo-parent versions:set@set-build-version -Dninja.build.patch.version=$BUILD_NUMBER'
                withSonarQubeEnv('Sonar') {
                    sh 'mvn clean deploy -U -P env-dev,build-server,nightly-build,code-coverage,sonar-analyze,update-codebase'
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
                sh 'mvn exec:exec@deploy-demo -N -P env-qa'
            }
        }
        stage('Release') {
            steps {
                input 'Release this build?'
                sh 'mvn -pl demo-parent versions:set@set-release-version -Dninja.build.patch.version=$BUILD_NUMBER'
                sh 'mvn deploy-maven-plugin:deploy-artifacts@deploy-release-cli -P build-server'
            }
        }
    }
}