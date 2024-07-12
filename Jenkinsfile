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
                sh 'cd src/main/resources && echo "spring.datasource.url=jdbc:postgresql://localhost:5432/' + env.POSTGRES_DB + '" > application.properties'
            }
        }
        
        stage('Install PostgreSQL') {
            steps {
                // Installer PostgreSQL
                sh '''
                sudo apt update
                sudo apt install -y postgresql postgresql-contrib
                sudo -i -u postgres psql -c "CREATE USER ${env.POSTGRES_USER} WITH PASSWORD '${env.POSTGRES_PASSWORD}';"
                sudo -i -u postgres psql -c "CREATE DATABASE ${env.POSTGRES_DB} OWNER ${env.POSTGRES_USER};"
                sudo -i -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE ${env.POSTGRES_DB} TO ${env.POSTGRES_USER};"
                '''
            }
        }
        
        stage('Build and Run Backend') {
            steps {
                // Construire et exécuter le backend Java
                sh '''
                cd my-project
                mvn clean install
                java -jar target/book-rest-api-reactjs-0.0.1-SNAPSHOT.jar --server.port=${env.BACKEND_PORT} &
                '''
            }
        }
        
        stage('Build and Run Frontend') {
            steps {
                // Construire et exécuter le frontend React
                sh '''
                cd my-project/src/main/webapp/reactjs
                npm install
                npm audit fix
                npm run build -- --port ${env.FRONTEND_PORT} &
                '''
            }
        }
        
        stage('Build Docker Images') {
            steps {
                // Builder les images Docker pour le backend et le frontend
                sh '''
                cd my-project
                docker build -t backend -f dockerfileboot .
                cd src/main/webap

