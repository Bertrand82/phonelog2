package phone.crm2;

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
import androidx.fragment.app.ListFragment;

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
        textViewNbPrivate = view.findViewById(R.id.textViewNbNumeros);
        listView = view.findViewById(android.R.id.list);
        initList();
        Button buttonClearList = view.findViewById(R.id.buttonClearBlackList);
        View.OnClickListener clicListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();

            }
        };
        buttonClearList.setOnClickListener(clicListener);

    }

    public void clear() {
        for(Contact c : this.listPrivateContacts){
            c.setPrivate(false);
            Contact c2 = applicationBg.getDb().getContact().update(c);
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
            TextView textView = rowView.findViewById(R.id.labelPrivateItem);
            Contact contact = listPrivateContacts.get(position);
            textView.setText(contact.getContactNumbberAndNAme());
            Button button = rowView.findViewById(R.id.buttonPrivateItemRemove);
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
            Contact contact = listPrivateContacts.get(position);
            contact.setPrivate(false);
            Contact c2 = applicationBg.getDb().getContact().update(contact);

            initList();
            updateNb();
            super.notifyDataSetChanged();
        }
    }

}