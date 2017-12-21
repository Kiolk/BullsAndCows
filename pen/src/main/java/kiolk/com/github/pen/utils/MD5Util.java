package kiolk.com.github.pen.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static kiolk.com.github.pen.utils.ConstantsUtil.EMPTY_STRING;

public class MD5Util {

    private static final String HASH_FORMAT_MD_5 = "MD5";

    public static String getHashString(String pString) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_FORMAT_MD_5);
            md.update(pString.getBytes());
            byte[] byteArray = md.digest();
            StringBuilder buffer = new StringBuilder();

            for (byte aByteArray : byteArray) {
                buffer.append(Integer.toHexString(0xFF & aByteArray));
            }

            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return EMPTY_STRING;
    }
}
