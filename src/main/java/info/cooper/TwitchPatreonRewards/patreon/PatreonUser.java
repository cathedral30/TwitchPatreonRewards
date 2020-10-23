package info.cooper.TwitchPatreonRewards.patreon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatreonUser {
    Long id;
    String about;
    String email;
    String fName;
    String lName;
    String imageUrl;
    String thumbUrl;
    String url;
}
