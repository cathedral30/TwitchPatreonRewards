package info.cooper.TwitchPatreonRewards.patreon;

public class PatreonAuth {
    public String id;
    public String uri;
    public String[] scopes;
    public String state;

    public PatreonAuth(String id, String uri, String[] scopes, String state) {
        this.id = id;
        this.uri = uri;
        this.scopes = scopes;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
