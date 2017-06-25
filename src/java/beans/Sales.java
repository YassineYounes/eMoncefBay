package beans;

import java.io.Serializable;
import java.sql.Date;


public class Sales implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int SID;
    private int MID;
    private int AID; 
    private int finalPrice;
    private String name;

    public void setSID(int SID) {
        this.SID = SID;
    }

    public void setMID(int MID) {
        this.MID = MID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }
    
    public int getSID() {
        return SID;
    }

    public int getMID() {
        return MID;
    }

    public int getAID() {
        return AID;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
        
}
