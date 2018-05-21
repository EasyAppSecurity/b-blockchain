package net.easyappsec.merkletree.factory.util;

import java.util.Iterator;

public class Iterators {

    private Iterators() {
    }

    public static int count(Iterator iterator) {
        if (iterator == null) {
            return 0;
        }
        int counter = 0;
        while (iterator.hasNext()) {
            counter++;
            iterator.next();
        }
        return counter;
    }
}
