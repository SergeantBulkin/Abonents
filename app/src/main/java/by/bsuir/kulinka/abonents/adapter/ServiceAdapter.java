package by.bsuir.kulinka.abonents.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bsuir.bottomapp.bar.abonents.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.bsuir.kulinka.abonents.model.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> implements Filterable
{
    //----------------------------------------------------------------------------------------------
    private List<Service> services;
    private List<Service> filteredServices;
    private ServiceOnItemClickListener listener;
    //----------------------------------------------------------------------------------------------
    public ServiceAdapter(ServiceOnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setServices(List<Service> services)
    {
        this.services = services;
        this.filteredServices = services;
        notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_list, parent, false);
        return new ServiceViewHolder(v);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position)
    {
        Service service = filteredServices.get(position);
        holder.serviceName.setText(service.getService_name());
        holder.serviceCost.setText(String.valueOf(service.getCost()));
        holder.bind(service, listener);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        if (filteredServices != null)
        {
            return filteredServices.size();
        }
        return 0;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charSequence.equals(""))
                {
                    filteredServices = services;
                } else
                {
                    List<Service> filteredList = new ArrayList<>();
                    for (Service service : services)
                    {
                        if (service.getService_name().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(service);
                        }
                    }
                    filteredServices = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredServices;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                filteredServices = (ArrayList<Service>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    //----------------------------------------------------------------------------------------------
    //MyViewHolder
    static class ServiceViewHolder extends RecyclerView.ViewHolder
    {
        TextView serviceName;
        TextView serviceCost;

        public ServiceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            serviceName = itemView.findViewById(R.id.item_service_name);
            serviceCost = itemView.findViewById(R.id.item_service_cost);
        }

        public void bind(Service service, ServiceOnItemClickListener serviceOnItemClickListener)
        {
            itemView.setOnClickListener(v -> serviceOnItemClickListener.serviceItemClicked(service));
        }
    }
    //----------------------------------------------------------------------------------------------
    public interface ServiceOnItemClickListener
    {
        void serviceItemClicked(Service service);
    }
    //----------------------------------------------------------------------------------------------
}
