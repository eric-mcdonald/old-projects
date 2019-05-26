cd /d "D:\lanius\lanius-driver\lanius-driver" &msbuild "lanius-driver.vcxproj" /t:sdvViewer /p:configuration="Debug" /p:platform=x64
exit %errorlevel% 