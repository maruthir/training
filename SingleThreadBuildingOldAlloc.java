package com.mydomain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Single thread app allocates a lot of young objects with almost nothing going to old gen
 */
public class SingleThreadBuildingOldAlloc {

    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        List<String> strList = new ArrayList<>();
        List<URL> urlList = new ArrayList<>();
        for (int i = 0; i < 100000000; i++) {
            strList.add("Str" + i);
        }
        for (int i = 0; i < 100000000; i++) {
            URL u = new URL("http://something.co" + i);
            urlList.add(u);
        }
        System.out.println("Duration : " + (System.currentTimeMillis() - start));
    }
}
