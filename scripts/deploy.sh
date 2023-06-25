#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=springboot-webservice

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl springboot-webservice | grep jar | awk '{print $1}') #수행 중인 스프링부트 어플리케이션 프로세스 id 찾기, 실행중라면 종료해주기 위함

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo  "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo " > kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR_NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME #jar파일에 실행권한 주기

echo "> $JAR_NAME 실행"

#nohup(no hang up)명령어는 터미널과의 세션 끊겨도 데몬 형태로 프로세스가 계속 동작하도록 함
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
        -Dspring.profiles.active=real \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
#nohup으로 계속 동작하기 때문에 codedeploy는 무한 대기, 따라서 그전에 프로그램의 표준입출력을 nohup.out파일에 출력, &은 백그라운드에서 실행
#프로그램을 종료 없이 백그라운드에서 실행하고 싶다 -> nohup + &


