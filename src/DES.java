import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class DES {
byte[] skey = new byte[1000];
String skeyString;
boolean lock;
int globalCount=0;
static String method;
static double startTime;
String[] selection={"DES","AES"};

String inputMessage,encryptedData,decryptedMessage;

public DES() {

try {
byte[] raw;
int localCount=0;
method=(String) JOptionPane.showInputDialog(null,"Please select an encryption method","Select method",JOptionPane.QUESTION_MESSAGE,null,selection,"DES");
inputMessage=JOptionPane.showInputDialog(null,"Enter message to encrypt");
System.out.println(method);
byte[] ibyte = inputMessage.getBytes();
String rawKey = JOptionPane.showInputDialog(null,"Enter the keys in hexadecimal without prefix ('0x')");
raw = hexStringToByteArray(rawKey);
int runningCount=Integer.parseInt(JOptionPane.showInputDialog(null,"Enter time you want to repeat for encryption+decryption"));
startTime = System.nanoTime( );
//System.out.println("Key in byte="+raw + "Lengh: "+ raw.length);
do{
if(method.equals("DES"))
{
	byte[] key8 = new byte[8];
	raw=makeSMBKey( raw, key8 );
	//System.out.println("length"+raw.length);
}
byte[] ebyte=encrypt(raw, ibyte);
//String encryptedData = new String(ebyte);
System.out.println("Encrypted message "+bytesToHex(ebyte));
//JOptionPane.showMessageDialog(null,"Encrypted Data "+"\n"+encryptedData);

byte[] dbyte= decrypt(raw,ebyte);
String decryptedMessage = new String(dbyte);
System.out.println("Decrypted message "+decryptedMessage);
System.out.println("1st time execution finished");
localCount++;
}while(localCount<=runningCount);
}
catch(Exception e) {
System.out.println(e);
}
}
private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
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
	System.out.println("Length:"+raw.length);
SecretKeySpec skeySpec = new SecretKeySpec(raw, method);
Cipher cipher = Cipher.getInstance((method.equals("DES")?("DES/ECB/"+(clear.length%8==0?"NoPadding":"PKCS5Padding")):"AES"));
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = cipher.doFinal(clear);
return encrypted;
}

private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
SecretKeySpec skeySpec = new SecretKeySpec(raw, method.equals("DES")?"DES":"AES");
Cipher cipher = Cipher.getInstance((method.equals("DES")?("DES/ECB/"+(encrypted.length%8==0?"NoPadding":"PKCS5Padding")):"AES"));
cipher.init(Cipher.DECRYPT_MODE, skeySpec);
byte[] decrypted = cipher.doFinal(encrypted);
return decrypted;
}


public static void main(String args[]) {
@SuppressWarnings("unused")
//-------Time measurement!---------
DES des = new DES();
long endTime = System.nanoTime( );
double estimatedTime = ((double)(endTime-startTime))/Math.pow(10,9);
System.out.println( " time taken = " + estimatedTime + " sec " );
//-------Time measurement end!---------
}

}