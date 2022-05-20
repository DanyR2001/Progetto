package Controller;


import java.sql.*;

public class QueryManger {
	private Connection conn;
	private int actualCol;
	private int actualRow;

	
		
	public QueryManger(String DB_URL, String USER, String PAS) {          	//il costruttore inizializza la calsse Drivemaneger esegunendo la connessione al database
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PAS);
			actualCol=0;
			actualRow=0;
			System.out.println("1");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			conn=null;
			actualCol=-1;
			actualRow=-1;
			System.out.println("2");
		}
	}
	
	public void makeInsertQuery(String x) {   				//metodo per inserire dati nel database
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(x);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void NumRow(String query) {				//conta e aggiorna il numero di righe di una query, dato che questod dato non è presente				
		Statement stmt;								//nei metadati
		int rowCount = 0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			actualCol=rsmd.getColumnCount();
			while(rs.next()) {
			    rowCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			actualRow=-1;
			actualCol=-1;
		}
		actualRow=rowCount;
	}
	
	public boolean queryWhithResult(String query) {
		Statement stmt;												//n righe tante quante le tuple in output + la prima composta dal
		try {														//nome della colonna sottostante
			if(conn==null)
				return false;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			NumRow(query);
			if(actualRow>0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String[][] makeQueryWhitNameColum(String query) {		//esegue una query dando in output una matrice composta da
		Statement stmt;												//n righe tante quante le tuple in output + la prima composta dal
		try {														//nome della colonna sottostante
			if(conn==null)
				return null;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			NumRow(query);
			if(actualCol==-1&&actualRow==-1)
				return null;
			ResultSetMetaData rsmd = rs.getMetaData();
			String[][] ret=new String[actualRow+1][actualCol];
			inizialize(ret,actualRow,actualCol);
			System.out.print("col "+actualCol);
			for(int k=0;k<actualCol;k++)
				ret[0][k]=rsmd.getColumnName(k+1); 
			for(int j=1;j<=actualRow&&rs.next();j++){
				int column = rsmd.getColumnCount();
				int i;
				for( i = 0; i< column; i++){
					ret[j][i]=rs.getString(i+1);
				}
			}
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String makeQueryOneRow(String query) {			//viene invocata quando l'output previsto della query è solo una riga
		String [][] result=makeQueryWhitNameColum(query);	// l'output previsto e ...+nomeriga[i]+valore[i]+...
		if((actualCol==-1&&actualRow==-1)||conn==null)
			return null;
		String ret="";
		for(int i = 0; i< actualCol; i++){
			ret+=result[0][i].toUpperCase()+": "+result[1][i]+ ", ";
		}
		return ret;
	}
	
	public String[] makeQuery(String query) {				//viene invocato quando vi vuole avere solo l'output senza nome della colonna
		String [][] result=makeQueryWhitNameColum(query);	//nella fattispecie quando ci si aspetta solo un risultato
		System.out.print(result.toString());
		if(actualCol==-1&&actualRow==-1||conn==null)
			return null;
		String[] ret=new String[actualRow];
		inizialize(ret);
		for(int j=0;j<actualRow;j++){
			for(int i = 0; i< actualCol; i++){
				ret[j]+=result[j+1][i];
			}
		}
		return ret;
	}
	
	public static String print(String[] x) {		//per avere un array di stringhe sottoforma di un unica stringa
		String ret="";
		for(int i=0;i<x.length;i++)
			ret+=x[i].toString();
		return ret;
	}
	
	public static String printSpaced(String[] x) {		//per avere un array di stringhe sottoforma di un unica stringa
		String ret="";
		for(int i=0;i<x.length;i++)
			ret+=x[i].toString()+" ";
		return ret;
	}
	
	private static void inizialize(String[] x) {	//per inizializzare una matrice con campi vuoti
		for(int i=0;i<x.length;i++)
			x[i]="";
	}
	
	private static void inizialize(String[][] x,int num,int col) {	//per inizializzare una matrice con campi vuoti
		for(int i=0;i<num;i++)
			for(int j=0;j<col;j++)
				x[i][j]="";
	}
	
	public static String[] parseCulonFromIndex(String [][]y,int x) {	//data una matrice y restituisce sottoforma di array di stringhe
		String[] temp=new String[y.length];								//la colonna x
		for(int i=0;i<y.length;i++)
			temp[i]=y[i][x];
		return temp;
	}

	public int getActualCol() {
		return actualCol;
	}

	public int getActualRow() {
		return actualRow;
	}
	
	public void closeConnection()
	{
		try {
			System.out.print("ciao");
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
