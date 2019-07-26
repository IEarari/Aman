package ibraheem.illdev.sos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCasesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cases> cases_array;

    public UserCasesAdapter (Context context, ArrayList<Cases> cases_array) {
        this.context = context;
        this.cases_array = cases_array;
    }

    @Override
    public int getCount() {
        return cases_array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.cases_list_item,parent, false);

        TextView Name = convertView.findViewById(R.id.case_name);
        if (cases_array.get(position).getDep().equals("100")) {
            Name.setText("Police");
        }else if (cases_array.get(position).getDep().equals("101")){
            Name.setText("Ambulance");
        }
        else {
            Name.setText("Fire Department");
        }
        return convertView;
    }
}
