pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'http://localhost:9000/'
        SONAR_TOKEN = ''
        DOCKERHUB_CREDENTIALS = 'token-dockerhub'
        IMAGE_NAME = 'foyer'
        IMAGE_TAG = 'latest'
    }

    stages {
        stage('get from github') {
            steps {
                echo 'Pulling from achref-hamzaoui-foyer branch...'
                git branch: 'achref-hamzaoui-foyer',
                    url: 'git@github.com:TaiebKacem2023/Groupe4-3ARTIC1-2425.git',
                    credentialsId: 'github-ssh-key'
            }
        }

        stage('RUN DATABASE') {
            steps {
                echo 'Running database...'
                sh "docker-compose up --build -d database"
                sh "sleep 20"
            }
        }

        stage('MVN clean') {
            steps {
                echo 'Cleaning...'
                sh "mvn clean"
            }
        }

        stage('MVN compile') {
            steps {
                echo 'Compiling...'
                sh "mvn compile"
            }
        }

        stage('MVN test') {
            steps {
                echo 'Testing...'
                sh "mvn test"
            }
        }

        stage('SonarQube analysis') {
            steps {
                echo "Running SonarQube analysis..."
                sh "mvn sonar:sonar -Dsonar.url=${SONARQUBE_SERVER} -Dsonar.login=${SONAR_TOKEN}"
            }
        }

        stage('MVN package') {
            steps {
                echo 'Packaging...'
                sh "mvn package"
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    sh "mvn deploy"
                }
            }
        }

        stage('Building image docker') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Push docker image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: DOCKERHUB_CREDENTIALS,
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_PASSWORD'
                )]) {
                    echo 'Logging into DockerHub...'
                    sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD}"
                    echo 'Tagging image...'
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}"
                    echo 'Pushing image to DockerHub...'
                    sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Docker Compose') {
            steps {
                echo 'Restarting with Docker Compose...'
                sh 'docker-compose down || true'
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminée avec succès !'
        }
        failure {
            echo '❌ La pipeline a échoué.'
        }
    }
}
