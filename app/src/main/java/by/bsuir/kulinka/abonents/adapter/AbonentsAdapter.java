package by.bsuir.kulinka.abonents.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsuir.bottomapp.bar.abonents.R;
import by.bsuir.kulinka.abonents.model.Abonent;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AbonentsAdapter extends RecyclerView.Adapter<AbonentsAdapter.AbonentViewHolder>
{
    //----------------------------------------------------------------------------------------------
    private List<Abonent> abonents;
    private AbonentOnItemClickListener listener;
    //----------------------------------------------------------------------------------------------
    public AbonentsAdapter(List<Abonent> abonents, AbonentOnItemClickListener listener)
    {
        this.abonents = abonents;
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
        Abonent abonent = abonents.get(position);
        holder.abonentName.setText(String.format("%s %s", abonent.getLastname(), abonent.getName()));
        holder.abonentNumber.setText(abonent.getMobile_number());
        holder.abonentDate.setText(abonent.getCreate_date().toString());
        holder.bind(abonent, listener);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        if (abonents != null)
        {
            return abonents.size();
        }
        else return 0;
    }
    //----------------------------------------------------------------------------------------------
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
}
