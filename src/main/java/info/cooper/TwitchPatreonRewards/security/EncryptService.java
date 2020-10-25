package info.cooper.TwitchPatreonRewards.security;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class EncryptService {

    private Cipher cipher;

    @Value("${private.key.location}")
    private Resource privateKeyResource;

    @Value("${public.key.location}")
    private Resource publicKeyResource;

    public EncryptService() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException {
        this.cipher = Cipher.getInstance("RSA");
    }

    public PrivateKey getPrivate() throws Exception {
        byte[] keyBytes = Files.readAllBytes(privateKeyResource.getFile().toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
    public PublicKey getPublic() throws Exception {
        byte[] keyBytes = Files.readAllBytes(publicKeyResource.getFile().toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String encryptText(String msg)
            throws Exception {
        PrivateKey key = getPrivate();
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8)));
    }

    public String decryptText(String msg)
            throws Exception {
        PublicKey key = getPublic();
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(msg)), StandardCharsets.UTF_8);
    }
}
