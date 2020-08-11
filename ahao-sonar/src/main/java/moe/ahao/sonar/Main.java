package moe.ahao.sonar;

public class Main {

    public int add(Integer a, Integer b) {
        if(a == null && b == null) {
            return 0;
        } else if(a == null) {
            return b;
        } else if(b == null) {
            return a;
        } else {
            return a + b;
        }
    }
}
