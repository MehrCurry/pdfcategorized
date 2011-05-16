package de.gzockoll.pdfcategorizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringMatcherTest
{
  public static void main(String[] args)
  {
    String regexString = ".*\\d{3}\\.\\d+\\.\\d+\\.\\d+.*";
    String spamString = "visit our site\\n at http://010.1.1.1/. \\nmore poop here.";

    Pattern aPattern =
Pattern.compile(regexString,Pattern.MULTILINE);
    Matcher aMatcher = aPattern.matcher(spamString);
    if (aMatcher.find())
    {
      System.out.println("got a match");
    }
    else
    {
      System.out.println("no match");
    }
  }
}