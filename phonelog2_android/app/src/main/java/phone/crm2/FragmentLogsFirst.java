package phone.crm2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import phone.crm2.model.Event;

public class FragmentLogsFirst extends ListFragment {

    private PhoneCallLArrayAdapter adapter;
    private Spinner spinner;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logs_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNavigation(view);
        //final String[] items = {"aaaa","bbbb","cccc"};
        //final ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items);
        initSpinner();
        logsSelected();
        //setListAdapter(aa);
    }

    private void initSpinner() {
        spinner = this.getActivity().findViewById(R.id.bdd_spinner2);
        @SuppressWarnings("rawtypes")
        List lisBgCalendarsSelected = this.getApplicationBg().getListCalendarsSelected();
        Object storageDefault = this.getApplicationBg().getStorage();
        int positionSelected = 0;
        int i = 0;
        for (Object o : lisBgCalendarsSelected) {
            if (o.equals(storageDefault)) {
                positionSelected = i;
            }
            i++;
        }

        ArrayAdapter<Object> adapterComboBox = new ArrayAdapter<Object>(this.getActivity(), android.R.layout.simple_spinner_item, lisBgCalendarsSelected.toArray());
        spinner.setAdapter(adapterComboBox);
        spinner.setSelection(positionSelected);
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                logsSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        };
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        // Specify the layout to use when the list of choices appears
        adapterComboBox.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    private ApplicationBg getApplicationBg() {
        ApplicationBg applicationBg = (ApplicationBg) this.getActivity().getApplication();
        return applicationBg;
    }

    private void logsSelected() {

        Object o = spinner.getSelectedItem();
        BgCalendar bgCalendar = (BgCalendar) o;
        int page = 0;
        List<Event> communicationList = UtilCalendar.getListEvent(this.getActivity(), bgCalendar, page);
        initListDisplayed(communicationList);
        ApplicationBg applicationBg = getApplicationBg();
        applicationBg.setStorage(bgCalendar);
    }

    private void initListDisplayed(List<Event> listPhoneCAll) {
        if (this.adapter == null) {
            this.adapter = new PhoneCallLArrayAdapter(this.getActivity(), listPhoneCAll);
            setListAdapter(this.adapter);

        } else {
            this.adapter.getListEvents().clear();
            this.adapter.getListEvents().addAll(listPhoneCAll);

        }
        this.adapter.notifyDataSetChanged();

    }


    private void initNavigation(View view) {
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentLogsFirst.this)
                        .navigate(R.id.action_navigation_to_FragmentSecond);
            }
        });
    }
}