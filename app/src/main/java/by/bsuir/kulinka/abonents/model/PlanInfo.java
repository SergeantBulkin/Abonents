package by.bsuir.kulinka.abonents.model;

import java.util.List;

public class PlanInfo
{
    //----------------------------------------------------------------------------------------------
    private int plan_id;
    private String name;
    private List<Service> services;
    private float cost;
    //----------------------------------------------------------------------------------------------
    public PlanInfo(int plan_id, String name, List<Service> services, float cost)
    {
        this.plan_id = plan_id;
        this.name = name;
        this.services = services;
        this.cost = cost;
    }
    //----------------------------------------------------------------------------------------------
    public int getPlan_id()
    {
        return plan_id;
    }

    public void setPlan_id(int plan_id)
    {
        this.plan_id = plan_id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Service> getServices()
    {
        return services;
    }

    public void setServices(List<Service> services)
    {
        this.services = services;
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
