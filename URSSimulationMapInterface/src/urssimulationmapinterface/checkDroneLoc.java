

package urssimulationmapinterface;

import java.util.*;
public class checkDroneLoc extends Thread {

    static private URSSimulationMapInterface xxx = null;
    public void run() {
        
        try {
        while(!Thread.interrupted())
        {
            
            xxx.readMsgs();
        //    try {
                sleep(200);
        }
        } catch (InterruptedException e) {
            System.out.println("2222*****************************22222");
            }
    //    }
    }
    /*public void run() {
       // System.out.println("Timer task executed.");
    try {  
xxx.readMsgs();
    }
    catch(Exception e)
    {
        xxx.readMsgs();
    }
}*/    

}


