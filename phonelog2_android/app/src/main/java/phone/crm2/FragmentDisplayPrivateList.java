package phone.crm2;

import android.app.AlertDialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import phone.crm2.model.Contact;

public class FragmentDisplayPrivateList extends ListFragment {

    private ApplicationBg applicationBg;
    private ListView listView;
    private TextView textViewNbPrivate;
    private List<Contact> listPrivateContacts;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_private_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applicationBg = (ApplicationBg) this.getActivity().getApplication();
        textViewNbPrivate = (TextView) view.findViewById(R.id.textViewNbNumeros);
        listView = (ListView) view.findViewById(android.R.id.list);
        initList();
        Button buttonClearList = (Button) view.findViewById(R.id.buttonClearBlackList);
        View.OnClickListener clicListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();

            }
        };
        buttonClearList.setOnClickListener(clicListener);

    }

    public void clear() {
        Log.i("bg2","FragmentDisplayPrivateList clear  size: "+listPrivateContacts.size());

        for(Contact c : this.listPrivateContacts){
            c.setPrivate(false);
            Contact c2 = applicationBg.getDb().getContact().update(c);
            Log.i("bg2","FragmentDisplayPrivateList   clear "+c2);
        }

        this.initList();
    }


    private void initList() {
        listPrivateContacts = applicationBg.getDb().getContact().getAllPrivate();
        //listNumero.addAll(applicationBg.gethPrivateList().values());
        String label = " " + listPrivateContacts .size() + " Blacklisted";
        textViewNbPrivate.setText(label);

        List<String> listNumber = new ArrayList<String>();
        for(Contact c : listPrivateContacts){
            listNumber.add(c.getNumber());
        }
        ArrayAdapter<String> baseAdapter = new FragmentDisplayPrivateList.StableArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, listNumber);
        listView.setAdapter(baseAdapter);
    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        private final Context contextBg;
        public StableArrayAdapter(Context context, int textViewResourceId, List<String> list) {
            super(context, textViewResourceId, list);
            this.contextBg = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) contextBg.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_list_private, parent, false);
            TextView textView =(TextView) rowView.findViewById(R.id.labelPrivateItem);
            Contact contact = listPrivateContacts.get(position);
            textView.setText(contact.getContactNumbberAndNAme());
            Button button = (Button)  rowView.findViewById(R.id.buttonPrivateItemRemove);
            View.OnClickListener onClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    remove(position);
                }
            };
            button.setOnClickListener(onClickListener);
            //TODO
//			textView.setText(getApplicationBg().gethPrivateList().get(number));

            return rowView;
        }


        protected void updateNb() {
            Log.w("bg","FragmentDisplayList No implemented yet");
        }

        private void remove(int position){
            Log.i("bg2","Remove position"+position+"  ");
            Contact contact = listPrivateContacts.get(position);
            contact.setPrivate(false);
            Contact c2 = applicationBg.getDb().getContact().update(contact);
            Log.i("bg2","Remove"+c2);

            initList();
            Log.i("bg2","Remove listPrivateContacts.size "+listPrivateContacts.size());
            updateNb();
            super.notifyDataSetChanged();
        }
    }

}