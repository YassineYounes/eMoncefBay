package beans;

import java.io.Serializable;

public class Bet implements Serializable {
    private static final long serialVersionUID = 1L;
    private int bid;
    private int value;
    private int MID;
    private int AID;

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMID(int MID) {
        this.MID = MID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public int getBid() {
        return bid;
    }

    public int getValue() {
        return value;
    }

    public int getMID() {
        return MID;
    }

    public int getAID() {
        return AID;
    }
}
