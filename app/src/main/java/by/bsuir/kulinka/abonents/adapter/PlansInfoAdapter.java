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
import by.bsuir.kulinka.abonents.model.Plan;
import by.bsuir.kulinka.abonents.model.PlanInfo;

public class PlansInfoAdapter extends RecyclerView.Adapter<PlansInfoAdapter.PlanViewHolder> implements Filterable
{
    //----------------------------------------------------------------------------------------------
    private List<PlanInfo> plansInfo;
    private List<PlanInfo> filteredPlansInfo;
    private PlanOnItemClickListener listener;
    //----------------------------------------------------------------------------------------------
    public PlansInfoAdapter(List<PlanInfo> plansInfo, PlanOnItemClickListener listener)
    {
        this.plansInfo = plansInfo;
        this.filteredPlansInfo = plansInfo;
        this.listener = listener;
    }

    public PlansInfoAdapter(PlanOnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void setPlansInfo(List<PlanInfo> plansInfo)
    {
        this.plansInfo = plansInfo;
        this.filteredPlansInfo = plansInfo;
        notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_list, parent, false);
        return new PlanViewHolder(v);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position)
    {
        PlanInfo planInfo = filteredPlansInfo.get(position);
        holder.planName.setText(planInfo.getName());
        holder.planCost.setText(String.valueOf(planInfo.getCost()));
        holder.bind(planInfo, listener);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        if (filteredPlansInfo != null)
        {
            return filteredPlansInfo.size();
        }
        else return 0;
    }

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
                    filteredPlansInfo = plansInfo;
                } else
                {
                    List<PlanInfo> filteredList = new ArrayList<>();
                    for (PlanInfo planInfo : plansInfo)
                    {
                        if (planInfo.getName().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(planInfo);
                        }
                    }
                    filteredPlansInfo = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPlansInfo;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                filteredPlansInfo = (ArrayList<PlanInfo>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //----------------------------------------------------------------------------------------------
    //MyViewHolder
    static class PlanViewHolder extends RecyclerView.ViewHolder
    {
        TextView planName;
        TextView planCost;

        public PlanViewHolder(@NonNull View itemView)
        {
            super(itemView);
            planName = itemView.findViewById(R.id.item_plan_name);
            planCost = itemView.findViewById(R.id.item_plan_cost);
        }

        public void bind(PlanInfo planInfo, PlanOnItemClickListener planOnItemClickListener)
        {
            itemView.setOnClickListener(v -> planOnItemClickListener.planItemClicked(planInfo));
        }
    }
    //----------------------------------------------------------------------------------------------
    public interface PlanOnItemClickListener
    {
        void planItemClicked(PlanInfo planInfo);
    }
    //----------------------------------------------------------------------------------------------
}
