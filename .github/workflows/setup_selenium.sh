#!/bin/bash

#curl -sS -o - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
#echo "deb [arch=amd64]  http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
#apt-get -y update
#apt-get -y install google-chrome-stable

#apt-get -q -y install unzip
#wget --no-verbose https://chromedriver.storage.googleapis.com/2.41/chromedriver_linux64.zip
#wget --no-verbose https://chromedriver.storage.googleapis.com/105.0.5195.52/chromedriver_linux64.zip
#wget --no-verbose https://chromedriver.storage.googleapis.com/106.0.5249.61/chromedriver_linux64.zip
#wget --no-verbose https://chromedriver.storage.googleapis.com/109.0.5414.25/chromedriver_linux64.zip
wget --no-verbose https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_linux64.zip
unzip chromedriver_linux64.zip

mv chromedriver /usr/bin/chromedriver
chown root:root /usr/bin/chromedriver
chmod +x /usr/bin/chromedriver

#wget --no-verbose https://selenium-release.storage.googleapis.com/3.141/selenium-server-standalone-3.141.59.jar
#wget --no-verbose https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.4.0/selenium-server-4.4.0.jar
#apt-get -q -y install xvfb 

##apt-get -q -y install build-essential
#apt-get -q -y install cpanminus
#cpanm --notest Selenium::Remote::Driver 
