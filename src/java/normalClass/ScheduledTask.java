/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package normalClass;
import dao.CheckerDAOImpl;
import dao.DAOFactory;
import java.util.TimerTask;
/**
 *
 * @author bbrayek
 */
public class ScheduledTask extends TimerTask {
    
    private final CheckerDAOImpl dao;

    public ScheduledTask() {
        System.out.println("ScheduledTask initiation");
        this.dao =(CheckerDAOImpl) DAOFactory.getInstance().getCheckerDAO();
        System.out.println("ScheduledTask initiatiated");
    }
    
    
    
    @Override
    public void run() {
        dao.check();
    }
    
}
