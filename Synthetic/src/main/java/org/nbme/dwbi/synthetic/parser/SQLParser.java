package org.nbme.dwbi.synthetic.parser;

import java.util.ArrayList;
import java.util.List;

import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.ContainerType;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.FieldType;
import org.nbme.dwbi.synthetic.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;

public class SQLParser {
	private Metadata md;
	private StringBuilder sb = new StringBuilder();
	private static final Logger logger = LoggerFactory.getLogger(SQLParser.class);

	public SQLParser() {
		this(new Metadata());
	}
	public SQLParser(Metadata md) {
		this.md = md;
		ArrayList<Container> containers = new ArrayList<Container>();
		md.setObjects(containers);
	}
		
	public void parseSqlStatements(String sql) throws JSQLParserException {
		
		String[] sqlStatements = sql.split("\\r*\\n/\\r*\\n");
		
		for(String sqlStatement : sqlStatements) {
			if(sqlStatement.matches("(?is)(?:^.*?CREATE\\s+TABLE.*|.*?CONSTRAINT\\s.*?\\sFOREIGN\\s+KEY.*)")) {
				sqlStatement = sqlStatement
						.replaceAll("(?i)(?<=\\s)STORAGE\\s*\\([^)]+\\)(?=\\s)", "") //these are needed if 'Include Storage Clause' is included in the ddl pull via Sql Navigator
						.replaceAll("(?i)(?<=\\s)NOCACHE(?=\\s)", "");
				parseSqlStatement(sqlStatement);
			}
		}
		
		logger.info(sb.toString());
		
	}	

	public void parseSqlStatement(String sql) throws JSQLParserException {
		Statement state = CCJSqlParserUtil.parse(sql);
		if(state instanceof CreateTable) {
			CreateTable ct = (CreateTable)state;
			Container container = new Container();
			container.setContainerType(ContainerType.CSV);
			container.setContainerName(ct.getTable().getName().replaceAll(Defaults.QUOTE_PATTERN, ""));
			sb.append((sb.length() > 0 ? System.lineSeparator() : "") + "##### TableName: " + container.getContainerName() + System.lineSeparator());
			ArrayList<Field> fields = new ArrayList<Field>();
			for(ColumnDefinition cd : ct.getColumnDefinitions()){
				Field field = new Field();
				field.setName(cd.getColumnName().replaceAll(Defaults.QUOTE_PATTERN, ""));
				String typeIn = cd.getColDataType().getDataType();
				FieldType typeOut = FieldType.STR;
				StringBuilder sbOptions = new StringBuilder();
				switch(typeIn) {
					case "DECIMAL":
					case "NUMBER":
						typeOut = FieldType.DOUBLE;
						List<String> args;
						if((args = cd.getColDataType().getArgumentsStringList()) != null) {
							int precision = Integer.parseInt(cd.getColDataType().getArgumentsStringList().get(0));
							int scale = 0;
							if(args.size() < 2 || (scale = Integer.parseInt(cd.getColDataType().getArgumentsStringList().get(1))) <= 0) {
								if(scale > 0) {
									precision -= scale;
								}
								typeOut = FieldType.INT;
							}
							int max = (int)Math.pow(10, precision) - 1;
							field.setHigh((double)max);
							sbOptions.append("High: " + field.getHigh());
						}
						break;
					case "DATE":
						typeOut = FieldType.DATE;
						break;
					case "INT":
					case "SMALLINT":
						typeOut = FieldType.INT;
						break;
					case "VARCHAR2":
						Integer maxLen = Integer.parseInt(cd.getColDataType().getArgumentsStringList().get(0));
						if(new Integer(1).equals(maxLen)) {
							typeOut = FieldType.CHAR;
						} else {
							field.setMaxLen(maxLen);
							sbOptions.append("MaxLen: " + maxLen);
						}
						break;
				}
				field.setFieldType(typeOut);
				
				fields.add(field);
				sb.append("####### Field: " + field.getName() + ": " + field.getFieldType() + (sbOptions.length() > 0 ? ", " + sbOptions.toString() : "") + System.lineSeparator());
			}
			container.setFields(fields);
			md.getObjects().add(container);
		} else if(state instanceof Alter) {
			Alter a = (Alter)state;
			String table = a.getTable().getName().replaceAll(Defaults.QUOTE_PATTERN, "");
			Container targetContainer = null;
			for(Container container : md.getObjects()) {
				if(container.getContainerName().equals(table)) {
					targetContainer = container;
					break;
				}
			}
			List<AlterExpression> aes = a.getAlterExpressions();
			for(AlterExpression ae : aes) {
				ForeignKeyIndex index = (ForeignKeyIndex)ae.getIndex();
				String source = index.getTable().getName().replaceAll(Defaults.QUOTE_PATTERN, "");
				for(int i = 0; i < index.getColumnsNames().size(); i++) {
					String targetCol = index.getColumnsNames().get(i).replaceAll(Defaults.QUOTE_PATTERN, "");
					String sourceCol = index.getReferencedColumnNames().get(i).replaceAll(Defaults.QUOTE_PATTERN, "");
					Field targetField = null;
					for(Field field : targetContainer.getFields()) {
						if(field.getName().equals(targetCol)) {
							targetField = field;
						break;
						}
					}
					targetField.setReference(source.replaceAll(Defaults.QUOTE_PATTERN, "") + "." + sourceCol.replaceAll(Defaults.QUOTE_PATTERN, ""));
					sb.append("$$$$$$$ Rel: " + source + "." + sourceCol + " -> " + targetField.getName() + System.lineSeparator());
				}
			}			
		}
	}

	public Metadata getMd() {
		return md;
	}

	public void setMd(Metadata md) {
		this.md = md;
	}
}
