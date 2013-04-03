package br.usp.ime.arranger.behaviors;

import java.util.Random;

public class MemoryBehavior extends AbstractBehavior {

    private int size;

    @Override
    public void run() throws BehaviorException {
        consumesSquaredSizeArray();
    }

    public void setSize(int newSize) {
        size = newSize;
    }

    public int getSize() {
        return size;
    }

    private float[] generateRandomArray(float[] v) {
        Random r;
        int i, length;
        length = v.length;
        r = new Random();
        for (i = 0; i < length; i++) {
            v[i] = (float) r.nextDouble();
        }
        return v;
    }

    public float[] consumesSquaredSizeArray() {
        int i, sizeSquare;
        sizeSquare = size * size;
        float[] v = new float[sizeSquare];
        v = generateRandomArray(v);
        for (i = 0; i < sizeSquare; i++)
            v[i] *= -1.0f;
        return v;
    }
}