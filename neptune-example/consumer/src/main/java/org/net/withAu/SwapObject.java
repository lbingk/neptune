package org.net.withAu;

import lombok.Data;

@Data
public class SwapObject {
    Object a;
    Object b;

    void swap(Object a, Object b) {
        Object temp = null;
        temp = a;
        a = b;
        b = temp;
        System.out.println(a);
        System.out.println(b);
    }

    public static void main(String[] args) {
        SwapObject swapObject = new SwapObject();
        swapObject.setA("a");
        swapObject.setB("b");

        Object a1 = swapObject.getA();
        Object b1 = swapObject.getB();
        System.out.println(a1);
        System.out.println(b1);

        swapObject.swap(a1, b1);
        System.out.println(a1 +"---"+ b1);

    }
}
