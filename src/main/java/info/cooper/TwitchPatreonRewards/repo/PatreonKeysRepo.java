package info.cooper.TwitchPatreonRewards.repo;

import info.cooper.TwitchPatreonRewards.patreon.PatreonKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class PatreonKeysRepo {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PatreonKey> patreonKeyMapper;

    String findById = "Select * from patreonkeys where idpatreonKeys=?";

    String findByEmail = "Select * from patreonkeys where email=?";

    String findByAccessToken = "Select * from patreonkeys where accessToken=?";

    String insertKey = "insert into patreonkeys (accessToken, expires, refreshToken, email) values (?, ?, ?, ?)";

    String updateKey = "update patreonkeys set accessToken = ?, expires = ?, refreshToken = ? where idpatreonKeys = ?";

    @Autowired
    public PatreonKeysRepo(JdbcTemplate aTemplate) {
        jdbcTemplate = aTemplate;

        patreonKeyMapper = (rs, i) -> new PatreonKey(
                rs.getLong("idpatreonKeys"),
                rs.getString("accessToken"),
                rs.getLong("expires"),
                rs.getString("refreshToken"),
                rs.getLong("patreonId")
        );
    }

    public PatreonKey findById(Long id) {
        return jdbcTemplate.queryForObject(findById, new Object[]{id}, patreonKeyMapper);
    }

    public PatreonKey findByPatreonId(Long id) {
        return jdbcTemplate.queryForObject(findByEmail, new Object[]{id}, patreonKeyMapper);
    }

    public PatreonKey findByAccessToken(String token) {
        return jdbcTemplate.queryForObject(findByAccessToken, new Object[]{token}, patreonKeyMapper);
    }

    public Integer createKey(String token, Long expires, String refreshToken, Long id) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(token);
        params.add(expires);
        params.add(refreshToken);
        params.add(id);
        return jdbcTemplate.update(insertKey, params.toArray());
    }

    public Integer updateKey(String token, Long expires, String refreshToken, Long id) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(token);
        params.add(expires);
        params.add(refreshToken);
        params.add(id);
        return jdbcTemplate.update(updateKey, params.toArray());
    }
}
