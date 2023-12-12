package ca.jrvs.apps.practice;
import java.util.Scanner;
public class RegexExcImpTest {
    public static void main(String[] args) {
      RegexExcImp regexExcImp = new RegexExcImp();
      Scanner scanner = new Scanner(System.in);

      System.out.print("Enter a filename to check if it's a JPEG: ");
      String filename = scanner.nextLine();
      boolean isJpeg = regexExcImp.matchJpeg(filename);
      System.out.println("Is " + filename + " a JPEG file? " + isJpeg);

      System.out.print("\nEnter an IP address to validate: ");
      String ipAddress = scanner.nextLine();
      boolean isValidIp = regexExcImp.matchIp(ipAddress);
      System.out.println("Is " + ipAddress + " a valid IP address? " + isValidIp);

      System.out.print("\nEnter a line to check if it's empty: ");
      String line = scanner.nextLine();
      boolean isEmpty = regexExcImp.isEmptyLine(line);
      System.out.println("Is the line empty? " + isEmpty);

      scanner.close();
    }
  }



