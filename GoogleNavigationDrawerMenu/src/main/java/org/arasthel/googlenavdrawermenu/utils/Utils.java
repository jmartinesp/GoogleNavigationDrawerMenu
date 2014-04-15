package org.arasthel.googlenavdrawermenu.utils;

/**
 * Created by Arasthel on 15/04/14.
 */
public class Utils {

    public static String[] convertToStringArray(CharSequence[] charSequences) {
        if(charSequences == null) {
            return null;
        }

        if (charSequences instanceof String[]) {
            return (String[]) charSequences;
        }

        String[] strings = new String[charSequences.length];
        for (int index = 0; index < charSequences.length; index++) {
            strings[index] = charSequences[index].toString();
        }

        return strings;
    }

}
