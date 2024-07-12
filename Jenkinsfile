pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/soufiane-ba/repo1.git'
        DOCKER_IMAGE = 'maven:latest'
        POSTGRES_DB = 'book-store'
        POSTGRES_USER = 'soufiane'
        POSTGRES_PASSWORD = 'soufiane'
        BACKEND_PORT = 8081
        FRONTEND_PORT = 3000
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/soufiane-ba/repo1.git'
                }
            }
        }

        stage('Database Configuration') {
            steps {
                script {
                    sh 'echo spring.datasource.url=jdbc:postgresql://localhost:5432/book-store > src/main/resources/application.properties'
                }
            }
        }

        stage('Build and Run Backend') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        def dockerImage = docker.build('backend', '-f dockerfileboot .')
                        dockerImage.inside("-v ${PWD}/my-project:/app") {
                            sh 'mvn clean install && java -jar target/book-rest-api-reactjs-0.0.1-SNAPSHOT.jar --server.port=8081 &'
                        }
                    }
                }
            }
        }

        stage('Build and Run Frontend') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        def dockerImage = docker.build('frontend', '-f dockerfilejs .')
                        dockerImage.inside("-v ${PWD}/my-project:/app") {
                            sh 'cd src/main/webapp/reactjs && npm install && npm audit fix && npm run build -- --port 3000 &'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        sh 'docker build -t backend -f dockerfileboot .'
                        sh 'cd src/main/webapp/reactjs && docker build -t frontend -f dockerfilejs .'
                    }
                }
            }
        }

        stage('Run Docker Containers') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        sh 'echo "POSTGRES_DB=${POSTGRES_DB}" > .env'
                        sh 'echo "POSTGRES_USER=${POSTGRES_USER}" >> .env'
                        sh 'echo "POSTGRES_PASSWORD=${POSTGRES_PASSWORD}" >> .env'
                        sh 'docker-compose up -d'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
