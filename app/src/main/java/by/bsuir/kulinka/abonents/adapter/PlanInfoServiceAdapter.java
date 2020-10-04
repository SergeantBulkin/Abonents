package by.bsuir.kulinka.abonents.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsuir.bottomapp.bar.abonents.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.bsuir.kulinka.abonents.model.Service;

public class PlanInfoServiceAdapter extends RecyclerView.Adapter<PlanInfoServiceAdapter.PlanInfoServiceViewHolder>
{
    //----------------------------------------------------------------------------------------------
    private List<Service> serviceList;
    private ServiceItemRemoveClickListener listener;
    //----------------------------------------------------------------------------------------------
    public PlanInfoServiceAdapter(List<Service> services, ServiceItemRemoveClickListener listener)
    {
        this.serviceList = services;
        this.listener = listener;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public PlanInfoServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_info_service, parent, false);
        return new PlanInfoServiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanInfoServiceViewHolder holder, int position)
    {
        Service service = serviceList.get(position);
        holder.serviceName.setText(service.getService_name());
        holder.serviceCost.setText(String.valueOf(service.getCost()));
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount()
    {
        if (serviceList != null)
        {
            return serviceList.size();
        }
        return 0;
    }
    //----------------------------------------------------------------------------------------------
    //MyViewHolder
    static class PlanInfoServiceViewHolder extends RecyclerView.ViewHolder
    {
        TextView serviceName;
        TextView serviceCost;
        ImageView iconDelete;

        public PlanInfoServiceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            serviceName = itemView.findViewById(R.id.item_plan_info_service_name);
            serviceCost = itemView.findViewById(R.id.item_plan_info_service_cost);
            iconDelete = itemView.findViewById(R.id.item_plan_info_service_icon_delete);
        }

        public void bind(int position, ServiceItemRemoveClickListener listener)
        {
            iconDelete.setOnClickListener(v -> listener.itemServiceRemoveClick(position));
        }
    }
    //----------------------------------------------------------------------------------------------
    public interface ServiceItemRemoveClickListener
    {
        void itemServiceRemoveClick(int position);
    }
    //----------------------------------------------------------------------------------------------
    public void addItem(Service service, int position)
    {
        this.serviceList.add(service);
        notifyItemInserted(position);
    }
    public void removeItem(int position)
    {
        this.serviceList.remove(position);
        notifyItemRemoved(position);
    }
    //----------------------------------------------------------------------------------------------
}
