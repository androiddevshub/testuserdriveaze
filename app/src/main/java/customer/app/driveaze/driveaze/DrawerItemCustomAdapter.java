package customer.app.driveaze.driveaze;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemCustomAdapter extends ArrayAdapter<DrawerNavItem> {

    Context mContext;
    int layoutResourceId;
    DrawerNavItem data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DrawerNavItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.navitemimage);
        TextView textViewName = (TextView) listItem.findViewById(R.id.navitemtitle);

        DrawerNavItem navItem = data[position];


        imageViewIcon.setImageResource(navItem.icon);
        textViewName.setText(navItem.title);

        return listItem;
    }
}
