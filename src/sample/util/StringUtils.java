package sample.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Pattern;

public class StringUtils {
  private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");


  public static String stripAccents(String input) {
    if (input == null) {
      return null;
    } else {
      StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Form.NFD));
      convertRemainingAccentCharacters(decomposed);
      return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll("");
    }
  }

  private static void convertRemainingAccentCharacters(StringBuilder decomposed) {
    for(int i = 0; i < decomposed.length(); ++i) {
      if (decomposed.charAt(i) == 321) {
        decomposed.deleteCharAt(i);
        decomposed.insert(i, 'L');
      } else if (decomposed.charAt(i) == 322) {
        decomposed.deleteCharAt(i);
        decomposed.insert(i, 'l');
      }
    }

  }

  public static String replaceChars(String str, char searchChar, char replaceChar) {
    return str == null ? null : str.replace(searchChar, replaceChar);
  }

  public static String removeAccents(String input) {
    if (input == null || input.length() < 1) {
      return "";
    }
    return (StringUtils.replaceChars(
        StringUtils.replaceChars(StringUtils.stripAccents(input), (char) 273, (char) 100),
        (char) 272, (char) 68));
  }

  public static boolean isEmpty(CharSequence cs){
    return cs == null || cs.length() == 0;
  }

  public static boolean isNotEmpty(CharSequence cs) {
    return !isEmpty(cs);
  }

  public static boolean isNotBlank(CharSequence cs) {
    return !isBlank(cs);
  }

  public static boolean isBlank(CharSequence cs) {
    int strLen = length(cs);
    if (strLen == 0) {
      return true;
    } else {
      for(int i = 0; i < strLen; ++i) {
        if (!Character.isWhitespace(cs.charAt(i))) {
          return false;
        }
      }

      return true;
    }
  }

  public static int length(CharSequence cs) {
    return cs == null ? 0 : cs.length();
  }
}
