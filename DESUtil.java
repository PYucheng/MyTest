
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * DES对称算法(加密/解密)
 *
 * @author ceet
 *
 */
public class DESUtil {

    private Key key;

    public DESUtil(String strKey) {
        setKey(strKey);
    }

    public void setKey(String strKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom(strKey.getBytes())); // 根据参数生成key
            this.key = generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String source) {
        return encrypt(source, "utf-8");
    }

    public String decrypt(String encryptedData) {
        return decrypt(encryptedData, "utf-8");
    }

    public String encrypt(String source, String charSet) {
        String encrypt = null;
        try {
            byte[] ret = encrypt(source.getBytes(charSet));
            encrypt = new String(Base64.encode(ret));
        } catch (Exception e) {
            e.printStackTrace();
            encrypt = null;
        }
        return encrypt;
    }

    public String decrypt(String encryptedData, String charSet) {
        String descryptedData = null;
        try {
            byte[] ret = descrypt(Base64.decode(encryptedData.toCharArray()));
            descryptedData = new String(ret, charSet);
        } catch (Exception e) {
            e.printStackTrace();
            descryptedData = null;
        }
        return descryptedData;
    }

    private byte[] encrypt(byte[] primaryData) {
        try {
            Cipher cipher = Cipher.getInstance("DES"); // Cipher对象实际完成加密操作
            cipher.init(Cipher.ENCRYPT_MODE, this.key); // 用密钥初始化Cipher对象(加密)

            return cipher.doFinal(primaryData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] descrypt(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("DES"); // Cipher对象实际完成解密操作
            cipher.init(Cipher.DECRYPT_MODE, this.key); // 用密钥初始化Cipher对象(解密)

            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String code = "ceet";
        DESUtil desUtil = new DESUtil("key");
        String encrypt = desUtil.encrypt(code);
        String decrypt = desUtil.decrypt(encrypt);
        System.out.println("原内容：" + code);
        System.out.println("加密：" + encrypt);
        System.out.println("解密：" + decrypt);
    }
}
