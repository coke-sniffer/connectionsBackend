@echo off

set /p COMMIT_MESSAGE="Enter commit message: "

cd C:\Users\tmwdn\Documents\23-24_JavaProjects\connectionsBackend

git add -A
git commit -am "%COMMIT_MESSAGE%"
git push -u origin master

ssh william@138.197.38.17 "(pkill screen) & screen -d -m bash -c '(cd connectionsBackend && git pull && gradle bootRun)'"
