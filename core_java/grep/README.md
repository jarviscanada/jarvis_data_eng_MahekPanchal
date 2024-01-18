# Introduction
The Java grep app mimics the Linux "grep" command which allows users to search for a text pattern recursively in a given directory, and output matched lines to a file. The app takes three arguments: 1) regex 2) rootPath 3) outFile.

# Quick Start
1) Using jar file: 
outfile=grep_$(date +%F_%T).txt
java -jar grep-demo.jar  ".*Romeo.*Juliet.*" "./data/txt" ./out/${outfile}
cat out/$outfile

2) Using classpath: 
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* ./data/txt ./out/grep.txt

#Implementation
I established a project structure adhering to Java coding standards and organized three files with corresponding dependencies in my project. After downloading a text file to the designated data directory, I integrated the SLF4J logging framework for efficient message logging. I configured the classpath and utilized the Maven Shade Plugin to generate an executable JAR file for the project. To conclude, I dockerized the application, ensuring its seamless deployment and execution.

## Pseudocode
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)

## Performance Issue
As a user if I want to use this program to process a 50GB file I can do that by: 
java -Xms5m -Xmx50m ,where -Xms5m sets the initial heap size to 5MB & Xmx50m sets the maximum heap size to 50MB.

# Test
1. Set Breakpoints
2. Start Debugging Session
3. Control Debugging Session
4. Inspect Variables and State
5. Resume or Stop Debugging

# Deployment

1. Create a file named Dockerfile (without any file extension) which contains instructions to build the Docker image.
2. Built the docker image using: docker build -t ${docker_user}/grep
3. Verify the created image by typing the docker images command.

# Improvement
1. In the app, regex is already specified, but it can be improved as a dynamic input given by the user.
2. If user inputs are considered then they should be validated to prevent SQL injections or Cross-site Scripting(XSS).
3. Caching mechanisms, where appropriate,should be implemented to reduce redundant computations and improve response times.
