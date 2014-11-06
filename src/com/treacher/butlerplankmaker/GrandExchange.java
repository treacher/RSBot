package com.treacher.butlerplankmaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by treach3r on 6/11/14.
 */
public class GrandExchange {


    public static int getPrice(final int id) {
        try {
            final URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
            final URLConnection con = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            String price;

            while ((line = in.readLine()) != null) {
                if (line.contains("mark_price")) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(line);
                    m.find();
                    System.out.println(m.group(0));
                    System.out.println(m.group());
                    return Integer.parseInt(m.group(0));
                }
            }
        } catch (final Exception ignored) {
            return 0;
        }
        return 0;
    }
}