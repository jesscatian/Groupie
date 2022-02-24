#!/bin/bash

# adapted from selenoid/vnc

SCREEN_RESOLUTION=${SCREEN_RESOLUTION:-"1920x1080x24"}
DISPLAY_NUM=99
export DISPLAY=":$DISPLAY_NUM"

VERBOSE=${VERBOSE:-""}
DRIVER_ARGS=${DRIVER_ARGS:-""}
if [ -n "$VERBOSE" ]; then
    DRIVER_ARGS="$DRIVER_ARGS --verbose"
fi

clean() {
  if [ -n "$FILESERVER_PID" ]; then
    kill -TERM "$FILESERVER_PID"
  fi
  if [ -n "$XSELD_PID" ]; then
    kill -TERM "$XSELD_PID"
  fi
  if [ -n "$XVFB_PID" ]; then
    kill -TERM "$XVFB_PID"
  fi
  if [ -n "$DRIVER_PID" ]; then
    kill -TERM "$DRIVER_PID"
  fi
  if [ -n "$X11VNC_PID" ]; then
    kill -TERM "$X11VNC_PID"
  fi
  if [ -n "$DEVTOOLS_PID" ]; then
    kill -TERM "$DEVTOOLS_PID"
  fi
  if [ -n "$PULSE_PID" ]; then
    kill -TERM "$PULSE_PID"
  fi
}

trap clean SIGINT SIGTERM

while ip addr | grep inet | grep -q tentative > /dev/null; do sleep 0.1; done

mkdir -p ~/pulse/.config/pulse
echo -n 'gIvST5iz2S0J1+JlXC1lD3HWvg61vDTV1xbmiGxZnjB6E3psXsjWUVQS4SRrch6rygQgtpw7qmghDFTaekt8qWiCjGvB0LNzQbvhfs1SFYDMakmIXuoqYoWFqTJ+GOXYByxpgCMylMKwpOoANEDePUCj36nwGaJNTNSjL8WBv+Bf3rJXqWnJ/43a0hUhmBBt28Dhiz6Yqowa83Y4iDRNJbxih6rB1vRNDKqRr/J9XJV+dOlM0dI+K6Vf5Ag+2LGZ3rc5sPVqgHgKK0mcNcsn+yCmO+XLQHD1K+QgL8RITs7nNeF1ikYPVgEYnc0CGzHTMvFR7JLgwL2gTXulCdwPbg=='| base64 -d>~/pulse/.config/pulse/cookie
HOME=$HOME/pulse pulseaudio --start --exit-idle-time=-1
HOME=$HOME/pulse pactl load-module module-native-protocol-tcp
PULSE_PID=$(ps --no-headers -C pulseaudio -o pid | sed -r 's/( )+//g')

/usr/bin/xvfb-run -l -n "$DISPLAY_NUM" -s "-ac -screen 0 $SCREEN_RESOLUTION -noreset -listen tcp" /usr/bin/fluxbox -display "$DISPLAY" -log /dev/null 2>/dev/null &
XVFB_PID=$!

retcode=1
until [ $retcode -eq 0 ]; do
  DISPLAY="$DISPLAY" wmctrl -m >/dev/null 2>&1
  retcode=$?
  if [ $retcode -ne 0 ]; then
    echo Waiting X server...
    sleep 0.1
  fi
done

x11vnc -display "$DISPLAY" -passwd vncpasswd -shared -forever -loop500 -rfbport 5900 -rfbportv6 5900 -logfile /dev/null &
X11VNC_PID=$!

/root/noVNC/utils/novnc_proxy --vnc localhost:5900 &

# set up webdrivermanager to use the pre-installed chromedriver
export CHROMIUM_FLAGS="--no-sandbox"
export WDM_CHROMEDRIVERVERSION=$(chromedriver --version | sed -E 's/ChromeDriver ([0-9.]+) .*/\1/g')
export WDM_CHROMEVERSION=$(chromium --version | sed -E 's/Chromium ([0-9.]+) .*/\1/g')
export WDM_CACHEPATH=/root/.cache/debian-drivers
export WDM_ARCHITECTURE=X64 # force this so WDM looks for drivers in a consistent place (even if the arch is not x86-64)
mkdir -p "$WDM_CACHEPATH/linux64/$WDM_CHROMEDRIVERVERSION/"
pushd "$WDM_CACHEPATH/linux64/$WDM_CHROMEDRIVERVERSION/"
ln -s /usr/bin/chromedriver
popd

cd /usr/local/310-project && xterm