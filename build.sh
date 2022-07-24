gradle shadowJar
mv build/libs/*.jar $TEST_FOLDER
CURRENT_PATH=$(pwd)
cd $TEST_FOLDER/..
./start.sh
cd $CURRENT_PATH
