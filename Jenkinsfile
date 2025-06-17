// 자주 사용되는 필요한 변수를 전역으로 선언하는 것도 가능.
def ecrLoginHelper = "docker-credential-ecr-login" // ECR credential helper 이름
def deployHost = "172.31.40.224" // 배포 인스턴스의 private 주소

// 젠킨스의 선언형 파이프라인 정의부 시작 (그루비 언어)
pipeline {
    agent any // 어느 젠킨스 서버에서나 실행이 가능
    environment {
        SERVICE_DIRS = "config-service,discovery-service,gateway-service,api-service,user-service,order-service,review-service,coupon-service"
        ECR_URL = "390844784325.dkr.ecr.ap-northeast-2.amazonaws.com"
        REGION = "ap-northeast-2"
    }
    stages {
        stage('Pull Codes from Github') {
            steps {
                checkout scm
            }
        }

        stage('Add Secret To config-service') {
            steps {
                withCredentials([file(credentialsId: 'config-secret', variable: 'configSecret')]) {
                    script {
                        sh 'cp $configSecret config-service/src/main/resources/application-prod.yml'
                    }
                }
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    def commitCount = sh(script: "git rev-list --count HEAD", returnStdout: true)
                                        .trim()
                                        .toInteger()
                    def changedServices = []
                    def serviceDirs = env.SERVICE_DIRS.split(",")

                    if (commitCount == 1) {
                        echo "Initial commit detected. All services will be built."
                        changedServices = serviceDirs
                    } else {
                        def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true)
                                            .trim()
                                            .split('\n')

                        echo "Changed files: ${changedFiles}"

                        serviceDirs.each { service ->
                            if (changedFiles.any { it.startsWith(service + "/") }) {
                                changedServices.add(service)
                            }
                        }
                    }

                    env.CHANGED_SERVICES = changedServices.join(",")
                    if (env.CHANGED_SERVICES == "") {
                        echo "No changes detected in service directories. Skipping build and deployment."
                        currentBuild.result = 'SUCCESS'
                    }
                }
            }
        }

        stage('Build Changed Services') {
            when {
                expression { env.CHANGED_SERVICES != "" }
            }
            steps {
                script {
                    def changedServices = env.CHANGED_SERVICES.split(",")
                    changedServices.each { service ->
                        sh """
                        echo "Building ${service}..."
                        cd ${service}
                        ./gradlew clean build -x test
                        ls -al ./build/libs
                        cd ..
                        """
                    }
                }
            }
        }

        stage('Build Docker Image & Push to AWS ECR') {
            when {
                expression { env.CHANGED_SERVICES != "" }
            }
            steps {
                script {
                    withAWS(region: "${REGION}", credentials: "aws-key") {
                        def changedServices = env.CHANGED_SERVICES.split(",")
                        changedServices.each { service ->
                            sh """
                            curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                            chmod +x ${ecrLoginHelper}
                            mv ${ecrLoginHelper} /usr/local/bin/

                            mkdir -p ~/.docker
                            echo '{"credHelpers": {"${ECR_URL}": "ecr-login"}}' > ~/.docker/config.json

                            docker build -t ${service}:latest ${service}
                            docker tag ${service}:latest ${ECR_URL}/${service}:latest
                            docker push ${ECR_URL}/${service}:latest
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy Changed Services to AWS EC2') {
            steps {
                sshagent(credentials: ["deploy-key"]) {
                    sh """
                    scp -o StrictHostKeyChecking=no docker-compose.yml ubuntu@${deployHost}:/home/ubuntu/docker-compose.yml

                    ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} '
                    cd /home/ubuntu && \

                    aws ecr get-login-password --region ${REGION} | docker login --username AWS --password-stdin ${ECR_URL} && \

                    CHANGED_SERVICES="${env.CHANGED_SERVICES}" && \
                    SERVICES=\$(echo \$CHANGED_SERVICES | tr "," " ") && \
                    docker-compose pull \$SERVICES && \
                    docker compose up -d \$SERVICES
                    '
                    """
                }
            }
        }

    }
}
