package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep{

final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

private String regex;
private String rootPath;
private String outFile;

  public Logger getLogger() {
    return logger;
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  public List<File> listFiles(String rootDir) {
    // Implement listing files in the root directory
    File root = new File(rootDir);
    List<File> fileList = new ArrayList<>();
    if (root.exists() && root.isDirectory()) {
      File[] files = root.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            fileList.add(file);
          }
        }
      }
    }
    return fileList;
  }

  public List<String> readLines(File inputFile) {
    // Implement reading lines from a file
    Path path = inputFile.toPath();
    try {
      return Files.readAllLines(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public boolean containsPattern(String line) {
    // Implement pattern matching logic here
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    return matcher.find(); // Return true if pattern is found in the line
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    // Implement writing lines to a file here
    File outputFile = new File(outFile);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
      for (String line : lines) {
        writer.write(line);
        writer.newLine();
      }
    }
  }


  @Override
  public void process() throws IOException {
    List<File> files = listFiles(rootPath);
    List<String> matchedLines = new ArrayList<>();

    for (File file : files) {
      List<String> lines = readLines(file);
      for (String line : lines) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }

    writeToFile(matchedLines);
  }


  @Override
  public String getRootPath() {
    return rootPath;
  }
  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }
  @Override
  public String getOutFile() {
    return outFile;
  }
  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
  public static void main(String[] args) {
    if(args.length !=3)
    {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

//    BasicConfigurator.configure();
    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try{
      javaGrepImp.process();
    } catch (Exception ex){
     javaGrepImp.logger.error("Error: Unable to Proceed", ex);
    }

  }
}










