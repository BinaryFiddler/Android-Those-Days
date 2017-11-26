package io.github.huang_chenyu.thosedays;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mickie on 11/25/17.
 */

public class Counter<E> {

    final HashMap<E, Integer> counts = new HashMap<>();

    protected boolean add(E item) {

        if ( counts.containsKey(item) ) {
            int val = counts.get(item);
            val += 1;
            counts.put(item, val);
        } else {
            counts.put(item, 1);
        }
        return true;
    }

    protected boolean remove ( E item ) {
        if (counts.containsKey((item))) {

            int val = counts.get(item);

            val -= 1;

            if ( val == 0 ) {

                counts.remove(item);

            } else {

                counts.put(item, val);

            }
            return true;

        } else {

            System.err.print("The item doesn't exist in the counter.");

            return false;

        }
    }

    protected E findMostCommon() {

        int maxVal = 0;

        E item = null;

        for (Map.Entry<E, Integer> entry : counts.entrySet()){

            int val = entry.getValue();

            if ( val > maxVal ) {

                item = entry.getKey();

                maxVal = val;

            }

        }

        return item;

    }
}
