package by.bsuir.kulinka.abonents.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Abonent
{
    //----------------------------------------------------------------------------------------------
    @PrimaryKey
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
}

