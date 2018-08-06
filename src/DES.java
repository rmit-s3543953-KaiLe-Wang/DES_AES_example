import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class DES {
byte[] skey = new byte[1000];
String skeyString;
static String method;
String[] selection={"DES","AES"};
static byte[] raw;
String inputMessage,encryptedData,decryptedMessage;

public DES() {
try {
//generateSymmetricKey();(null,"Enter message to encrypt")
method=(String) JOptionPane.showInputDialog(null,"Please select an encryption method","Select method",JOptionPane.QUESTION_MESSAGE,null,selection,"DES");
inputMessage=JOptionPane.showInputDialog(null,"Enter message to encrypt");
System.out.println(method);
byte[] ibyte = inputMessage.getBytes();
String rawKey = JOptionPane.showInputDialog(null,"Enter the keys in hexadecimal without prefix ('0x')");
raw = hexStringToByteArray(rawKey);
//System.out.println("Key in byte="+raw + "Lengh: "+ raw.length);
if(method.equals("DES"))
{
	byte[] key8 = new byte[8];
	raw=makeSMBKey( raw, key8 );
}
byte[] ebyte=encrypt(raw, ibyte);
String encryptedData = new String(ebyte);
System.out.println("Encrypted message "+toHex(encryptedData));
JOptionPane.showMessageDialog(null,"Encrypted Data "+"\n"+encryptedData);

byte[] dbyte= decrypt(raw,ebyte);
String decryptedMessage = new String(dbyte);
System.out.println("Decrypted message "+decryptedMessage);

JOptionPane.showMessageDialog(null,"Decrypted Data "+"\n"+decryptedMessage);
}
catch(Exception e) {
System.out.println(e);
}
}

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
public static String toHex(String text) throws UnsupportedEncodingException
{
    byte[] myBytes = text.getBytes(/*"UTF-8"*/);

    return DatatypeConverter.printHexBinary(myBytes);
}

public static byte[] makeSMBKey(byte[] key7, byte[] key8) {

    int i;

    key8[0] = (byte) ( ( key7[0] >> 1) & 0xff);
    key8[1] = (byte)(( ((key7[0] & 0x01) << 6) | (((key7[1] & 0xff)>>2) & 0xff)) & 0xff );
    key8[2] = (byte)(( ((key7[1] & 0x03) << 5) | (((key7[2] & 0xff)>>3) & 0xff)) & 0xff );
    key8[3] = (byte)(( ((key7[2] & 0x07) << 4) | (((key7[3] & 0xff)>>4) & 0xff)) & 0xff );
    key8[4] = (byte)(( ((key7[3] & 0x0F) << 3) | (((key7[4] & 0xff)>>5) & 0xff)) & 0xff );
    key8[5] = (byte)(( ((key7[4] & 0x1F) << 2) | (((key7[5] & 0xff)>>6) & 0xff)) & 0xff );
    key8[6] = (byte)(( ((key7[5] & 0x3F) << 1) | (((key7[6] & 0xff)>>7) & 0xff)) & 0xff );
    key8[7] = (byte)(key7[6] & 0x7F);
    for (i=0;i<8;i++) {
        key8[i] = (byte)( key8[i] << 1);
    }
    return key8;
}
private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
SecretKeySpec skeySpec = new SecretKeySpec(raw, method);
Cipher cipher = Cipher.getInstance(method);
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = cipher.doFinal(clear);
return encrypted;
}

private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
SecretKeySpec skeySpec = new SecretKeySpec(raw, method);
Cipher cipher = Cipher.getInstance(method);
cipher.init(Cipher.DECRYPT_MODE, skeySpec);
byte[] decrypted = cipher.doFinal(encrypted);
return decrypted;
}
public static void main(String args[]) {
@SuppressWarnings("unused")
DES des = new DES();
}

}