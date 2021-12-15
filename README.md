# Prerequisites
- Make sure you have read permissions for the target directories (targetDirs)
- Make sure you have write permissions for the working directory (workingDir)

# Usage
- Build with maven: mvn clean compile assembly:single  
- Copy file into UNIX server
- run with 2 arguments
  - Argument 1 - targetDirs: this is the list of directories where the program will search recursively.
    - Example: "/home/special_user,/app,/tmp"
  - Argument 2 - workingDir: this is the directory where the result will be saved.
    - Example: "/home/user1"
  - Example: java -jar log4jsearcher-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/special_user,/app,/tmp" "/home/user1"
- Results under the working folder
  - Example: /home/user1/result_1639592880328.csv
