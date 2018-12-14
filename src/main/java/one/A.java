package one;

import org.junit.Test;

import java.util.Random;

public class A {

    @Test
    public void test5() throws InterruptedException {
        while (true) {
            System.out.printf("随机数 = "+new Random().nextInt(2)+"\n");
            Thread.sleep(1000);
        }
    }
}
