pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git pull'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew check'
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'build/distributions/*.zip', fingerprint: true
      junit 'build/reports/**/*.xml'
    }
  }
}