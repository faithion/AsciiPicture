package basic;

import java.io.IOException;
import java.util.ResourceBundle;

public class test {
	public static void main(String[] args){
		try {
			ResourceBundle resourceBundle=ResourceBundle.getBundle("uploadpath");
			Runtime.getRuntime().exec("ipconfig");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
