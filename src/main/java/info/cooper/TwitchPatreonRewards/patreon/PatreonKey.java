package info.cooper.TwitchPatreonRewards.patreon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatreonKey {
    Long id;
    String token;
    Long expires;
    String refreshToken;
    Long patreonId;
}
