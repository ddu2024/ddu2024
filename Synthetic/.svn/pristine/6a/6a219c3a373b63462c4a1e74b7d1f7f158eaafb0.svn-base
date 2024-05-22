package org.nbme.dwbi.synthetic.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;

public class DDLPuller {
	private String url;
	private String user;
	private String password;
	private File outputDir;
	private HashSet<String> done = new HashSet<String>();
	private static final Logger logger = LoggerFactory.getLogger(DDLPuller.class);

	public static void main(String[] args) throws Exception {
		DDLPuller p = new DDLPuller(System.getProperty("url"), System.getProperty("user"), System.getProperty("pass"), System.getProperty("file"));
		p.pullAllDLL("TAPS_V2", "EXAMINEE");
	}

	public DDLPuller(String url, String user, String password, String outputDir) throws SQLException {
		super();
		this.url = url;
		this.user = user;
		this.password = password;

		this.outputDir = new File(outputDir);
		
		if(!this.outputDir.exists()) {
			this.outputDir.mkdir();
		}
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	}

	public void pullAllDLL(String schema, String table) throws SQLException, Exception {
		String res = pullDLL(schema, table);
		File file = new File(outputDir,  "DDL_" + table + ".sql");
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(res);
			writer.close();
		}
	}
	
	public String pullDLL(String schema, String table) throws SQLException, Exception {
		if (!done.add(schema.toUpperCase() + "." + table.toUpperCase())) {
			return "";
		}
		
		System.out.println(schema + "." + table);
		StringBuilder sb = new StringBuilder();

		try (Connection con = DriverManager.getConnection(url, user, password)) {
			try {
				sb.append(getDDL(schema, table, con));
				getCSVData(schema, table, con);
			} catch (SQLException e) {
				if (e.getMessage().contains("of type TABLE not found in schema")) {
					logger.error(schema + "." + table + " not found");
					return "";
				} else {
					throw e;
				}
			}

			ArrayList<String> rels = getRel(schema, table, con);

			for (String rel : rels) {
				sb.append("\r\n/\r\n" + rel);
			}

			for (String rel : rels) {
				ArrayList<String> tables = parseSqlStatement(rel);
				for (String tab : tables) {
					String[] splitUp = tab.split("__SEP___");
					sb.append("\r\n/\r\n" + pullDLL(splitUp[0], splitUp[1]));
				}
			}
		}
		return sb.toString();
	}
	
	private String getDDL(String schema, String table, Connection con) throws SQLException {
		Statement st = con.createStatement();
		
		String sql = "select dbms_metadata.get_ddl('TABLE', '" + table.toUpperCase() + "', '" +  schema.toUpperCase() + "') from dual";
		
		String ddl = null;
		ResultSet rs = st.executeQuery(sql);
        if(rs.next()){
            ddl = rs.getString(1).replaceAll("(?is) ENABLE(\\s*,|\\s|$)", "$1").replaceAll("(?s),\\s*CONSTRAINT\\s.*", ")").replaceAll("(?s) SEGMENT CREATION.*", "");
        }
        st.close();
        return ddl;
	}
	
	private void getCSVData(String schema, String table, Connection con) throws Exception {
		Statement st = con.createStatement();

		String sql = "select rowid, a.* from " + schema + "." + table + " a\r\n" + "order by 1 desc\r\n"
				+ "fetch first 1000 rows only";

		try {
			ResultSet result = st.executeQuery(sql);
			if (result != null) {
				File file = new File(outputDir, table + ".csv");
				try (FileWriter fileWriter = new FileWriter(file); CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);) {

					int cols = result.getMetaData().getColumnCount();
					for (int i = 2; i <= cols; i++) { //this class counts from 1, and we want to skip the first column, so we use 2
						csvPrinter.print(result.getMetaData().getColumnName(i));
					}
					csvPrinter.println();
					while (result.next()) {
						for (int i = 2; i <= cols; i++) {
							csvPrinter.print(result.getString(i));
						}
						csvPrinter.println();
					}
				}
			}
		} catch (Exception e) {
			logger.error("NO CSV FOR " + schema + "." + table + " due to: " + e.getMessage());
		}
		st.close();
	}
	
	private  ArrayList<String> getRel(String schema, String table, Connection con) throws Exception {
		Statement st = con.createStatement();
		        
        String sql = "SELECT DBMS_METADATA.get_ddl ('REF_CONSTRAINT', constraint_name, owner)\r\n"
        		+ "FROM   all_constraints\r\n"
        		+ "WHERE  owner      = '" + schema.toUpperCase() + "'\r\n"
        		+ "AND    table_name = '" +  table.toUpperCase() + "'\r\n"
        		+ "AND    constraint_type = 'R'";
		

		ArrayList<String> retVal = new ArrayList<String>();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
        	String rel = rs.getString(1).replaceAll("\\)[^()]+$",")");
        	retVal.add(rel);
        }
        st.close();
        return retVal;
	}
	

	public ArrayList<String> parseSqlStatement(String sql) throws JSQLParserException {
		net.sf.jsqlparser.statement.Statement state = CCJSqlParserUtil.parse(sql);
		ArrayList<String> retVal = new ArrayList<String>();
		if (state instanceof Alter) {
			Alter a = (Alter) state;

			List<AlterExpression> aes = a.getAlterExpressions();
			for (AlterExpression ae : aes) {
				ForeignKeyIndex index = (ForeignKeyIndex) ae.getIndex();
				String source = index.getTable().getName().replaceAll(Defaults.QUOTE_PATTERN, "");
				String schema = index.getTable().getSchemaName().replaceAll(Defaults.QUOTE_PATTERN, "");
				retVal.add(schema + "__SEP___" + source);
			}
		}
		return retVal;
	}
}
