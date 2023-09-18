package controller;

import view.GameFrm;

/**
 *
 * @author admin
 */
public class MinuteThread extends Thread {

    private GameFrm gf;
    private HourThread htd;
    private int count;

    public MinuteThread(GameFrm gf, HourThread htd) {
        super();
        this.gf = gf;
        this.htd = htd;
        count = 0;
    }

    public void increase() {
        count++;
        if (count == 60) {
            htd.increase();
            count = 0;
        }
        gf.setMinute(count);
    }

    @Override
    public void run() {
        while (true) {
            try {
                gf.setMinute(count);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
}
