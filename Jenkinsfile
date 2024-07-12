pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('IDDockerhub')  // ID des credentials Docker Hub stockés dans Jenkins
        DOCKER_IMAGE_BACKEND = "soufiane1912/my-project-backend"
        DOCKER_IMAGE_FRONTEND = "soufiane1912/my-project-frontend"
    }

    stages {
        stage('Checkout') {
            steps {
                // Clone du repo Git
                git branch: 'main', url: 'https://github.com/soufiane-ba/repo1.git'
            }
        }
        
        stage('Build JAR') {
            steps {
                script {
                    dir('./') {
                        // Construction du projet avec Maven
                        sh 'mvn clean package'
                    }
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                script {
                    dir('./') {
                        sh "docker build -t ${DOCKER_IMAGE_BACKEND} -f dockerfileboot ."
                    }
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                script {
                    dir('./src/main/webapp/reactjs/') {
                        sh "docker build -t ${DOCKER_IMAGE_FRONTEND} -f dockerfilejs ."
                    }
                }
            }
        }

        stage('Push the image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'IDDockerhub', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
                    sh "docker push ${DOCKER_IMAGE_BACKEND}"
                    sh "docker push ${DOCKER_IMAGE_FRONTEND}"
                }
            }
        } 
    } 
}
