package org.ace;

public class Test {
    public static void main(String[] args) {
        String value = "demo,28,female";
        String arr_value[] = value.toString().split(",");
        System.out.println(arr_value.length);
    }
}
