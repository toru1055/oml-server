#!/bin/bash

start(){
  if pid=`status` >/dev/null ;then
    echo "\`$exe' is already running: $pid"
    exit 1
  fi
  echo -n "Starting $exe : "
  nohup $exe &
  [ $? -ne 0 ] && echo NG || echo OK
  pid=$!
  echo $pid | tee $pidFile
}
stop(){
  if pid=`status` >/dev/null ;then
    echo -n "Stopping $exe: "
    kill -SIGKILL $pid
    [ $? -ne 0 ] && echo NG || echo OK
    rm $pidFile
    exit $?
  fi
  echo "Not runnning"
  exit 1
}
status(){
  if [ -s $pidFile ];then
    pid=`cat $pidFile`
    echo "$pid"
    return 0
  fi
  echo "NG"
  return 1
}

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  status)
    status
    ;;
  *)
    echo "Usage: $0 {start|stop|status}"
    exit 1
esac
