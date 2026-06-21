pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    triggers {
        githubPush()
    }
    environment {
        APP_NAME = 'gradle-demo-email'
        LOGIN_REGISTRY = 'docker.io'
        IMAGE_NAMESPACE = 'kpwork7827'
        IMAGE_REPOSITORY = 'gradle-demo-email'
        IMAGE_NAME = "${LOGIN_REGISTRY}/${IMAGE_NAMESPACE}/${IMAGE_REPOSITORY}"
        KUBE_NAMESPACE = 'gradle-demo'
        KUBE_DEPLOYMENT = 'gradle-demo-email'
        CONTAINER_NAME = 'gradle-demo-email'
        DOCKER_CREDENTIALS_ID = 'docker-registry-creds'
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-creds'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_SHORT_SHA = sh(script: 'git rev-parse --short=8 HEAD', returnStdout: true).trim()
                    env.IMAGE_TAG = "${env.BUILD_NUMBER}-${env.GIT_SHORT_SHA}"
                }
            }
        }

        stage('Unit Test & Package') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build --no-daemon'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                      -t ${IMAGE_NAME}:${IMAGE_TAG} \
                      -t ${IMAGE_NAME}:latest \
                      .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: env.DOCKER_CREDENTIALS_ID,
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh '''
                        echo "${DOCKER_PASSWORD}" | docker login "${LOGIN_REGISTRY}" -u "${DOCKER_USERNAME}" --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${IMAGE_NAME}:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: env.KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
                    sh '''
                        kubectl apply -f k8s/namespace.yaml
                        kubectl apply -f k8s/configmap.yaml
                        kubectl apply -f k8s/service.yaml
                        kubectl apply -f k8s/deployment.yaml
                        kubectl set image deployment/${KUBE_DEPLOYMENT} ${CONTAINER_NAME}=${IMAGE_NAME}:${IMAGE_TAG} -n ${KUBE_NAMESPACE}
                        kubectl rollout status deployment/${KUBE_DEPLOYMENT} -n ${KUBE_NAMESPACE} --timeout=180s
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed. Deployed ${IMAGE_NAME}:${IMAGE_TAG}"
        }
        failure {
            echo 'Pipeline failed. Check the failed stage logs above.'
        }
        always {
            sh 'docker logout ${LOGIN_REGISTRY} || true'
        }
    }
}
