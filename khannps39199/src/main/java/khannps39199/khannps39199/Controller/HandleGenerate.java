package khannps39199.khannps39199.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import khannps39199.khannps39199.Model.ColumnInfo;
import khannps39199.khannps39199.Model.CommonFunction;
import khannps39199.khannps39199.Model.ConnectInfo;
import khannps39199.khannps39199.Model.ForeignKeyInfo;

public class HandleGenerate {

	CommonFunction commonFunction = new CommonFunction();

	public void HandleGenerateEntity(ConnectInfo connectInfo, List<String> packageNameSplit,
			List<ColumnInfo> listtBLColumn, ConnectInfo conInfo, List<ForeignKeyInfo> importedKeysInfosList,
			List<ForeignKeyInfo> exportedKeysInfosList) throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(connectInfo.getTblName());
		context.put("className", firstUpcaseClassName);
		context.put("tableName", connectInfo.getTblName());
		context.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));
		List<Map<String, String>> fields = new ArrayList<>();
		List<Map<String, String>> foreignKeys = new ArrayList<>();
		List<Map<String, String>> exportKeys = new ArrayList<>();
		for (ColumnInfo e : listtBLColumn) {
			Map<String, String> itemFeilds = new HashMap<>();
			String sqlType = e.getSqlType().toUpperCase();
			String javaType = switch (sqlType) {
			case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
			case "INT", "INT IDENTITY", "INTEGER" -> "int";
			case "BIGINT" -> "long";
			case "BIT" -> "boolean";
			case "DECIMAL" -> "double";
			case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
			default -> "String"; // fallback
			};
			boolean isExistsKey = importedKeysInfosList.stream()
					.anyMatch(keys -> keys.getFkColumn().equals(e.getName()));
			if (isExistsKey) {
				continue;
			}
			String variableFeildName = commonFunction.firstLowCase(commonFunction.ConvertToClassName(e.getName()));
			itemFeilds.put("javaType", javaType);
			itemFeilds.put("columnName", e.getName());
			itemFeilds.put("fieldName", variableFeildName); // Tên cột
			fields.add(itemFeilds);
		}
		for (ForeignKeyInfo e : importedKeysInfosList) {

			Map<String, String> itemForeignKeys = new HashMap<>();
			String firstUpcaseClassNameImportKey = commonFunction.ConvertToClassName(e.getPkTable());
			String variableCamelFieldName = commonFunction.ConvertToVariableName(e.getPkTable());

			itemForeignKeys.put("fkColumnName", e.getFkColumn());
			itemForeignKeys.put("pkClassName", firstUpcaseClassNameImportKey);
			itemForeignKeys.put("camelFieldName", variableCamelFieldName); // Tên cột

			System.out.println(e.getPkTable() + "\n"+e.getFkColumn() + " " + e.getPkColumn() + " "+variableCamelFieldName+  "\n" );
			foreignKeys.add(itemForeignKeys);
		}
		String idType = switch (listtBLColumn.get(0).getSqlType().toUpperCase()) {
		case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
		case "INT", "INT IDENTITY", "INTEGER" -> "int";
		case "BIGINT" -> "long";
		case "BIT" -> "boolean";
		case "DECIMAL" -> "Double";
		case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
		default -> "String"; // fallback
		};
		context.put("className", firstUpcaseClassName);
		context.put("isInteger", idType.equals("int") ? "@GeneratedValue(strategy = GenerationType.IDENTITY)" : "");
		context.put("fields", fields);
		context.put("foreignKeys", foreignKeys);
		context.put("exportKeys", exportKeys);
		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/Entity" + "/" + firstUpcaseClassName + ".java")) {

			mustache.execute(writer, context);
		}

	}
	public void  handleGenerateAdminRouter (List<String> listtBL, ConnectInfo conInfo) throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();
		List<Map<String,String>> feilds= new ArrayList<>();
		for(String tableName : listtBL) {
			Map<String,String> feildFeild= new HashMap<>();
			String firstUpcaseClassName = commonFunction.ConvertToClassName(tableName);

			feildFeild.put("className", firstUpcaseClassName);
			feilds.add(feildFeild);
		}
		context.put("fields", feilds);

		new File(conInfo.getFrontEndSourceURL() +"/src/router/Admin").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/adminRouterJS.mustache");

		try (Writer writer = new FileWriter(
				conInfo.getFrontEndSourceURL() +"/src/router/Admin/AdminRouter.js")) {
			mustache.execute(writer, context);
		}
	}
	
	public void HandleGenerateRepository(ConnectInfo connectInfo, List<String> packageNameSplit,
			List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();

		String firstUpcaseClassName = commonFunction.ConvertToClassName(connectInfo.getTblName());

		context.put("className", firstUpcaseClassName);
		context.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));

		String javaType = switch (listtBLColumn.get(0).getSqlType().toUpperCase()) {
		case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
		case "INT", "INT IDENTITY", "INTEGER" -> "Integer";
		case "BIGINT" -> "long";
		case "BIT" -> "boolean";
		case "DECIMAL" -> "Double";
		case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
		default -> "String"; // fallback
		};
		context.put("idType", javaType);

		new File(conInfo.getBackEndSourceURL() + "/Repository").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/repository.mustache");

		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/Repository" + "/" + firstUpcaseClassName + "Repository.java")) {

			mustache.execute(writer, context);
		}

	}

	public int HandleDefineRepositoryToService(ConnectInfo connectInfo, List<String> packageNameSplit,
			List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
		boolean isUpdate = false;

		String firstUpcaseClassName = commonFunction.ConvertToClassName(connectInfo.getTblName());
		String lowerClassName = commonFunction.ConvertToVariableName(connectInfo.getTblName());
		Map<String, Object> context = new HashMap<>();
		Map<String, Object> contextToGenBaseApi = new HashMap<>();
		context.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));
		context.put("className", firstUpcaseClassName);
		context.put("variableClassName", lowerClassName);

		String idType = switch (listtBLColumn.get(0).getSqlType().toUpperCase()) {
		case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
		case "INT", "INT IDENTITY", "INTEGER" -> "int";
		case "BIGINT" -> "long";
		case "BIT" -> "boolean";
		case "DECIMAL" -> "double";
		case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
		default -> "String"; // fallback
		};
		context.put("idType", idType);
		new File(conInfo.getBackEndSourceURL() + "/Service").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/service.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/Service" + "/" + firstUpcaseClassName + "Service.java")) {

			mustache.execute(writer, context);
		}
		contextToGenBaseApi.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));
		contextToGenBaseApi.put("Classname", firstUpcaseClassName);
		contextToGenBaseApi.put("variableName", lowerClassName);
		new File(conInfo.getBackEndSourceURL() + "//API").mkdirs(); // Tạo thư mục nếu chưa có
		Mustache mustacheToGenBASEAPI = mf.compile("TemplateToGenerate/controlerAPI.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/API" + "/" + firstUpcaseClassName + "API.java")) {

			mustacheToGenBASEAPI.execute(writer, contextToGenBaseApi);
		}

//		Path path = Paths.get(conInfo.getBackEndSourceURL() + "/Service" + "/" + firstUpcaseClassName + ".java");
//		String content = Files.readString(path);
//
//		// check xem đã khai báo vào service chưa
//
//		// Vị trí chèn trước dấu "}" cuối cùng
//		int insertPos = content.lastIndexOf('}');
//
//		// Đoạn cần thêm
//		String toAppend = "\n@Autowired\n" + "    private " + firstUpcaseClassName + "Repository " + lowerClassName
//				+ "Repository;\n\n" +
//
//				"    public List<" + firstUpcaseClassName + "> " + lowerClassName + "FindAll() {\n" + "        return "
//				+ lowerClassName + "Repository.findAll();\n" + "    }\n" +
//
//				"    public " + firstUpcaseClassName + " " + lowerClassName + "Save(" + firstUpcaseClassName + " "
//				+ lowerClassName + ") {\n"
//
//				+ "        return " + lowerClassName + "Repository.save(" + lowerClassName + ");\n" + "    }\n"
//				+ "    public Optional<" + firstUpcaseClassName + "> " + lowerClassName + "FindById(" + idType
//				+ " id) {\n" + "        return " + lowerClassName + "Repository.findById(id);\n" + "    }\n" +
//
//				"    public void " + lowerClassName + "DeleteById(" + idType + " id) {\n" + "        " + lowerClassName
//				+ "Repository.deleteById(id);\n" + "    }\n";
//		;
//		isUpdate = content.contains("\n@Autowired\n" + "    private " + firstUpcaseClassName + "Repository "
//				+ lowerClassName + "Repository;\n\n");
//		System.out.println("isContains" + "");
//		if (isUpdate == true) {
//			System.out.println("isContains");
//		} else {
//			// Ghi lại file
//			String updatedContent = content.substring(0, insertPos) + toAppend + "\n" + content.substring(insertPos);
//			Files.writeString(path, updatedContent);
//
//		}
		return 0;
	}

}
