package thesis.nars.lig.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import thesis.nars.lig.Model.MessageModel;
import thesis.nars.lig.R;


public class MessageListAdapter extends ArrayAdapter<MessageModel> {
    Context context;
    ArrayList<MessageModel> messagelist;
    int resource;
    String userID;
    String senderName;


    private final static int LEFT_MESSAGE = 0;
    private final static int RIGHT_MESSAGE = 1;
    private DatabaseReference databaseReference;
    private String status;
    private String test;


    public MessageListAdapter(Context context, int resource, ArrayList<MessageModel> objects, String userID, String senderName) {
        super(context, resource, objects);
        this.context = context;
        messagelist = objects;
        this.resource = resource;
        this.userID = userID;
        this.senderName = senderName;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder viewHolder;
        int viewType = this.getItemViewType(position);
        final MessageModel messageModel = messagelist.get(position);

        test = messageModel.getSenderName();
        if(convertView== null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            viewHolder = new ViewHolder();
            switch (viewType) {
                case LEFT_MESSAGE:
                    layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.incoming_chat_item, parent, false);
                    viewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text_Txt);
                    viewHolder.senderName = (TextView) convertView.findViewById(R.id.message_senderName);
                    break;

                case RIGHT_MESSAGE:
                    layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.out_going_chat_item, parent, false);
                    viewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text_Txt);
                    viewHolder.senderName = (TextView) convertView.findViewById(R.id.message_senderName);
                    break;
            }
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (viewType) {
            case LEFT_MESSAGE:
                viewHolder.messageText.setText(messageModel.getMessageTxt());
                viewHolder.senderName.setText(messageModel.getSenderName());
            case RIGHT_MESSAGE:
                viewHolder.messageText.setText(messageModel.getMessageTxt());
        }


        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {

        MessageModel message = messagelist.get(position);
        if(message.getSenderID().equals(userID)){
            return RIGHT_MESSAGE;
        }
        else {
            return LEFT_MESSAGE;
        }
    }

    public static class ViewHolder{
        TextView messageText;
        ImageView downloadIcon;
        TextView senderName;
        TextView iv_isActive;
    }
}