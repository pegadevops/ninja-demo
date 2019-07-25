@echo off
java -Djava.security.egd=file:///dev/urandom -cp "config;lib/*" ru.lanit.bpm.goblin.deploy.core.Main %*
pause
