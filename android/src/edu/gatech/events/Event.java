package edu.gatech.events;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Event implements Parcelable {
    public String title;
    public String description;
    public String location;
    public String/*Date*/ time;

    public Event() {}

    public Event(String title, String time, String location, String description) {
        this.title = title;
        this.time = time;
        this.location = location;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(location);
        parcel.writeString(time);
    }

    private static Map<String, String> locationAddresses = new HashMap<String, String>() {{
        put("Student Center", "350 Ferst Drive, Atlanta, GA 30332-0458");
        put("College of Computing", "801 Atlantic Drive, Atlanta, GA 30332-0280");
        put("Klaus Advanced Computing Building", "266 Ferst Drive, Atlanta, GA 30332-0765");
        put("Howey Physics Building", "800 Atlantic Drive, Atlanta, GA");
        put("Library", "704 Cherry Street, Atlanta, GA");
        put("Alexander Memorial Coliseum", "965 Fowler Street, Atlanta, GA");
        put("Clough Undergraduate Learning Center", "266 Fourth St. NW, Atlanta, GA");
        put("Campus Recreation Center", "750 Ferst Drive, Atlanta, GA");
        put("Swann Building", "613 Cherry Street, Atlanta, GA");
        put("Old CE Building", "215 Bobby Dodd Way, Atlanta, GA");
        put("Parker H. Petit Institute for Bioengineering & Bio", "315 Ferst Drive, Atlanta, GA");
    }};

    public String getAddress() {
        String value = locationAddresses.get(location);
        if (value == null) {
            value = "";
        }
        return value;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel parcel) {
            Event event = new Event();
            event.title = parcel.readString();
            event.description = parcel.readString();
            event.location = parcel.readString();
            event.time = parcel.readString();
            return event;
        }

        @Override
        public Event[] newArray(int i) {
            return new Event[i];
        }
    };
}
