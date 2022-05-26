import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

public class Encryption {
    private static final byte[] IV = {8, 16, 4, 2, 8, 5, 4, 21, 78, 45, 4, 22, 3, 0, 1, 8};

    public static byte[] decrypt(byte[] message) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(IV);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    App.CRYPTO_KEY.toCharArray(),
                    App.CRYPTO_SALT.getBytes(),
                    65536,
                    256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

            return cipher.doFinal(message);

        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }
}
