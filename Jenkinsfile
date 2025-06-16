// 자주 사용되는 필요한 변수를 전역으로 선언하는 것도 가능.
def ecrLoginHelper = "docker-credential-ecr-login" // ECR credential helper 이름

// 젠킨스의 선언형 파이프라인 정의부 시작 (그루비 언어)
pipeline {
    agent any // 어느 젠킨스 서버에서나 실행이 가능
    environment {
        SERVICE_DIRS = "config-service,discovery-service,gateway-service,api-service,user-service,order-service,review-service,coupon-service"
        ECR_URL = "390844784325.dkr.ecr.ap-northeast-2.amazonaws.com"
        REGION = "ap-northeast-2"
    }
    stages {
        // 각 작업 단위를 스테이지로 나누어서 작성 가능.
        stage('Pull Codes from Github') { // 스테이지 제목 (맘대로 써도 됨)
            steps {
                checkout scm // 젠킨스와 연결된 소스 컨트롤 매니저(git 등)에서 코드를 가져오는 명령어
            }
        }

        stage('Add Secret To config-service') {
            steps {
                withCredentials([file(credentialsId: 'config-secret', variable: 'configSecret')]) {
                    script {
                        sh 'cp $configSecret config-service/src/main/resources/application-dev.yml'
                    }
                }
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    // rev-list: 특정 브랜치나 커밋을 기준으로 모든 이전 커밋 목록을 나열
                    // --count: 목록 출력 말고 커밋 개수만 숫자로 반환
                    def commitCount = sh(script: "git rev-list --count HEAD", returnStdout: true)
                                        .trim()
                                        .toInteger()
                    def changedServices = []
                    def serviceDirs = env.SERVICE_DIRS.split(",")

                    if (commitCount == 1) {
                        // 최초 커밋이라면 모든 서비스 빌드
                        echo "Initial commit detected. All services will be built."
                        changedServices = serviceDirs // 변경된 서비스는 모든 서비스다.

                    } else {
                        // 변경된 파일 감지
                        def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true)
                                            .trim()
                                            .split('\n') // 변경된 파일을 줄 단위로 분리

                        // 변경된 파일 출력
                        // [user-service/src/main/resources/application.yml,
                        // user-service/src/main/java/com/playdata/userservice/controller/UserController.java,
                        // ordering-service/src/main/resources/application.yml]
                        echo "Changed files: ${changedFiles}"

                        serviceDirs.each { service ->
                            // changedFiles라는 리스트를 조회해서 service 변수에 들어온 서비스 이름과
                            // 하나라도 일치하는 이름이 있다면 true, 하나도 존재하지 않으면 false
                            // service: user-service -> 변경된 파일 경로가 user-service/로 시작한다면 true
                            if (changedFiles.any { it.startsWith(service + "/") }) {
                                changedServices.add(service)
                            }
                        }
                    }

                    //변경된 서비스 이름을 모아놓은 리스트를 다른 스테이지에서도 사용하기 위해 환경 변수로 선언.
                    // join() -> 지정한 문자열을 구분자로 하여 리스트 요소를 하나의 문자열로 리턴. 중복 제거.
                    // 환경변수는 문자열만 선언할 수 있어서 join을 사용함.
                    env.CHANGED_SERVICES = changedServices.join(",")
                    if (env.CHANGED_SERVICES == "") {
                        echo "No changes detected in service directories. Skipping build and deployment."
                        // 성공 상태로 파이프라인을 종료
                        currentBuild.result = 'SUCCESS'
                    }
                }
            }
        }

        stage('Build Changed Services') {
            // 이 스테이지는 빌드되어야 할 서비스가 존재한다면 실행되는 스테이지.
            // 이전 스테이지에서 세팅한 CHANGED_SERVICES라는 환경변수가 비어있지 않아야만 실행.
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
                    // jenkins에 저장된 credentials를 사용하여 AWS 자격증명을 설정.
                    withAWS(region: "${REGION}", credentials: "aws-key") {
                        def changedServices = env.CHANGED_SERVICES.split(",")
                        def newTag = "1.0.${env.BUILD_NUMBER}"  // <-- 빌드 번호를 태그에 포함

                        changedServices.each { service ->
                            sh """
                            # ECR에 이미지를 push하기 위해 인증 정보를 대신 검증해 주는 도구 다운로드.
                            # /usr/local/bin/ 경로에 해당 파일을 이동
                            # ❗️실행마다 설치되므로, Jenkins 에이전트에 미리 설치하거나 캐시 처리 추천.
                            curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                            chmod +x ${ecrLoginHelper}
                            mv ${ecrLoginHelper} /usr/local/bin/

                            # Docker에게 push 명령을 내리면 지정된 URL로 push할 수 있게 설정.
                            # 자동으로 로그인 도구를 쓰게 설정
                            mkdir -p ~/.docker
                            echo '{"credHelpers": {"${ECR_URL}": "ecr-login"}}' > ~/.docker/config.json

                            docker build -t ${service}:${newTag} ${service}
                            docker tag ${service}:${newTag} ${ECR_URL}/${service}:${newTag}
                            docker push ${ECR_URL}/${service}:${newTag}
                            """
                        }
                    }
                }
            }
        }

        // ✅ 새로 추가된 스테이지 (k8s 없이 docker-compose 배포 방식)
        stage('Deploy using Docker Compose') {
            when {
                expression { env.CHANGED_SERVICES != "" } // 변경된 서비스가 있을 때에만 실행
            }

            steps {
                script {
                    def changedServices = env.CHANGED_SERVICES.split(",")
                    // 동일하게 newTag 자동생성 부분 수정
                    def newTag = "1.0.${env.BUILD_NUMBER}"  // <-- 빌드 번호를 태그에 포함

                    changedServices.each { service ->
                        // 로컬 서버나 EC2에서 실행 중인 컨테이너 중지 → 새 이미지로 재배포
                        sh """
                            echo "Deploying ${service} with Docker Compose..."

                            # 기존 컨테이너 종료 (에러 무시)
                            docker compose -f ${service}/docker-compose.yml down || true

                            # 최신 이미지 ECR에서 pull
                            docker pull ${ECR_URL}/${service}:${newTag}

                            # docker-compose.yml 내 image 태그 갱신
                            sed -i 's#image: .*#image: ${ECR_URL}/${service}:${newTag}#' ${service}/docker-compose.yml

                            # 변경된 docker-compose로 서비스 재실행
                            docker compose -f ${service}/docker-compose.yml up -d
                        """
                    }
                }
            }
        }
    }
}
