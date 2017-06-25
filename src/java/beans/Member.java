package beans;

import java.io.Serializable;

public class Member {

    private int mid ; 
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String numT;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
	
    public int getMid(){
	return mid;
    }

    public void setLastName(String firstName) {
        this.lastName = firstName;
    }

    public void setFirstName(String lastName) {
        this.firstName = lastName;
    }

    public void setNumT(String numT) {
        this.numT = numT;
    }

    public String getNom() {
        return firstName;
    }

    public String getPrenom() {
        return lastName;
    }

    public String getNumT() {
        return numT;
    }

    public void setMid(int mid) {
	this.mid = mid;
    }
    
    public String getPassword() {
	return password;
    }



    public void setPassword(String password) {
	this.password = password;
    }

    public String getEmail() {
		return email;
    }



    public void setEmail(String email) {
		this.email = email;
    }
	

}
