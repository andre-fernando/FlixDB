package comandre_fernando.httpsgithub.flixdb.data_objects;

import android.os.Parcel;
import android.os.Parcelable;



@SuppressWarnings("unused")
public class TrailerObject implements Parcelable {
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
    }

    public TrailerObject(String ID, String KEY, String NAME, String SITE, String TYPE) {
        id =ID;
        key=KEY;
        name=NAME;
        site=SITE;
        type=TYPE;
    }

    @SuppressWarnings("WeakerAccess")
    protected TrailerObject(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<TrailerObject> CREATOR = new Parcelable.Creator<TrailerObject>() {
        @Override
        public TrailerObject createFromParcel(Parcel source) {
            return new TrailerObject(source);
        }

        @Override
        public TrailerObject[] newArray(int size) {
            return new TrailerObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
