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
                // Installer PostgreSQL
                script {
                    sh '''
                    sudo apt update
                    sudo apt install -y postgresql postgresql-contrib
                    sudo -u postgres psql -c "CREATE USER ${POSTGRES_USER} WITH PASSWORD '${POSTGRES_PASSWORD}';"
                    sudo -u postgres psql -c "CREATE DATABASE ${POSTGRES_DB} OWNER ${POSTGRES_USER};"
                    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE ${POSTGRES_DB} TO ${POSTGRES_USER};"
                    '''
                }
            }
        }
        
        stage('Build and Run Backend') {
            steps {
                // Construire et exécuter le backend Java
                script {
                    sh '''
                    cd my-project
                    mvn clean install
                    java -jar target/book-rest-api-reactjs-0.0.1-SNAPSHOT.jar --server.port=${BACKEND_PORT} &
                    '''
                }
            }
        }
        
        stage('Build and Run Frontend') {
            steps {
                // Construire et exécuter le frontend React
                script {
                    sh '''
                    cd my-project/src/main/webapp/reactjs
                    npm install
                    npm audit fix
                    npm run build -- --port ${FRONTEND_PORT} &
                    '''
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                // Builder les images Docker pour le backend et le frontend
                script {
                    sh '''
                    cd my-project
                    docker build -t backend -f dockerfileboot .
                    cd src/main/webapp/reactjs
                    docker build -t frontend -f dockerfilejs .
                    '''
                }
            }
        }
        
        stage('Run Docker Containers') {
            steps {
                // Exécuter les conteneurs Docker avec Docker Compose
                script {
                    sh '''
                    cd my-project
                    echo "POSTGRES_DB=${POSTGRES_DB}" > .env
                    echo "POSTGRES_USER=${POSTGRES_USER}" >> .env
                    echo "POSTGRES_PASSWORD=${POSTGRES_PASSWORD}" >> .env
                    docker-compose up -d
                    '''
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
