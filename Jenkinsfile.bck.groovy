pipeline {

    agent any

    tools {
        // Install the Maven version configured as and add it to the path.
        maven 'Maven-3.8.6'
        jdk 'JDK-9.0.4'
    }

    stages {
        
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
            
        }
        stage('Build') {
            steps {
                git 'https://github.com/medTX94/TP_pipeline-master.git'
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
                
            }
            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}