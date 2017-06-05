package it.asilva;


import java.math.BigInteger;
import java.util.Arrays;

public class MyBigInt {

    final static long LONG_MASK = 0xffffffffL;
    private final String numString;
    private final BigInteger bigInteger;
    private final short[] digits;

    public MyBigInt(String numString) {
        this.numString = numString;
        this.bigInteger = new BigInteger(numString);
        digits = new short[numString.length()];
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (short) (numString.charAt(i) - '0');
        }
    }

    public MyBigInt multiplyNative(MyBigInt other) {
        final BigInteger result = this.bigInteger.multiply(other.bigInteger);
        return new MyBigInt(result.toString());
    }

    public short[] multiply(MyBigInt other) {
        return multiply(digits, other.digits);
    }

    @Override
    public String toString() {
        return numString;
    }

    public String digitsString() {
        return Arrays.toString(digits);
    }

    private static short[] multiply(short[] arr, short x) {
        short[] res = new short[arr.length];
        short carry = 0;
        for (int i = res.length - 1; i >= 0; i--) {
            int current = x * arr[i] + carry;
            res[i] = (short) (current % 10);
            carry = (short) (current / 10);
        }
        return enlargeIfNeeded(res, carry);
    }

    private static short[] add(short[] x, short[] y) {
        if (x.length == 0) {
            return y;
        }
        if (y.length == 0) {
            return x;
        }
        if (x.length < y.length) {
            short[] tmp = x;
            x = y;
            y = tmp;
        }
        short[] res = new short[x.length];
        short carry = 0;
        for (int i = x.length - 1; i >= x.length - y.length; i--) {
            short current = (short) (x[i] + y[i - (x.length - y.length)] + carry);
            carry = (short) (current / 10);
            res[i] = (short) (current % 10);
        }
        for (int i = x.length - y.length - 1; i >= 0; i--) {
            short current = (short) (x[i] + carry);
            carry = (short) (current / 10);
            res[i] = (short) (current % 10);
        }

        return enlargeIfNeeded(res, carry);
    }

    private static short[] multiply(short[] x, short[] y) {
        if (x.length == 0 || y.length == 0) {
            return new short[0];
        }
        if (x.length < y.length) {
            short[] tmp = x;
            x = y;
            y = tmp;
        }
        short[] res = new short[x.length];
        for (int i = y.length - 1; i >= 0; i--) {
            short[] partial = multiply(x, y[i]);
            partial = padRight(partial, y.length - i - 1);
            res = add(res, partial);
        }
        return res;
    }

    private static short[] enlargeIfNeeded(short[] res, short carry) {
        if (carry == 0) {
            return res;
        }
        short[] largerRes = new short[res.length + 1];
        largerRes[0] = carry;
        System.arraycopy(res, 0, largerRes, 1, res.length);
        return largerRes;
    }

    private static short[] padRight(short[] res, int padding) {
        short[] largerRes = new short[res.length + padding];
        for (int j = 1; j <= padding; j++) {
            largerRes[largerRes.length - j] = 0;

        }
        largerRes[largerRes.length - 1] = 0;
        System.arraycopy(res, 0, largerRes, 0, res.length);
        return largerRes;
    }

    public static void main(String[] args) {
        MyBigInt m1 = new MyBigInt("3141592653589793238462643383279502884197169399375105820974944592");
        MyBigInt m2 = new MyBigInt("2718281828459045235360287471352662497757247093699959574966967627");
        final MyBigInt result = m1.multiplyNative(m2);
        System.out.println(result);
        System.out.println(result.digitsString());
        final short[] multiply = m1.multiply(m2);
        System.out.println(Arrays.toString(multiply));
//

//        final MyBigInt myBigInt = new MyBigInt("12345");
//        System.out.println(myBigInt.calculatePaddedLength(1000));
//        System.out.println(Integer.MAX_VALUE << 2);

//        final short[] ints = multiply(new short[]{2, 5}, new short[]{1,1});
//        System.out.println(Arrays.toString(ints));
    }
}
