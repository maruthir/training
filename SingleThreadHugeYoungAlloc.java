package mydomain;

import java.net.URL;

/**
 * Single thread app allocates a lot of young objects with almost nothing going to old gen
 */
public class SingleThreadHugeYoungAlloc {

    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        int k = 0;
        for (int i = 0; i < 100000000; i++) {
            String temp = new String("Str" + i);
            k += temp.length();
        }
        for (int i = 0; i < 100000000; i++) {
            URL u = new URL("http://something.co" + i);
            k += u.getPort();
        }
        System.out.println("Duration : " + (System.currentTimeMillis() - start));
        Thread.sleep(300000);
    }
}
