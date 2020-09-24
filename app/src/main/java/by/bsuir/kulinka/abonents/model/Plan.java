package by.bsuir.kulinka.abonents.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Plan
{
    //----------------------------------------------------------------------------------------------
    @PrimaryKey
    private int id;

    private String plan_name;
    //----------------------------------------------------------------------------------------------
    public Plan(String plan_name)
    {
        this.plan_name = plan_name;
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

    public String getPlan_name()
    {
        return plan_name;
    }

    public void setPlan_name(String plan_name)
    {
        this.plan_name = plan_name;
    }
    //----------------------------------------------------------------------------------------------
}


