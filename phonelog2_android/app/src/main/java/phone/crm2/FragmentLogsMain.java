package phone.crm2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.List;

import phone.crm2.model.Event;

public class FragmentLogsMain extends Fragment implements AdapterView.OnItemClickListener{

    private PhoneCallLArrayAdapter adapter;
    private Spinner spinner;
    private BgCalendar bgCalendar;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logs_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        Object storageDefault = this.getApplicationBg().getStorageCalendar();
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
        this.bgCalendar = (BgCalendar) o;
        List<Event> communicationList = UtilCalendar.getListEvent(this.getActivity(), bgCalendar, page);
        initListDisplayed(communicationList);
        ApplicationBg applicationBg = getApplicationBg();
        applicationBg.setStorage(bgCalendar);
    }

    private void initListDisplayed(List<Event> listPhoneCAll) {
        ListView listView = (ListView)  this.getActivity().findViewById(R.id.listLogsMain);
        listView.setAdapter(adapter);

        if (this.adapter == null) {
            this.adapter = new PhoneCallLArrayAdapter(this.getActivity(), listPhoneCAll);
            listView.setAdapter(this.adapter);

        } else {
            this.adapter.getListEvents().clear();
            this.adapter.getListEvents().addAll(listPhoneCAll);
        }

        AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int n = firstVisibleItem + visibleItemCount;
                if (n >= totalItemCount){
                    Log.i("bg2","FragmentLogsDetail.onScroll firstItem "+firstVisibleItem+" visibleItemCount "+visibleItemCount+"  "+(firstVisibleItem+visibleItemCount)+" totalItemCount :"+totalItemCount);
                    appendNexPageLog();
                }
            }

        };
        listView.setOnItemClickListener(this);
        if (listPhoneCAll.size()>=UtilCalendar.LIMIT_P_PAGE) {
            listView.setOnScrollListener(onScrollListener);
        }



        this.adapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.e("bg2","FragmentLogMAin No implemeneted yet");
    }

    private int page =0;
    private void appendNexPageLog(){
        page++;
        Log.i("bg2","Frqagment.logsMain.appendNexPage "+page);
        List<Event> listNextPAge = UtilCalendar.getListEvent(this.getActivity(), bgCalendar, page);
        Log.i("bg2","Frqagment.logsMain.appendNexPage adapter "+this.adapter);
        Log.i("bg2","Frqagment.logsMain.appendNexPage adapter "+this.adapter.getListEvents());
        this.adapter.getListEvents().addAll(listNextPAge);
        this.adapter.notifyDataSetChanged();
    }


}