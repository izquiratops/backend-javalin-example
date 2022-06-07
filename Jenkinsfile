pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'

                // Run the maven build
                sh "mvn clean verify"
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
}
