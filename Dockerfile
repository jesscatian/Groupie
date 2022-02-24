FROM debian:buster

ENV NOVNC_TAG="v1.3.0-beta"
ENV WEBSOCKIFY_TAG="v0.10.0"
ENV VNC_PASSWORD="vncpasswd"

# install JDK and maven
RUN apt-get update && apt-get install -y openjdk-11-jdk maven 

# install dependencies for VNC server
RUN apt install -y apt-transport-https ca-certificates tzdata locales libcurl4 curl gnupg fontconfig fontconfig-config fonts-dejavu-core fonts-liberation fonts-wqy-zenhei fonts-thai-tlwg-ttf fonts-ipafont-mincho fonts-sahadeva fonts-noto-unhinted fonts-noto-color-emoji libfontconfig1 libfontenc1 libfreetype6 libxfont2 libxft2 libnss3-tools xfonts-base xfonts-encodings xfonts-utils xvfb pulseaudio fluxbox x11vnc feh wmctrl libnss-wrapper xsel x11-utils tigervnc-common iproute2

# install VNC viewer (noVNC)
RUN \
    apt install -y python3 python3-pip procps git && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    git config --global advice.detachedHead false && \
    git clone https://github.com/novnc/noVNC --branch ${NOVNC_TAG} /root/noVNC && \
    git clone https://github.com/novnc/websockify --branch ${WEBSOCKIFY_TAG} /root/noVNC/utils/websockify && \
    cp /root/noVNC/vnc.html /root/noVNC/index.html

RUN sed -i "s/'autoconnect', false/'autoconnect', true/" /root/noVNC/app/ui.js
RUN sed -i "s/WebUtil.getConfigVar('password')/'$VNC_PASSWORD'/" /root/noVNC/app/ui.js

# install chromium
RUN apt-get install -y chromium chromium-driver

# set up command for running chromium
RUN echo 'chromium --no-sandbox --disable-gpu --allow-pre-commit-input --disable-background-networking --disable-client-side-phishing-detection --disable-default-apps --disable-hang-monitor --disable-popup-blocking --disable-prompt-on-repost --disable-sync --enable-blink-features=ShadowDOMV0 --enable-logging --log-level=0 --no-first-run --no-service-autorun --password-store=basic --remote-debugging-port=0 --test-type=webdriver --use-mock-keychain --user-data-dir="$(mktemp -d)" "$*"' > /usr/local/bin/chrome-browser && chmod 755 /usr/local/bin/chrome-browser

COPY docker/entrypoint.sh /

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

VOLUME ["/root/.m2", "/usr/local/310-project"]