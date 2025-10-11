#!/bin/bash

# 배포 설정
SERVER="ulala"
REMOTE_DIR="/home/opc/ulala"
APP_NAME="ulala-0.0.1-SNAPSHOT.jar"

# 색상 출력
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NO_COLOR='\033[0m'

# 전체 배포 시작 시간
DEPLOY_START=$(date +%s)

echo -e "${GREEN}========================================${NO_COLOR}"
echo -e "${GREEN}  Ulala Server 배포 시작${NO_COLOR}"
echo -e "${GREEN}========================================${NO_COLOR}"

# 1. 로컬 빌드
echo -e "\n${YELLOW}[1/4] 프로젝트 로컬 빌드 중...${NO_COLOR}"
echo -e "${YELLOW}./gradlew clean build${NO_COLOR}"
STEP_START=$(date +%s)
./gradlew clean build

if [ $? -ne 0 ]; then
    echo -e "${RED}빌드 실패!${NO_COLOR}"
    exit 1
fi

STEP_END=$(date +%s)
STEP_TIME=$((STEP_END - STEP_START))
echo -e "${GREEN}빌드 완료!${NO_COLOR} ${CYAN}(소요시간: ${STEP_TIME}초)${NO_COLOR}"

# 2. JAR 파일 전송
echo -e "\n${YELLOW}[2/4] JAR 파일 전송 중...${NO_COLOR}"
echo -e "${YELLOW}scp build/libs/$APP_NAME $SERVER:$REMOTE_DIR/${NO_COLOR}"
STEP_START=$(date +%s)
scp build/libs/$APP_NAME $SERVER:$REMOTE_DIR/

if [ $? -ne 0 ]; then
    echo -e "${RED}파일 전송 실패!${NO_COLOR}"
    exit 1
fi
STEP_END=$(date +%s)
STEP_TIME=$((STEP_END - STEP_START))
echo -e "${GREEN}파일 전송 완료!${NO_COLOR} ${CYAN}(소요시간: ${STEP_TIME}초)${NO_COLOR}"

# 3. 기존 애플리케이션 종료
echo -e "\n${YELLOW}[3/4] 기존 애플리케이션 종료 중...${NO_COLOR}"
echo -e "${YELLOW}ssh $SERVER \"pkill -f '$APP_NAME' || true\"${NO_COLOR}"
STEP_START=$(date +%s)
ssh $SERVER "pkill -f '$APP_NAME' || true"
sleep 2
STEP_END=$(date +%s)
STEP_TIME=$((STEP_END - STEP_START))
echo -e "${GREEN}애플리케이션 종료 완료!${NO_COLOR} ${CYAN}(소요시간: ${STEP_TIME}초)${NO_COLOR}"

# 4. 새 애플리케이션 실행
echo -e "\n${YELLOW}[4/4] 애플리케이션 시작 중...${NO_COLOR}"
echo -e "${YELLOW}ssh $SERVER \"cd $REMOTE_DIR && (nohup java -jar $APP_NAME --spring.profiles.active=prod >> app.log 2>&1 &)\"${NO_COLOR}"
STEP_START=$(date +%s)
ssh $SERVER "cd $REMOTE_DIR && (nohup java -jar $APP_NAME --spring.profiles.active=prod >> app.log 2>&1 &)"
STEP_END=$(date +%s)
STEP_TIME=$((STEP_END - STEP_START))
echo -e "${GREEN}애플리케이션 시작 완료!${NO_COLOR} ${CYAN}(소요시간: ${STEP_TIME}초)${NO_COLOR}"

# 전체 배포 시간 계산
DEPLOY_END=$(date +%s)
TOTAL_TIME=$((DEPLOY_END - DEPLOY_START))

echo -e "\n${GREEN}========================================${NO_COLOR}"
echo -e "${GREEN}  배포 완료${CYAN} (총 소요시간: ${TOTAL_TIME}초)${NO_COLOR}"
echo -e "${GREEN}========================================${NO_COLOR}"
