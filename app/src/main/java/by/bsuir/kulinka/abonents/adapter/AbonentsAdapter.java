package by.bsuir.kulinka.abonents.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bsuir.bottomapp.bar.abonents.R;
import by.bsuir.kulinka.abonents.model.Abonent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AbonentsAdapter extends RecyclerView.Adapter<AbonentsAdapter.AbonentViewHolder> implements Filterable
{
    //----------------------------------------------------------------------------------------------
    private List<Abonent> abonents;
    private List<Abonent> filteredAbonents;
    private AbonentOnItemClickListener listener;
    //----------------------------------------------------------------------------------------------
    public AbonentsAdapter(List<Abonent> abonents, AbonentOnItemClickListener listener)
    {
        this.abonents = abonents;
        this.filteredAbonents = abonents;
        this.listener = listener;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public AbonentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_abonent_list, parent, false);
        return new AbonentViewHolder(v);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull AbonentViewHolder holder, int position)
    {
        Abonent abonent = filteredAbonents.get(position);
        holder.abonentName.setText(String.format("%s %s", abonent.getLastname(), abonent.getName()));
        holder.abonentNumber.setText(abonent.getMobile_number());
        holder.abonentDate.setText(fromSQLDate(abonent.getCreate_date()));
        holder.bind(abonent, listener);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        if (filteredAbonents != null)
        {
            return filteredAbonents.size();
        }
        else return 0;
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
                    filteredAbonents = abonents;
                } else
                {
                    List<Abonent> filteredList = new ArrayList<>();
                    for (Abonent abonent : abonents)
                    {
                        if (abonent.getName().toLowerCase().contains(charString.toLowerCase()) || abonent.getMobile_number().contains(charString))
                        {
                            filteredList.add(abonent);
                        }
                    }
                    filteredAbonents = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredAbonents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                filteredAbonents = (ArrayList<Abonent>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    //----------------------------------------------------------------------------------------------
    //MyViewHolder
    static class AbonentViewHolder extends RecyclerView.ViewHolder
    {
        TextView abonentName;
        TextView abonentNumber;
        TextView abonentDate;

        public AbonentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            abonentName = itemView.findViewById(R.id.item_abonent_name);
            abonentNumber = itemView.findViewById(R.id.item_abonent_number);
            abonentDate = itemView.findViewById(R.id.item_abonent_date);
        }

        void bind(Abonent abonent, AbonentOnItemClickListener clickListener)
        {
            itemView.setOnClickListener(v -> clickListener.abonentItemClicked(abonent));
        }
    }
    //----------------------------------------------------------------------------------------------
    public interface AbonentOnItemClickListener
    {
        void abonentItemClicked(Abonent abonent);
    }
    //----------------------------------------------------------------------------------------------
    //Форматировать дату из SQL формата
    private String fromSQLDate(String sqlDate)
    {
        String[] mas = sqlDate.split("-");
        return mas[2] + "." + mas[1] + "." + mas[0];
    }
    //----------------------------------------------------------------------------------------------
}
