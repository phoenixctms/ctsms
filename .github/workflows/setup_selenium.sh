#!/bin/bash

#curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
#echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
#apt-get -y update
#apt-get -y install google-chrome-stable

wget --no-verbose https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip
unzip chromedriver_linux64.zip

mv chromedriver /usr/bin/chromedriver
chown root:root /usr/bin/chromedriver
chmod +x /usr/bin/chromedriver

wget --no-verbose https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar
#apt-get -y install xvfb 


apt-get -q -y install cpanminus
cpanm --notest Selenium::Remote::Driver 
