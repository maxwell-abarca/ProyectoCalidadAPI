pipeline {
    agent any

    environment {
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Test', url: 'https://github.com/maxwell-abarca/ProyectoCalidadAPI.git'
            }
        }

        stage('Preparar wrapper') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Compilar y ejecutar pruebas') {
            steps {
                sh './gradlew clean test jacocoTestReport'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                    archiveArtifacts artifacts: 'build/reports/jacoco/test/html/**', allowEmptyArchive: true
                }
            }
        }

        stage('Analisis SonarQube') {
            steps {
                withSonarQubeEnv('Local SonarQube') {
                    sh './gradlew sonarqube'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}