package info.cooper.TwitchPatreonRewards.repo;

import info.cooper.TwitchPatreonRewards.patreon.PatreonKey;
import info.cooper.TwitchPatreonRewards.security.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    String findByPatreonId = "Select * from patreonkeys where patreonId=?";

    String findByAccessToken = "Select * from patreonkeys where accessToken=?";

    String insertKey = "insert into patreonkeys (accessToken, expires, refreshToken, patreonId) values (?, ?, ?, ?)";

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
                        rs.getInt("expires"),
                        rs.getString("refreshToken"),
                        rs.getLong("patreonId")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new PatreonKey(
                    rs.getLong("idpatreonKeys"),
                    rs.getString("accessToken"),
                    rs.getInt("expires"),
                    rs.getString("refreshToken"),
                    rs.getLong("patreonId")
            );
        };
    }

    public PatreonKey findById(Long id) {
        return jdbcTemplate.queryForObject(findById, new Object[]{id}, patreonKeyMapper);
    }

    public PatreonKey findByPatreonId(Long id) {
        PatreonKey patreonKey = null;
        try {
            return jdbcTemplate.queryForObject(findByPatreonId, new Object[]{id}, patreonKeyMapper);
        } catch (EmptyResultDataAccessException e) {
            return patreonKey;
        }
    }

    public PatreonKey findByAccessToken(String token) {
        String encryptedToken;
        PatreonKey patreonKey = null;
        try {
            encryptedToken = encryptService.encryptText(token);
        } catch (Exception e) {
            encryptedToken = token;
        }
        try {
            return jdbcTemplate.queryForObject(findByAccessToken, new Object[]{encryptedToken}, patreonKeyMapper);
        } catch (EmptyResultDataAccessException e) {
            return patreonKey;
        }
    }

    public Integer createKey(String token, Integer expires, String refreshToken, Long id) {
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

    public Integer updateKey(String token, Integer expires, String refreshToken, Long id) {
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
