package net.easyappsec.merkletree.util;

public class MathUtil {

    private MathUtil() {
    }

    public static int log(int x, int base) {
        return (int) (Math.log(x) / Math.log(base));
    }

}
