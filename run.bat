set AppPath="C:\Users\nahrinrs\Documents\GitHub\jersey-guice-api-gradle"
pushd %AppPath%
rmdir build /s /q 
call gradle clean assemble deploy

set Pathname="build\output"
pushd %Pathname%
java -jar jersey-guice-api-gradle-0.1.jar
popd
popd

