package fr.sorbonne.gutenberg;

import fr.sorbonne.gutenberg.core.NFA;
import org.junit.jupiter.api.Test;

public class DigraphTest {

    @Test
    public void graph() {
        NFA nfa = new NFA("((d|a)*|b)*c*");
        System.out.println(nfa.recognizes("bbdadadaaa"));
        System.out.println(nfa);
    }
}
