pipeline{
    agent any

    environment {
        WS = "${WORKSPACE}"
        HARBOR_URL="10.206.73.155"
        HARBOR_ID="e776c21b-53d5-4746-8359-6d5dac8529fb"
    }

    stages {
        stage("环境检查"){
            steps {
                sh 'whoami'
                sh 'echo $JAVA_HOME'
                sh 'echo $M2_HOME'
                sh 'mvn -v'
                sh 'java -version'
                sh 'docker version'
            }
        }
        stage('整体编译打包镜像') {
            steps {
                sh "echo ${WS}"
                sh "cd ${WS} && mvn clean install -DskipTests"
            }
        }
        stage('docker镜像与推送') {
            steps {
                sh "cd ${WS} && docker build -f Dockerfile -t ${HARBOR_URL}/test/springframe:latest ."
                withCredentials([usernamePassword(credentialsId: "${HARBOR_ID}", passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login -u ${username} -p ${password} ${HARBOR_URL}"
                    sh "docker push ${HARBOR_URL}/test/springframe:latest"
                }
            }
        }
    }
}