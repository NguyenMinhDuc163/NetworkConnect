package controller;

import view.GameFrm;

/**
 *
 * @author admin
 */
public class HourThread extends Thread{
    private GameFrm gf;
    private int count;
    
    public HourThread(GameFrm gf){
        super();
        this.gf = gf;
        count = 0;
    }
    
    public void increase(){
        count++;
        gf.setHour(count);
    }
    
    @Override
    public void run(){
        while(true){
            try{
                gf.setHour(count);
            }catch(Exception e){
                System.out.println(e.getStackTrace());
            }
        }
    }
}
