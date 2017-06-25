package beans;

import java.io.Serializable;
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int CID;
    private String description; 
    private String name;
    private String image;

    /**
     * @return the CID
     */
    public int getCID() {
        return CID;
    }

    /**
     * @param CID the CID to set
     */
    public void setCID(int CID) {
        this.CID = CID;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }
}
