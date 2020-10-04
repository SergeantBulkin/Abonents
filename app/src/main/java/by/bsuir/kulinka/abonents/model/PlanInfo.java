package by.bsuir.kulinka.abonents.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PlanInfo implements Parcelable
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

    public PlanInfo(String name, List<Service> services, float cost)
    {
        this.name = name;
        this.services = services;
        this.cost = cost;
    }

    protected PlanInfo(Parcel in)
    {
        this.plan_id = in.readInt();
        this.name = in.readString();
        this.services = in.createTypedArrayList(Service.CREATOR);
        this.cost = in.readFloat();
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
    public static final Creator<PlanInfo> CREATOR = new Creator<PlanInfo>()
    {
        @Override
        public PlanInfo createFromParcel(Parcel in)
        {
            return new PlanInfo(in);
        }

        @Override
        public PlanInfo[] newArray(int size)
        {
            return new PlanInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(plan_id);
        dest.writeString(name);
        dest.writeTypedList(services);
        dest.writeFloat(cost);
    }
    //----------------------------------------------------------------------------------------------
}
