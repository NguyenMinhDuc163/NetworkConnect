package controller;

import view.GameFrm;

/**
 *
 * @author admin
 */
public class SecondThread extends Thread{

    private GameFrm gf;
    private MinuteThread mtd;
    private int count;
    private boolean exit = false;

    public SecondThread(GameFrm gf, MinuteThread mtd) {
        super();
        this.gf = gf;
        this.mtd = mtd;
        count = 0;
    }

    @Override
    public void run() {
        while (!exit) {
            try {
                count++;
                if (count == 60) {
                    count = 0;
                    mtd.increase();
                }
                gf.setSecond(count);
                this.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
    public void stopp(){
        exit=true;
}
}
