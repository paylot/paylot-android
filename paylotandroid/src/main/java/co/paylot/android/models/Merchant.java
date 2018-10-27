package co.paylot.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created with love by Dozie on 10/24/2018.
 */

public class Merchant {
    @SerializedName("_id")
    private String id;
    private String name;
    private Logo logo;

    public String getId() {
        return id;
    }

    public Merchant setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Merchant setName(String name) {
        this.name = name;
        return this;
    }

    public Logo getLogo() {
        return logo;
    }

    public Merchant setLogo(Logo logo) {
        this.logo = logo;
        return this;
    }

    public class Logo{
        private String url;
        private String thumb;

        public String getUrl() {
            return url;
        }

        public Logo setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getThumb() {
            return thumb;
        }

        public Logo setThumb(String thumb) {
            this.thumb = thumb;
            return this;
        }
    }
}
