import java.io.UnsupportedEncodingException;

/**
 * base64编码/解码
 * @author ceet
 *
 */
public class Base64 {

    public static String encode(String data) {
        return new String(encode(data.getBytes()));
    }

    public static String decode(String data) {
        try {
            return new String(decode(data.toCharArray()),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();

    private static byte[] codes = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            codes[i] = -1;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            codes[i] = (byte) (i - 'A');
        }

        for (int i = 'a'; i <= 'z'; i++) {
            codes[i] = (byte) (26 + i - 'a');
        }
        for (int i = '0'; i <= '9'; i++) {
            codes[i] = (byte) (52 + i - '0');
        }
        codes['+'] = 62;
        codes['/'] = 63;
    }

    public static char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    public static byte[] decode(char[] data) {
        int tempLen = data.length;
        for (int ix = 0; ix < data.length; ix++) {
            if ((data[ix] > 255) || codes[data[ix]] < 0) {
                --tempLen;
            }
        }
        int len = (tempLen / 4) * 3;
        if ((tempLen % 4) == 3) {
            len += 2;
        }
        if ((tempLen % 4) == 2) {
            len += 1;

        }
        byte[] out = new byte[len];

        int shift = 0;
        int accum = 0;
        int index = 0;

        for (int ix = 0; ix < data.length; ix++) {
            int value = (data[ix] > 255) ? -1 : codes[data[ix]];

            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }

        if (index != out.length) {
            throw new Error("Miscalculated data length (wrote " + index
                    + " instead of " + out.length + ")");
        }

        return out;
    }
}




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
