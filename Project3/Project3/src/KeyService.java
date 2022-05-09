
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class KeyService {

    private static final String AES
            = "AES";

    // We are using a Block cipher(CBC mode)
    private static final String AES_CIPHER_ALGORITHM
            = "AES/CBC/PKCS5PADDING";

    static byte[] initializationVector
            = KeyService.createInitializationVector();

    private static Scanner message;

    // Function to create a
    // secret key
    public static SecretKey createAESKey()
            throws Exception
    {
        SecureRandom securerandom
                = new SecureRandom();
        KeyGenerator keygenerator
                = KeyGenerator.getInstance(AES);

        keygenerator.init(256, securerandom);
        SecretKey key
                = keygenerator.generateKey();

        return key;
    }

    // This function takes plaintext,
    // the key with an initialization
    // vector to convert plainText
    // into CipherText.
    public static byte[] do_AESEncryption(
            String plainText,
            SecretKey secretKey,
            byte[] initializationVector)
            throws Exception
    {
        Cipher cipher
                = Cipher.getInstance(
                AES_CIPHER_ALGORITHM);

        IvParameterSpec ivParameterSpec
                = new IvParameterSpec(
                initializationVector);

        cipher.init(Cipher.ENCRYPT_MODE,
                secretKey,
                ivParameterSpec);

        return cipher.doFinal(
                plainText.getBytes());
    }

    // This function performs the
    // reverse operation of the
    // do_AESEncryption function.
    // It converts ciphertext to
    // the plaintext using the key.
    public static String do_AESDecryption(
            byte[] cipherText,
            SecretKey secretKey,
            byte[] initializationVector)
            throws Exception
    {
        Cipher cipher
                = Cipher.getInstance(
                AES_CIPHER_ALGORITHM);

        IvParameterSpec ivParameterSpec
                = new IvParameterSpec(
                initializationVector);

        cipher.init(
                Cipher.DECRYPT_MODE,
                secretKey,
                ivParameterSpec);

        byte[] result
                = cipher.doFinal(cipherText);

        return new String(result);
    }

    // Function to initialize a vector
    // with an arbitrary value
    public static byte[] createInitializationVector()
    {

        // Used with encryption
        byte[] initializationVector
                = new byte[16];
        SecureRandom secureRandom
                = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return initializationVector;
    }


    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public static SecretKey convertStringToSecretKeyto(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
        return originalKey;
    }



    public static void main(String[] args) {

        try {
            SecretKey secretKey = createAESKey();

            String plainText
                    = "{\"id\":1,\"content\":\"1\"}";

            byte[] initializationVector
                    = createInitializationVector();

            // Encrypting the message
            // using the symmetric key
            byte[] cipherText
                    = do_AESEncryption(
                    plainText,
                    secretKey,
                    initializationVector);


            String str = convertSecretKeyToString(secretKey);

            SecretKey recover = convertStringToSecretKeyto(str);
//            System.out.println(
//                    "The ciphertext or "
//                            + "Encrypted Message is: "
//                            + Hex.encodeHexString(
//                            cipherText));

            // Decrypting the encrypted
            // message
            String decryptedText
                    = do_AESDecryption(
                    cipherText,
                    recover,
                    initializationVector);

            System.out.println(
                    "Your original message is: "
                            + decryptedText);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

}
