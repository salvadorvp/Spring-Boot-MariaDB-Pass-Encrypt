package com.westwebsystems.budgets.helputils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {
    public static final String AES = "AES";
    public static final String KEY_FILE = "westwebsystems.key";

    /**
     * Encrypt a value and generate a keyfile.
     * If the keyfile is not found, then a new one will be created.
     *
     * source: http://stackoverflow.com/questions/3769622/encrypt-and-decrypt-property-file-value-in-java
     *
     * @throws GeneralSecurityException
     * @throws IOException if an I/O error occurs
     */
    public static String encrypt(String value, File keyFile)
            throws GeneralSecurityException, IOException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
            fw.close();
        }

        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    /**
     * Decrypt a value.
     *
     * source: http://stackoverflow.com/questions/3769622/encrypt-and-decrypt-property-file-value-in-java
     *
     * @throws GeneralSecurityException
     * @throws IOException if an I/O error occurs
     */
    public static String decrypt(String message, File keyFile) throws GeneralSecurityException, IOException {
        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
        return new String(decrypted);
    }


    private static SecretKeySpec getSecretKeySpec(File keyFile) throws NoSuchAlgorithmException, IOException {
        byte[] key = readKeyFile(keyFile);
        SecretKeySpec sks = new SecretKeySpec(key, AES);
        return sks;
    }

    private static byte[] readKeyFile(File keyFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
        String keyValue = scanner.next();
        scanner.close();
        return hexStringToByteArray(keyValue);
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    // ------------------- M A I N -----------------------
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader stdin = new BufferedReader(isr);

        System.out.println("SecurityUtils - Secret token encryption");
        System.out.println("---------------------------------------");
        System.out.println("This will do two things:");
        System.out.println("1. Look for the key file '" + KEY_FILE + "' in the current directory. If it doesn't exist");
        System.out.println("   it will create a new key file that will be used to encrypt the typed secret token.");
        System.out.println("2. Will generate an encrypted string for the typed secret token.");
        System.out.println("");
        System.out.println("NOTE: The generated key file is required to decrypt existing encrypted strings. It cannot be");
        System.out.println("      re-generated, if it's lost then a new key file will have to be created and the ");
        System.out.println("      mismatched encrypted strings with the previous key file will be unusable.");
        System.out.println("");
        System.out.print("Enter the secret token to encrypt:");
        String line = stdin.readLine();

        String inputToken = line;
        System.out.println("Input: " + inputToken);

        System.out.println("");

        File keyFile = new File(KEY_FILE);
        boolean exists = keyFile.exists();

        if (!exists)
            System.out.println ("Creating a new '" + KEY_FILE + "' key file!");
        else
            System.out.println("Found an existing '" + KEY_FILE + "' key file.");


        String encryptedStr = SecurityUtils.encrypt(inputToken, keyFile);
        System.out.println ("Encrypted: " + encryptedStr);

        String decryptedStr = SecurityUtils.decrypt(encryptedStr, keyFile);
        System.out.println("Decrypted: " + decryptedStr);
    }

}

