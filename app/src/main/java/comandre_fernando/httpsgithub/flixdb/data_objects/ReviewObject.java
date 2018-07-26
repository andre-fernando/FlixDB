package comandre_fernando.httpsgithub.flixdb.data_objects;


import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class ReviewObject implements Parcelable {
    private String id;
    private String author;
    private String content;
    private String review_url;


    public ReviewObject(String ID, String AUTHOR, String CONTENT, String REVIEW_URL) {
        id=ID;
        author=AUTHOR;
        content=CONTENT;
        review_url=REVIEW_URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.review_url);
    }



    protected ReviewObject(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.review_url = in.readString();
    }

    public static final Parcelable.Creator<ReviewObject> CREATOR = new Parcelable.Creator<ReviewObject>() {
        @Override
        public ReviewObject createFromParcel(Parcel source) {
            return new ReviewObject(source);
        }

        @Override
        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReview_url() {
        return review_url;
    }

    public void setReview_url(String review_url) {
        this.review_url = review_url;
    }
}
