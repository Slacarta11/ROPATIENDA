package model;

import java.sql.Date;

public class Category {
    private int id;
    private String name;
    private String description;
    private float discountPercentage;
    private Date creationDate;
    private boolean isActive;
    private String image;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(float discountPercentage) { this.discountPercentage = discountPercentage; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}