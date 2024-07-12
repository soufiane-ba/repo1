pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/soufiane-ba/repo1.git'
        POSTGRES_DB = 'book-store'
        POSTGRES_USER = 'soufiane'
        POSTGRES_PASSWORD = 'soufiane'
        BACKEND_PORT = 8081
        FRONTEND_PORT = 3000
    }

    stages {
        stage('Checkout') {
            steps {
                // Cloner le dépôt
                git branch: 'main', url: env.GIT_REPO_URL
            }
        }
        
        stage('Database Configuration') {
            steps {
                // Modifier le fichier application.properties
                script {
                    sh 'echo "spring.datasource.url=jdbc:postgresql://localhost:5432/${POSTGRES_DB}" > src/main/resources/application.properties'
                }
            }
        }
        
        stage('Install PostgreSQL') {
            steps {
                // Utilisation d'un conteneur Docker pour installer PostgreSQL
                script {
                    docker.image('postgres:latest').run("-e POSTGRES_DB=${POSTGRES_DB} -e POSTGRES_USER=${POSTGRES_USER} -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD}")
                }
            }
        }
        
        stage('Build and Run Backend') {
            steps {
                // Utilisation d'un conteneur Docker pour construire et exécuter le backend Java
                script {
                    docker.image('maven:latest').inside('-v ${PWD}/my-project:/app') {
                        sh 'cd /app && mvn clean install && java -jar target/book-rest-api-reactjs-0.0.1-SNAPSHOT.jar --server.port=${BACKEND_PORT} &'
                    }
                }
            }
        }
        
        stage('Build and Run Frontend') {
            steps {
                // Utilisation d'un conteneur Docker pour construire et exécuter le frontend React
                script {
                    docker.image('node:latest').inside('-v ${PWD}/my-project:/app') {
                        sh 'cd /app/src/main/webapp/reactjs && npm install && npm audit fix && npm run build -- --port ${FRONTEND_PORT} &'
                    }
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                // Utilisation d'un conteneur Docker pour construire les images Docker
                script {
                    docker.image('docker:latest').inside('-v ${PWD}/my-project:/app') {
                        sh 'cd /app && docker build -t backend -f dockerfileboot .'
                        sh 'cd /app/src/main/webapp/reactjs && docker build -t frontend -f dockerfilejs .'
                    }
                }
            }
        }
        
        stage('Run Docker Containers') {
            steps {
                // Utilisation d'un conteneur Docker pour exécuter les conteneurs Docker avec Docker Compose
                script {
                    docker.image('docker/compose:latest').inside('-v ${PWD}/my-project:/app') {
                        sh 'cd /app && echo "POSTGRES_DB=${POSTGRES_DB}" > .env && echo "POSTGRES_USER=${POSTGRES_USER}" >> .env && echo "POSTGRES_PASSWORD=${POSTGRES_PASSWORD}" >> .env && docker-compose up -d'
                    }
                }
            }
        }
    }

    post {
        success {
            // Notifications de succès
            echo 'Pipeline completed successfully!'
        }
        failure {
            // Notifications d'échec
            echo 'Pipeline failed.'
        }
    }
}
