package io.github.huang_chenyu.thosedays;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


import android.util.Log;

/**
 * Created by mickie on 11/25/17.
 */

public class MyQue<E> extends LinkedList<E> {
    private int size;
    private Counter<E> counter;

    public MyQue(int size) {
        this.size = size;
        this.counter = new Counter<E>();
    }

    public boolean append(E item) {

        super.add(item);
//        Update the counter.
        this.counter.add(item);

//      Used to maintain the size of the que.
        while (size() > this.size) {

            E removed = super.remove();

//            Update the counter
            this.counter.remove(removed);
        }

        return true;
    }

    public E findMax() {
        return this.counter.findMostCommon();
    }

    @Override
    public E peek() {
        return super.peekLast();
    }


    public void printAll() {
        for (int i = 0; i < this.size(); i++) {
           Log.d("MyQue", this.get(i).toString());
        }
    }

}


class Counter<E> {

    final HashMap<E, Integer> counts = new LinkedHashMap<>();


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