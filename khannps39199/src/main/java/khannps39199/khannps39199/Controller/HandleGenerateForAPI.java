package khannps39199.khannps39199.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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

public class HandleGenerateForAPI {
	CommonFunction commonFunction = new CommonFunction();

	public void HandleGenerateEntity(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
			List<ForeignKeyInfo> importedKeysInfosList, List<ForeignKeyInfo> exportedKeysInfosList)
			throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
		context.put("className", firstUpcaseClassName);
		context.put("tableName", conInfo.getTblName());
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
			String resultTosetUpdatable = !variableFeildName.contains("create") ? (e.getName().concat("\""))
					: (e.getName().concat("\",updatable = false"));

			itemFeilds.put("columnName", resultTosetUpdatable);
			itemFeilds.put("fieldName", variableFeildName);
			itemFeilds.put("isCreUp", variableFeildName.contains("create") ? "@CreationTimestamp"
					: (variableFeildName.contains("update") ? "@UpdateTimestamp" : ""));
			fields.add(itemFeilds);
		}
		for (ForeignKeyInfo e : importedKeysInfosList) {
			Map<String, String> itemForeignKeys = new HashMap<>();
			String firstUpcaseClassNameImportKey = commonFunction.ConvertToClassName(e.getPkTable());
			String variableCamelFieldName = commonFunction.ConvertToVariableName(e.getPkTable());
			itemForeignKeys.put("fkColumnName", e.getFkColumn());
			itemForeignKeys.put("pkClassName", firstUpcaseClassNameImportKey);
			itemForeignKeys.put("camelFieldName", variableCamelFieldName);

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
		context.put("isInteger", idType.equals("int") ? "@GeneratedValue(strategy = GenerationType.IDENTITY)" : "");
		context.put("exportKeys", exportKeys);
		context.put("fields", fields);
		context.put("foreignKeys", foreignKeys);
		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/Entity" + "/" + firstUpcaseClassName + ".java")) {
			mustache.execute(writer, context);
		}
	}

	public void HandleGenerateDTOS(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
			List<ForeignKeyInfo> importedKeysInfosList, List<ForeignKeyInfo> exportedKeysInfosList)
			throws SQLException, IOException {
		Map<String, Object> contextForDTOS = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
		contextForDTOS.put("className", firstUpcaseClassName);
		contextForDTOS.put("tableName", conInfo.getTblName());
		contextForDTOS.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));
		List<Map<String, String>> fieldsForDTOS = new ArrayList<>();
		List<Map<String, String>> foreignKeysForDTOS = new ArrayList<>();
		List<Map<String, String>> exportKeys = new ArrayList<>();
		for (ColumnInfo e : listtBLColumn) {
			Map<String, String> itemFeildsForDTOS = new HashMap<>();
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
			String type = switch (sqlType) {
			case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "text";
			case "INT", "INT IDENTITY", "INTEGER" -> "number";
			case "BIGINT" -> "number";
			case "BIT" -> "text";
			case "DECIMAL" -> "number";
			case "DATE", "DATETIME", "TIMESTAMP" -> "date";
			default -> "text"; // fallback
			};
			boolean isExistsKey = importedKeysInfosList.stream()
					.anyMatch(keys -> keys.getFkColumn().equals(e.getName()));
			if (isExistsKey) {
				continue;
			}
			String variableFeildName = commonFunction.firstLowCase(commonFunction.ConvertToClassName(e.getName()));
			itemFeildsForDTOS.put("javaType", javaType);
			itemFeildsForDTOS.put("columnName", e.getName());
			itemFeildsForDTOS.put("fieldName", variableFeildName);
			fieldsForDTOS.add(itemFeildsForDTOS);
		}
		for (ForeignKeyInfo e : importedKeysInfosList) {
			Map<String, String> itemForeignKeysForDTOS = new HashMap<>();
			;
			String firstUpcaseClassNameImportKey = commonFunction.ConvertToClassName(e.getPkTable());
			String variableCamelFieldName = commonFunction.ConvertToVariableName(e.getPkTable());

			itemForeignKeysForDTOS.put("fkColumnName", e.getFkColumn());
			itemForeignKeysForDTOS.put("pkClassName", firstUpcaseClassNameImportKey);
			itemForeignKeysForDTOS.put("camelFieldName", variableCamelFieldName);
			foreignKeysForDTOS.add(itemForeignKeysForDTOS);
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
		contextForDTOS.put("fields", fieldsForDTOS);
		contextForDTOS.put("foreignKeys", foreignKeysForDTOS);
		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");

		new File(conInfo.getBackEndSourceURL() + "/DTOS").mkdirs(); // Tạo thư mục nếu chưa có
		mf = new DefaultMustacheFactory();
		mustache = mf.compile("TemplateToGenerate/DTOS.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/DTOS" + "/" + firstUpcaseClassName + "DTOS.java")) {
			mustache.execute(writer, contextForDTOS);
		}
	}

	public void HandleGenerateMapper(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
			List<ForeignKeyInfo> importedKeysInfosList, List<ForeignKeyInfo> exportedKeysInfosList)
			throws SQLException, IOException {
		Map<String, Object> contextForMapper = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());

		contextForMapper.put("className", firstUpcaseClassName);
		contextForMapper.put("tableName", conInfo.getTblName());
		contextForMapper.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));

		List<Map<String, String>> fieldsForMapper = new ArrayList<>();
		List<Map<String, String>> fieldsForMapperToObject = new ArrayList<>();
		List<Map<String, String>> foreignServiceForMapperToObject = new ArrayList<>();
		for (ColumnInfo e : listtBLColumn) {
			Map<String, String> itemFeildsForMapper = new HashMap<>();
			Map<String, String> itemFeildsForReacticeObject = new HashMap<>();
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
			String type = switch (sqlType) {
			case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "text";
			case "INT", "INT IDENTITY", "INTEGER" -> "number";
			case "BIGINT" -> "number";
			case "BIT" -> "text";
			case "DECIMAL" -> "number";
			case "DATE", "DATETIME", "TIMESTAMP" -> "date";
			default -> "text"; // fallback
			};
			boolean isExistsKey = importedKeysInfosList.stream()
					.anyMatch(keys -> keys.getFkColumn().equals(e.getName()));
			if (isExistsKey) {
				continue;
			}
			String variableFeildName = commonFunction.firstLowCase(commonFunction.ConvertToClassName(e.getName()));
			itemFeildsForReacticeObject.put("fieldName", variableFeildName);
			itemFeildsForMapper.put("javaType", javaType);
			itemFeildsForMapper.put("columnName", e.getName());
			if (javaType.equals("boolean")) {
				itemFeildsForMapper.put("fieldName", "entity." + variableFeildName + "(),");
			} else {
				itemFeildsForMapper.put("fieldName",
						"entity.get" + commonFunction.ConvertToClassName(e.getName()) + "(),");
			}

			fieldsForMapper.add(itemFeildsForMapper);
			fieldsForMapperToObject.add(itemFeildsForMapper);
		}
		for (ForeignKeyInfo e : importedKeysInfosList) {
			Map<String, String> itemForeignKeysForMapper = new HashMap<>();
			Map<String, String> itemForeignKeysForMapperToObject = new HashMap<>();
			Map<String, String> itemForeignServiceForMapperToObject = new HashMap<>();
			String firstUpcaseClassNameImportKey = commonFunction.ConvertToClassName(e.getPkTable());
			String variableCamelFieldName = commonFunction.ConvertToVariableName(e.getPkTable());
			itemForeignKeysForMapper.put("fieldName", "entity.get" + firstUpcaseClassNameImportKey + "().getId(),");
			itemForeignKeysForMapperToObject.put("fieldName",
					variableCamelFieldName + "Service." + variableCamelFieldName + "FindById(entity.get"
							+ firstUpcaseClassNameImportKey + "()).orElse(null),");
			itemForeignServiceForMapperToObject.put("classNameService", firstUpcaseClassNameImportKey);
			itemForeignServiceForMapperToObject.put("variableNameService", variableCamelFieldName);
			fieldsForMapper.add(itemForeignKeysForMapper);
			fieldsForMapperToObject.add(itemForeignKeysForMapperToObject);
			foreignServiceForMapperToObject.add(itemForeignServiceForMapperToObject);
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
		Map<String, String> handleLastComa = fieldsForMapper.get(fieldsForMapper.size() - 1);
		String lastField = handleLastComa.get("fieldName");
		if (!lastField.substring(lastField.length() - 1).contains(")")) {
			lastField = lastField.substring(0, lastField.length() - 1);
			handleLastComa.put("fieldName", lastField);
			fieldsForMapper.set(fieldsForMapper.size() - 1, handleLastComa);
		}
		Map<String, String> handleLastComaForMapperToObject = fieldsForMapperToObject
				.get(fieldsForMapperToObject.size() - 1);
		String lastFieldForMapperToObject = handleLastComaForMapperToObject.get("fieldName");
		if (!lastFieldForMapperToObject.substring(lastFieldForMapperToObject.length() - 1).contains(")")) {
			lastFieldForMapperToObject = lastFieldForMapperToObject.substring(0,
					lastFieldForMapperToObject.length() - 1);
			handleLastComaForMapperToObject.put("fieldName", lastFieldForMapperToObject);
			fieldsForMapperToObject.set(fieldsForMapperToObject.size() - 1, handleLastComaForMapperToObject);
		}

		contextForMapper.put("fields", fieldsForMapper);
		contextForMapper.put("fieldsmapperToObject", fieldsForMapperToObject);
		contextForMapper.put("foreignService", foreignServiceForMapperToObject);
		String lowerClassName = commonFunction.ConvertToVariableName(conInfo.getTblName());
		contextForMapper.put("variableName", lowerClassName);
		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");

		new File(conInfo.getBackEndSourceURL() + "/Mapper").mkdirs(); // Tạo thư mục nếu chưa có
		mf = new DefaultMustacheFactory();
		mustache = mf.compile("TemplateToGenerate/Mapper.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/Mapper" + "/" + firstUpcaseClassName + "Mapper.java")) {
			mustache.execute(writer, contextForMapper);
		}
	}

	public void handleGenerateAdminRouter(List<String> listtBL, ConnectInfo conInfo) throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();
		List<Map<String, String>> feilds = new ArrayList<>();
		for (String tableName : listtBL) {
			Map<String, String> feildFeild = new HashMap<>();
			String firstUpcaseClassName = commonFunction.ConvertToClassName(tableName);
			feildFeild.put("className", firstUpcaseClassName);
			feilds.add(feildFeild);
		}
		context.put("fields", feilds);
		new File(conInfo.getFrontEndSourceURL() + "/src/router/Admin").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/adminRouterJS.mustache");
		try (Writer writer = new FileWriter(conInfo.getFrontEndSourceURL() + "/src/router/Admin/AdminRouter.js")) {
			mustache.execute(writer, context);
		}
	}

	public void HandleGenerateRepository(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn,
			ConnectInfo conInfo) throws SQLException, IOException {
		Map<String, Object> context = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
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

	public int HandleDefineRepositoryToService(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn,
			ConnectInfo conInfo) throws SQLException, IOException {
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
		String lowerClassName = commonFunction.ConvertToVariableName(conInfo.getTblName());
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
		return 0;
	}

	public int HandlGenerateControllerAPIBasic(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn,
			ConnectInfo conInfo) throws SQLException, IOException {
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
		String lowerClassName = commonFunction.ConvertToVariableName(conInfo.getTblName());
		Map<String, Object> contextToGenBaseApi = new HashMap<>();
		String idType = switch (listtBLColumn.get(0).getSqlType().toUpperCase()) {
		case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
		case "INT", "INT IDENTITY", "INTEGER" -> "int";
		case "BIGINT" -> "long";
		case "BIT" -> "boolean";
		case "DECIMAL" -> "double";
		case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
		default -> "String"; // fallback
		};

		contextToGenBaseApi.put("packageName",
				packageNameSplit.get(packageNameSplit.size() - 3) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 2) + "."
						+ packageNameSplit.get(packageNameSplit.size() - 1));
		contextToGenBaseApi.put("Classname", firstUpcaseClassName);
		contextToGenBaseApi.put("variableName", lowerClassName);

		new File(conInfo.getBackEndSourceURL() + "//API").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustacheToGenBASEAPI = mf.compile("TemplateToGenerate/controlerAPI.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getBackEndSourceURL() + "/API" + "/" + firstUpcaseClassName + "API.java")) {
			mustacheToGenBASEAPI.execute(writer, contextToGenBaseApi);
		}
		return 0;
	}

	public void HandleGenerateIndex(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
			List<ForeignKeyInfo> importedKeysInfosList, List<ForeignKeyInfo> exportedKeysInfosList)
			throws SQLException, IOException {
		Map<String, Object> contextForFormFEIndex = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());
		List<Map<String, String>> fieldsForFromFE = new ArrayList<>();
		List<Map<String, String>> fieldsForReacticeObject = new ArrayList<>();
		List<Map<String, String>> exportKeys = new ArrayList<>();
		for (ColumnInfo e : listtBLColumn) {
			Map<String, String> itemFeildsForFormFE = new HashMap<>();
			Map<String, String> itemFeildsForReacticeObject = new HashMap<>();
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
			String type = switch (sqlType) {
			case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "text";
			case "INT", "INT IDENTITY", "INTEGER" -> "number";
			case "BIGINT" -> "number";
			case "BIT" -> "text";
			case "DECIMAL" -> "number";
			case "DATE", "DATETIME", "TIMESTAMP" -> "datetime";
			default -> "text"; // fallback
			};
			boolean isExistsKey = importedKeysInfosList.stream()
					.anyMatch(keys -> keys.getFkColumn().equals(e.getName()));
			if (isExistsKey) {
				continue;
			}
			String variableFeildName = commonFunction.firstLowCase(commonFunction.ConvertToClassName(e.getName()));
			if (!sqlType.equals("INT IDENTITY")) {
				if (variableFeildName.toUpperCase().contains("EMAIL")) {
					itemFeildsForFormFE.put("type", "email");
					itemFeildsForFormFE.put("fieldName", variableFeildName);
					fieldsForFromFE.add(itemFeildsForFormFE);
				} else {
					if (variableFeildName.toUpperCase().contains("PASSWORD")) {
						itemFeildsForFormFE.put("type", "password");
						itemFeildsForFormFE.put("fieldName", variableFeildName);
						fieldsForFromFE.add(itemFeildsForFormFE);

					} else {
						if (!(variableFeildName.toUpperCase().contains("CREATE")
								|| variableFeildName.toUpperCase().contains("UPDATE"))) {
							itemFeildsForFormFE.put("type", type);
							itemFeildsForFormFE.put("fieldName", variableFeildName);
							fieldsForFromFE.add(itemFeildsForFormFE);
						}
					}
				}
			}
			itemFeildsForReacticeObject.put("fieldName", variableFeildName);
			fieldsForReacticeObject.add(itemFeildsForReacticeObject);
		}

		contextForFormFEIndex.put("TableName", firstUpcaseClassName);
		contextForFormFEIndex.put("fieldsToDynamicFeild", fieldsForFromFE);
		contextForFormFEIndex.put("fields", fieldsForReacticeObject);
		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");

		mf = new DefaultMustacheFactory();
		mustache = mf.compile("TemplateToGenerate/adminIndex.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getFrontEndSourceURL() + "/src/components/Admin/" + firstUpcaseClassName + "/index.vue")) {
			mustache.execute(writer, contextForFormFEIndex);
		}
	}

	public void HandleGenerateForm(List<String> packageNameSplit, List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
			List<ForeignKeyInfo> importedKeysInfosList, List<ForeignKeyInfo> exportedKeysInfosList)
			throws SQLException, IOException {
		Map<String, Object> contextForFormFE = new HashMap<>();
		String firstUpcaseClassName = commonFunction.ConvertToClassName(conInfo.getTblName());

		List<Map<String, String>> fieldsForFromFE = new ArrayList<>();
		List<Map<String, String>> fieldsForReacticeObject = new ArrayList<>();
		List<Map<String, String>> exportKeys = new ArrayList<>();
		for (ColumnInfo e : listtBLColumn) {
			Map<String, String> itemFeildsForFormFE = new HashMap<>();
			Map<String, String> itemFeildsForReacticeObject = new HashMap<>();
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
			String type = switch (sqlType) {
			case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "text";
			case "INT", "INT IDENTITY", "INTEGER" -> "number";
			case "BIGINT" -> "number";
			case "BIT" -> "text";
			case "DECIMAL" -> "number";
			case "DATE", "DATETIME", "TIMESTAMP" -> "datetime";
			default -> "text"; // fallback
			};
			boolean isExistsKey = importedKeysInfosList.stream()
					.anyMatch(keys -> keys.getFkColumn().equals(e.getName()));
			if (isExistsKey) {
				continue;
			}
			String variableFeildName = commonFunction.firstLowCase(commonFunction.ConvertToClassName(e.getName()));
			if (!sqlType.equals("INT IDENTITY")) {
				if (variableFeildName.toUpperCase().contains("EMAIL")) {
					itemFeildsForFormFE.put("type", "email");
					itemFeildsForFormFE.put("fieldName", variableFeildName);
					fieldsForFromFE.add(itemFeildsForFormFE);
				} else {
					if (variableFeildName.toUpperCase().contains("PASSWORD")) {
						itemFeildsForFormFE.put("type", "password");
						itemFeildsForFormFE.put("fieldName", variableFeildName);
						fieldsForFromFE.add(itemFeildsForFormFE);

					} else {
						if (!(variableFeildName.toUpperCase().contains("CREATE")
								|| variableFeildName.toUpperCase().contains("UPDATE"))) {
							itemFeildsForFormFE.put("type", type);
							itemFeildsForFormFE.put("fieldName", variableFeildName);
							fieldsForFromFE.add(itemFeildsForFormFE);
						}
					}
				}
			}
			itemFeildsForReacticeObject.put("fieldName", variableFeildName);
			fieldsForReacticeObject.add(itemFeildsForReacticeObject);
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
		contextForFormFE.put("fieldsToDynamicFeild", fieldsForFromFE);
		contextForFormFE.put("fields", fieldsForReacticeObject);

		new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");

		new File(conInfo.getFrontEndSourceURL() + "/src/components/Admin/" + firstUpcaseClassName).mkdirs();
		mf = new DefaultMustacheFactory();
		mustache = mf.compile("TemplateToGenerate/FormModelFE.mustache");
		try (Writer writer = new FileWriter(
				conInfo.getFrontEndSourceURL() + "/src/components/Admin/" + firstUpcaseClassName + "/Form.vue")) {
			mustache.execute(writer, contextForFormFE);
		}
	}

}
