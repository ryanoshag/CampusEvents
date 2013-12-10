package edu.gatech.events;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class EventDetailActivity extends Activity {

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        setTitle("Event details");

        event = getIntent().getParcelableExtra("Event");
        //handles setting up the menu
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(event.title);
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(event.time.toString());
        TextView location = (TextView) findViewById(R.id.location);
        location.setText(event.location);
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(event.description);
        
        //Following is to add a button listener so that when the user clicks on the 
        //calendar button that they can actually add it to their google calendar.
        Button addToCalendarButton = (Button)findViewById(R.id.calendarButton);
        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
			//TODO factor this out into its own class.
			
        	@Override
			public void onClick(View v) {
				//Creates an intent that will allow the user to directly add the event into their
        		//google calendar.  It adds the title and location to their calendar event
        		
				Intent startCalendarIntent = new Intent(Intent.ACTION_INSERT);
				startCalendarIntent.setType("vnd.android.cursor.item/event");
				startCalendarIntent.putExtra(Events.TITLE, event.title);
				startCalendarIntent.putExtra(Events.EVENT_LOCATION, event.location);
				startActivity(startCalendarIntent);
				//TODO add the starting date to the calendar
			}
		});

        Button findDirectinosButton = (Button)findViewById(R.id.findDirections);
        findDirectinosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent directionsIntent = null;
                try {
                    directionsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.ENGLISH, "geo:0,0?q=%s", URLEncoder.encode(event.getAddress(), "UTF-8"))));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                startActivity(directionsIntent);
            }
        });
        
    }
}
