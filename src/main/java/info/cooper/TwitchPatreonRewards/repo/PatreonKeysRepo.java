package info.cooper.TwitchPatreonRewards.repo;

import info.cooper.TwitchPatreonRewards.patreon.PatreonKey;
import info.cooper.TwitchPatreonRewards.security.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class PatreonKeysRepo {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PatreonKey> patreonKeyMapper;
    private EncryptService encryptService;

    String findById = "Select * from patreonkeys where idpatreonKeys=?";

    String findByEmail = "Select * from patreonkeys where email=?";

    String findByAccessToken = "Select * from patreonkeys where accessToken=?";

    String insertKey = "insert into patreonkeys (accessToken, expires, refreshToken, email) values (?, ?, ?, ?)";

    String updateKey = "update patreonkeys set accessToken = ?, expires = ?, refreshToken = ? where idpatreonKeys = ?";

    @Autowired
    public PatreonKeysRepo(JdbcTemplate aTemplate, EncryptService anEncryptService) {
        jdbcTemplate = aTemplate;
        encryptService = anEncryptService;

        patreonKeyMapper = (rs, i) -> {
            try {
                return new PatreonKey(
                        rs.getLong("idpatreonKeys"),
                        encryptService.decryptText(rs.getString("accessToken")),
                        rs.getLong("expires"),
                        rs.getString("refreshToken"),
                        rs.getLong("patreonId")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new PatreonKey(
                    rs.getLong("idpatreonKeys"),
                    rs.getString("accessToken"),
                    rs.getLong("expires"),
                    rs.getString("refreshToken"),
                    rs.getLong("patreonId")
            );
        };
    }

    public PatreonKey findById(Long id) {
        return jdbcTemplate.queryForObject(findById, new Object[]{id}, patreonKeyMapper);
    }

    public PatreonKey findByPatreonId(Long id) {
        return jdbcTemplate.queryForObject(findByEmail, new Object[]{id}, patreonKeyMapper);
    }

    public PatreonKey findByAccessToken(String token) {
        try {
            String encryptedToken = encryptService.encryptText(token);
            return jdbcTemplate.queryForObject(findByAccessToken, new Object[]{encryptedToken}, patreonKeyMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return jdbcTemplate.queryForObject(findByAccessToken, new Object[]{token}, patreonKeyMapper);
        }
    }

    public Integer createKey(String token, Long expires, String refreshToken, Long id) {
        ArrayList<Object> params = new ArrayList<>();
        try {
            params.add(encryptService.encryptText(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.add(expires);
        params.add(refreshToken);
        params.add(id);
        return jdbcTemplate.update(insertKey, params.toArray());
    }

    public Integer updateKey(String token, Long expires, String refreshToken, Long id) {
        ArrayList<Object> params = new ArrayList<>();
        try {
            params.add(encryptService.encryptText(token));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.add(expires);
        params.add(refreshToken);
        params.add(id);
        return jdbcTemplate.update(updateKey, params.toArray());
    }
}
