import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;


public class TestSQL {

	public static void main(String args[]){

	    try {
	    	SQLiteConnection db = new SQLiteConnection(new File("/tmp/database"));
			db.open(true);
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
