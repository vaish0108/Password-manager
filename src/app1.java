import java.util.Scanner;
import java.sql.*;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
public class app1{
	
	public static byte[] Encrypt(byte[] pass) {
		byte[] keyBytes = null;
		byte[] ivBytes = null;
		SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		byte[] encrypted= new byte[cipher.getOutputSize(pass.length)];
		
		
		return encrypted;
	}
	
	public static int Encp_len(byte[]epass, byte[]pass) {
		int enc_len = 0;
		Cipher cipher = null;
		try {
			enc_len = cipher.update(pass, 0, pass.length, epass, 0);
		} catch (ShortBufferException e1) {
			e1.printStackTrace();
		}
		try {
			enc_len += cipher.doFinal(epass, enc_len);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (ShortBufferException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return enc_len;
	}

	public static byte[] Decrypt(byte[] pass,int elen) {
		byte[] keyBytes = null;
		byte[] ivBytes = null;
		SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int enc_len;
		byte[] decrypted = new byte[cipher.getOutputSize(elen)];
		int dec_len = 0;
		try {
			dec_len = cipher.update(pass, 0, elen, decrypted, 0);
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dec_len += cipher.doFinal(decrypted, dec_len);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
		      e.printStackTrace();
		}
		
		return decrypted;
	}
	
	
	public static void main(String[] args) {

		Scanner input= new Scanner(System.in);
		
		System.out.println("SIGN UP or LOG IN");
		String init_step = input.nextLine();
		
		int encryp_len=0;
		
		String execute;
		if(init_step=="S")
		{
			System.out.println("Enter username:");
			String user_name = input.nextLine();	
			System.out.println("Enter Master Password");
			String Mpassword = input.nextLine();
			byte[]mpass= Mpassword.getBytes();
			
			byte[] encryp_pass= Encrypt(mpass);
		    encryp_len= Encp_len(encryp_pass,mpass);
			
			String url = "jdbc:mysql://localhost/mp";
			Connection connection = null;
			try {
				connection = DriverManager.getConnection (url);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
			encryp_pass.toString();
			String ep= new String(encryp_pass);
			String sql= "insert into masterpass(user_name,ep)";
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
			  e.printStackTrace();
			}
			 			
		}
		
		else if(init_step=="L") 
		{
			System.out.println("Enter username:");
		
			String user_name = input.nextLine();
			System.out.println("Enter Master Password");
			String Mpassword = input.nextLine();
			byte[]mpass= Mpassword.getBytes();
			
			byte[] encryp_pass= Encrypt(mpass);
			
			String url = "jdbc:mysql://localhost/mp";
			Connection connection = null;
			try {
				connection = DriverManager.getConnection (url);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String query = "select masterpassword from masterpass where usename= user_name";
			ResultSet result = null;
			try {
				result = statement.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String masterpassword = null;
			try {
				while(result.next()) {
					 masterpassword = result.getString("masterpassword");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			byte[]dp= masterpassword.getBytes();
			
			byte[] decryp_pass= Decrypt(dp,encryp_len);
			decryp_pass.toString();
			String dep= new String(decryp_pass);
			
			if(dep!=Mpassword) {
				System.out.println("You are not registered");
				
			}
			else 
			{
				System.out.println("ADD or RETRIEVE");
			    String execute1 = input.nextLine();
			    
			    if(execute1 =="ADD")
			    {
			    	System.out.println("Domain name:");
			    	String D_name= input.nextLine();
			    	System.out.println("Username:");
			    	String U_name= input.nextLine();
			    	System.out.println("Password:");
			    	String Pass= input.nextLine();
			    	String e2pass= D_name+":"+U_name+":"+Pass;
			    	
			    	Statement statement1 = null;
					try {
						statement1 = connection.createStatement();
					} catch (SQLException e) {
						e.printStackTrace();
					}			
					String sql1= "insert into domainpass (D_name,U_name,e2pass)";
					try {
						statement1.executeUpdate(sql1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    }
			    else if(execute1 =="RETRIEVE")
			    {
			    	System.out.println("Domain name:");
			    	String D_name= input.nextLine();
			    	System.out.println("Username:");
			    	String U_name= input.nextLine();
			    	System.out.println("Password:");
			    	
			    	System.out.println("Pass");
			    }
			 
			    
			}   
			
		}
		
		
		
	}

}
