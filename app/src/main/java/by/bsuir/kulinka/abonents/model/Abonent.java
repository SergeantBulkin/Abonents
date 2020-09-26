package by.bsuir.kulinka.abonents.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Abonent implements Parcelable
{
    //----------------------------------------------------------------------------------------------
    private Integer id;

    private String lastname;
    private String name;
    private int age;
    private String mobile_number;
    private int plan_id;
    private float balance;
    private String address;
    private String create_date;
    //----------------------------------------------------------------------------------------------
    //Конструкторы
    public Abonent(Integer id, String lastname, String name, int age, String mobile_number, int plan_id, float balance, String address, String create_date)
    {
        this.id = id;
        this.lastname = lastname;
        this.name = name;
        this.age = age;
        this.mobile_number = mobile_number;
        this.plan_id = plan_id;
        this.balance = balance;
        this.address = address;
        this.create_date = create_date;
    }

    public Abonent(String lastname, String name, int age, String mobile_number, int plan_id, float balance, String address, String create_date)
    {
        this.lastname = lastname;
        this.name = name;
        this.age = age;
        this.mobile_number = mobile_number;
        this.plan_id = plan_id;
        this.balance = balance;
        this.address = address;
        this.create_date = create_date;
    }

    public Abonent(Parcel parcel)
    {
        this.id = parcel.readInt();
        this.lastname = parcel.readString();
        this.name = parcel.readString();
        this.age = parcel.readInt();
        this.mobile_number = parcel.readString();
        this.plan_id = parcel.readInt();
        this.balance = parcel.readFloat();
        this.address = parcel.readString();
        this.create_date = parcel.readString();
    }
    //----------------------------------------------------------------------------------------------
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getMobile_number()
    {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number)
    {
        this.mobile_number = mobile_number;
    }

    public int getPlan_id()
    {
        return plan_id;
    }

    public void setPlan_id(int plan_id)
    {
        this.plan_id = plan_id;
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.id);
        dest.writeString(this.lastname);
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.mobile_number);
        dest.writeInt(this.plan_id);
        dest.writeFloat(this.balance);
        dest.writeString(this.address);
        dest.writeString(this.create_date);
    }
    //----------------------------------------------------------------------------------------------
    public static final Creator<Abonent> CREATOR = new Creator<Abonent>()
    {
        @Override
        public Abonent createFromParcel(Parcel source)
        {
            return new Abonent(source);
        }

        @Override
        public Abonent[] newArray(int size)
        {
            return new Abonent[size];
        }
    };

    public static Creator<Abonent> getCreator()
    {
        return CREATOR;
    }
    //----------------------------------------------------------------------------------------------
}

