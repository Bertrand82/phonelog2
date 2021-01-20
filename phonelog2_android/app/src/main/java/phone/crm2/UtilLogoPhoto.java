package phone.crm2;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import phone.crm2.model.Contact;

public class UtilLogoPhoto {

	public static void init(Context context, TextView textViewPhoto, ImageView imageViewPhoto, Contact contact) {
		Uri uriPhoto=null;
		if (contact == null) {
		}else {
			uriPhoto = contact.getExtra(context).getPhotoUri();
			imageViewPhoto.setImageURI(uriPhoto);
		}
		if (uriPhoto == null) {
			imageViewPhoto.setVisibility(View.INVISIBLE);
			textViewPhoto.setVisibility(View.VISIBLE);
			textViewPhoto.setBackgroundColor(getColorFromContact(contact));
		} else {
			imageViewPhoto.setVisibility(View.VISIBLE);
			textViewPhoto.setVisibility(View.INVISIBLE);
		}
		String displayName2 = contact.getExtra(context).getDisplayName();
		if ((displayName2 != null) && (displayName2.length() > 1)) {
			String label = displayName2.substring(0, 2);
			textViewPhoto.setText(label);
		} else {
			String number = contact.getNumber().trim();
			textViewPhoto.setText(number.substring(number.length() - 2));
		}

	}
	private static final int[] colors = {0xfff58559, 0xff59a2be,0xffe4c62e,0xfff16364};
	private static final int[] colors_old = {Color.BLUE, Color.CYAN,Color.YELLOW,Color.RED, Color.GREEN, Color.LTGRAY};
	private static int getColorFromContact(Contact contact){
		String number = contact.getNumber();
		int n =0;
		for(byte b : number.getBytes()){
			n+=b;
		}
		int i = n%colors.length;
		return colors[i];
	}
	

	
}
