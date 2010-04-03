# This script installs the man code to where it needs to go.
# It also copies the color table to the correct location
# There are two modes:
#  1) REMOTE_INSTALLATION to a robot
#  2) SIMULATOR_INSTALLATION to a local naoqi folder

# Exit on error
set -e
# No uninitialized variables
set -o nounset

#CMake configured options
REMOTE="ON"

WEBOTS=""
ROBOT=trillian.local

if [ "$WEBOTS" = "" ]; then
    WEBOTS="OFF"
fi

NAO_CONTROLLER=projects/contests/nao_robocup/controllers/nao_soccer_player_red

if [ $WEBOTS = "ON" ]; then
echo "Setting local install to WEBOT"
    LOCAL_DEST=$WEBOTS_HOME/$NAO_CONTROLLER
else
    LOCAL_DEST=/usr/local/nao-1.4/modules
fi

COLOR_TABLE_PATH=tables/lab120/jack800.mtb
COLOR_TABLE_DIR=install/etc
TABLE_STD_NAME=table.mtb

if [ $REMOTE = "ON" ] && [  $WEBOTS = "OFF" ] ; then

  echo "Installing remotely"

  DEST=/home/nao/naoqi/lib/naoqi

  if [ ! -n "$ROBOT" ]; then
    echo "[upload.sh] Error - no remote address specified" >&2
    exit 1
  fi
  DEST=nao@$ROBOT:$DEST

else
  echo "Installing locally"
  DEST=$LOCAL_DEST

fi

#if [ $REMOTE != "ON" ]; then
#  #echo "[upload.sh] Error - remote install not specified in configuration" >&2
#  #exit 1
#  echo "No remote install"
#  exit 0
#fi

mkdir -p $COLOR_TABLE_DIR #ensure the color table directory exists
echo "echo $COLOR_TABLE_PATH > $COLOR_TABLE_DIR/whichtable.txt"
echo $COLOR_TABLE_PATH > $COLOR_TABLE_DIR/whichtable.txt
echo "cp $COLOR_TABLE_PATH $COLOR_TABLE_DIR/$TABLE_STD_NAME"
cp $COLOR_TABLE_PATH $COLOR_TABLE_DIR/$TABLE_STD_NAME

SRC=install/lib/*

echo "rsync -rcLv $SRC $DEST/"
rsync -rcLv $SRC $DEST/

ETC_SRC=install/etc/*
ETC_DEST=$DEST/../../etc
rsync -rcLv $ETC_SRC $ETC_DEST/
#echo "scp -rv $SRC $DEST/"
#scp -r $SRC $DEST/
