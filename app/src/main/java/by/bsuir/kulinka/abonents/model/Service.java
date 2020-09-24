package by.bsuir.kulinka.abonents.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Service
{
    //----------------------------------------------------------------------------------------------
    @PrimaryKey
    private int id;

    private String service_name;
    private float cost;
    //----------------------------------------------------------------------------------------------
    public Service(String service_name, float cost)
    {
        this.service_name = service_name;
        this.cost = cost;
    }
    //----------------------------------------------------------------------------------------------
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getService_name()
    {
        return service_name;
    }

    public void setService_name(String service_name)
    {
        this.service_name = service_name;
    }

    public float getCost()
    {
        return cost;
    }

    public void setCost(float cost)
    {
        this.cost = cost;
    }
    //----------------------------------------------------------------------------------------------
}

