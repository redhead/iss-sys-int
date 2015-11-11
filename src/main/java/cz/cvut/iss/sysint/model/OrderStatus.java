package cz.cvut.iss.sysint.model;

import java.io.Serializable;

public class OrderStatus implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 49660196019150682L;
	private String resolution;
    private String description;

    public OrderStatus(String resolution, String description) {
        this.resolution = resolution;
        this.description = description;
    }

    public String getResolution() {
        return resolution;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "OrderStatus [resolution=" + resolution + ", description=" + description + "]";
    }
}
