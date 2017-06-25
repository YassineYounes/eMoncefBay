package beans;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int AID;
    private String description; 
    private String title; 
    private Date deadline ; 
    private String image; 
    private int CID ; 
    private int betPrice; 
    private int directPrice; 
    private int Mid; 

    public int getMid() {
        return Mid;
    }


    public int getAID() {
        return AID;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getImage() {
        return image;
    }

    public int getCID() {
        return CID;
    }

    public int getBetPrice() {
        return betPrice;
    }

    public int getDirectPrice() {
        return directPrice;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCID(int CID) {
        this.CID = CID;
    }

    public void setBetPrice(int betPrice) {
        this.betPrice = betPrice;
    }

    public void setDirectPrice(int directPrice) {
        this.directPrice = directPrice;
    }

    public void setMid(int Mid) {
        this.Mid = Mid;
    }
    
}
