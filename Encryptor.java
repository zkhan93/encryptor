import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
public class Batman{
	public static String PASSWORD="zeek";
	public static String ENCRYPT="en";
	public static String DECRYPT="de";
	private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
	public static void main(String args[])throws IOException{
		String action=args[0];
		String source_filename=args[1];
		String key=args[2];
		File sourceFile=null;
		if(authenticate()){
			try{
				sourceFile=new File(source_filename);
			}catch(Exception ex){
				System.out.println("source not found");
				return;
			}
			if(action.equals(ENCRYPT)){
				doEncrypt(sourceFile,key);
			}else if(action.equals(DECRYPT)){
				doDecrypt(sourceFile,key);
			}
		}
	}
	public static boolean authenticate()throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		return br.readLine().equals(PASSWORD);
	}
	public static void doCrypto(int cryptMode,File source,String key){
		try{
			Key secretkey=new SecretKeySpec(key.getBytes(),ALGORITHM);
			Cipher cipher=Cipher.getInstance(TRANSFORMATION);
			cipher.init(cryptMode,secretkey);
			
			boolean readDestFileName=false;
			FileOutputStream out=null;
			while(!readDestFileName){
				try{
					System.out.print("output file: ");
					out=new FileOutputStream(new File(getDestFilename()))	;
					readDestFileName=true;
				}catch(Exception ex){
					System.out.println("Dest filename not valid. try again");
				}
			}
			FileInputStream in=new FileInputStream(source);
			byte[] inputBytes=new byte[8192];
			byte[] outputBytes;
			int bytesRead;
			CipherInputStream cin=new CipherInputStream(in,cipher);
			while((bytesRead=cin.read(inputBytes))!=-1){
				out.write(inputBytes,0,bytesRead);
			}
			out.flush();
			out.close();
			in.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void doEncrypt(File source,String key){
		doCrypto(Cipher.ENCRYPT_MODE, source,key);
	}
	public static void doDecrypt(File source,String key){
		doCrypto(Cipher.DECRYPT_MODE,source,key);
	}
	public static String getDestFilename() throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		return br.readLine();
	}
}