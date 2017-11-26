package io.github.huang_chenyu.thosedays;

import java.util.LinkedList;

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

    public E peek() {
        return super.peek();
    }

    public void printAll() {
        for (int i = 0; i < this.size(); i++) {
            System.out.println(this.get(i));
        }
    }

}
