package fr.sorbonne.gutenberg.core;

import java.util.ArrayList;
import java.util.List;

public class KMP {

    public static int[] kmpTable(String pattern) {
        int pos = 1, cnd = 0;
        int[] table = new int[pattern.length() + 1];

        table[0] = -1;

        while (pos < pattern.length()) {
            if (pattern.charAt(pos) == pattern.charAt(cnd)) {
                table[pos] = table[cnd];
            } else {
                table[pos] = cnd;
                cnd = table[cnd];
                while (cnd >= 0 && pattern.charAt(pos) != pattern.charAt(cnd)) {
                    cnd = table[cnd];
                }
            }
            pos = pos + 1;
            cnd = cnd + 1;
        }
        table[pos] = cnd;

        return table;
    }

    static List<Integer> matchAll(char[] factor, int[] retained, char[] text) {
        int index, p;
        List<Integer> pos = new ArrayList<>();
        p = match(0, factor, retained, text);
        while (p != -1) {
            pos.add(p);
            index = p + 1;
            p = match(index, factor, retained, text);
        }
        return pos;
    }

    private static int match(int pos, char[] factor, int[] retained, char[] text) {
        int i = pos;
        int j = 0;
        while (i < text.length) {
            if (j == factor.length) return i - factor.length;
            if (factor[j] == text[i]) {
                i++;
                j++;
            } else if (retained[j] == -1) {
                i++;
                j = 0;
            } else j = retained[j];
        }
        if (j == factor.length) return i - j;
        else return -1;
    }

}
