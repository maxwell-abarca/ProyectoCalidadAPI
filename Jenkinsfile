pipeline {
    agent any

    environment {
        GRADLE_OPTS = '-Dorg.gradle.daemon=false'
    }

    stages {
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
                    junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
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