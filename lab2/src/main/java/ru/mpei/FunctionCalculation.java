package ru.mpei;

public class FunctionCalculation {

    static float y;

    public static float squaredPolynomial(float x){
        y = (float) (-0.5 * Math.pow(x, 2) - 4);
        return y;
    }
    public static float Polynomial(float x){
        y = (float) (Math.pow(2, -0.1 * x));
        return y;
    }
    public static float cos(float x){
        y = (float) Math.cos(x);
        return y;
    }
}