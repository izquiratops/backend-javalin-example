pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'

                withMaven(
                    jdk: 'JDK 11',
                    maven: 'Maven 3.8') {
                    // Run the maven build
                    sh "mvn clean verify"
                }
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
