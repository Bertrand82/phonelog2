package phone.crm2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

public class FragmentAbout extends Fragment {

    private static final String VERSION = "2.02";//3/2015
    private static final String BUILD = "6/2/2015";


    private final String adress = "https://ptit-crm.ew.r.appspot.com/";
    private final String urlStr = "https://ptit-crm.ew.r.appspot.com/";
    private Button buttonSendMessage;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonGoToSite = view.findViewById(R.id.site_url);
        buttonGoToSite.setText(urlStr);
        buttonGoToSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlStr));
                startActivity(intent);
            }
        });
        Button buttonSendMessage = view.findViewById(R.id.buttonAboutSendMessage);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMail();// Ouvre le sender de mail par defaut de l'user
            }
        });


    }




    private void sendMail() {
        UtilEmail.sendEmail(this.getActivity(),"bertrand.guiral@gmail.com","crm :  Retour from android ","");
    }
}
