pipeline {
  agent any
  stages {
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