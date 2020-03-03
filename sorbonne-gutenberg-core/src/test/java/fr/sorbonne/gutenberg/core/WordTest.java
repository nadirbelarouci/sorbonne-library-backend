package fr.sorbonne.gutenberg.core;

import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    void fromIndex() {
        System.out.println(Word.parse("spreading:4:5884-62,7793-16,8604-53,9521-12"));
    }
}