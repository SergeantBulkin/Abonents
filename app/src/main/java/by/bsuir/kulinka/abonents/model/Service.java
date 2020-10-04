package by.bsuir.kulinka.abonents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable
{
    //----------------------------------------------------------------------------------------------
    private int id;

    private String service_name;
    private float cost;
    //----------------------------------------------------------------------------------------------
    public Service(String service_name, float cost)
    {
        this.service_name = service_name;
        this.cost = cost;
    }

    protected Service(Parcel in)
    {
        this.id = in.readInt();
        this.service_name = in.readString();
        this.cost = in.readFloat();
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
    public static final Creator<Service> CREATOR = new Creator<Service>()
    {
        @Override
        public Service createFromParcel(Parcel in)
        {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size)
        {
            return new Service[size];
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
        dest.writeInt(id);
        dest.writeString(service_name);
        dest.writeFloat(cost);
    }
    //----------------------------------------------------------------------------------------------
}

