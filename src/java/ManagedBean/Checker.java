/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;


import normalClass.ScheduledTask;
import java.util.Timer;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author bbrayek
 */
@ManagedBean(name = "Checker",eager=true)
@ApplicationScoped
public class Checker {

    /**
     * Creates a new instance of Checker
     */
    public Checker() {
        System.out.println("checker initiation");
        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
        time.schedule(st, 0, 86400000); // Create Repetitively task for every 1 day 86400000
        System.out.println("ScheduledTask initiatiated");
    }
    
}
