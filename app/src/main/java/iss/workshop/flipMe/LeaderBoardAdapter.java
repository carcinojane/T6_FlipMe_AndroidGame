package iss.workshop.flipMe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LeaderBoardAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<LeaderBoardPlayers> playersList;


    public LeaderBoardAdapter(@NonNull Context context, int resource, List<LeaderBoardPlayers> playersList) {
        super(context, resource, playersList);

        this.context = context;
        this.playersList = playersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder;
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = inflater.inflate(R.layout.leader_board_list_item, null);
            convertView.setOnClickListener(null);
            holder = new viewHolder();
            holder.rankView = (TextView) convertView.findViewById(R.id.itemRank);
            holder.nameView = (TextView) convertView.findViewById(R.id.itemName);
            holder.scoreView = (TextView) convertView.findViewById(R.id.itemScore);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        int rank = (playersList.get(position).getPlayerRank());
        String name = (playersList.get(position).getPlayerName());
        int score = (playersList.get(position).getPlayerScore());

        holder.rankView.setText(String.valueOf(rank));
        holder.nameView.setText(String.valueOf(name));
        holder.scoreView.setText(String.valueOf(score));


        return convertView;
    }

    static class viewHolder {
        TextView rankView;
        TextView nameView;
        TextView scoreView;
    }
}
