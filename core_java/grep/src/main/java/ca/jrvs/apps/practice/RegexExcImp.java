package ca.jrvs.apps.practice;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexExcImp extends RegexExc {
  @Override
  public boolean matchJpeg(String filename) {
    // Check if the filename ends with .jpeg or .jpg (case insensitive)
    Pattern pattern = Pattern.compile("\\.(?i)(jpeg|jpg)$");
    Matcher matcher = pattern.matcher(filename);
    return matcher.find();
  }

  @Override
  public boolean matchIp(String ip) {
    // Check if the given string is a valid IP address
    Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    Matcher matcher = pattern.matcher(ip);
    return matcher.matches();
  }

  @Override
  public boolean isEmptyLine(String line) {
    // Check if the given line is empty or contains only whitespace
    return line.trim().isEmpty();
  }
}
