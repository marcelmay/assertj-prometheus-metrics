package de.m3y.prometheus.assertj;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {
    @Test
    public void testSimiliar() {
        String[] candidates = StringUtils.similar("foo",
                new String[]{"afoo", "foobar", "abcfooefg", "afooba","afoob"},
                3);
        assertThat(candidates).containsExactly("afoo","afoob","foobar");

        // N larger than max values
        candidates = StringUtils.similar("foo",
                new String[]{"afoo", "foobar", "abcfooefg", "afooba","afoob"},
                10);
        assertThat(candidates).containsExactly("afoo","afoob","foobar","afooba", "abcfooefg");
    }
}
