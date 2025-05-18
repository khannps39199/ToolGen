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
import khannps39199.khannps39199.Model.ConnectInfo;

public class HandleGenerate {
    public void HandleGenerateEntity(ConnectInfo connectInfo, List<String> packageNameSplit,
            List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("className", connectInfo.getTblName());
        context.put("tableName", connectInfo.getTblName());
        context.put("packageName", packageNameSplit.get(packageNameSplit.size() - 1) + "."
                + packageNameSplit.get(packageNameSplit.size() - 2) + ".Entity");
        List<Map<String, String>> fields = new ArrayList<>();
        listtBLColumn.forEach(e -> {
            Map<String, String> itemFeilds = new HashMap<>();
            String sqlType = e.getSqlType().toUpperCase();
            String javaType = switch (sqlType) {
                case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
                case "INT", "INTEGER" -> "int";
                case "BIGINT" -> "long";
                case "BIT" -> "boolean";
                case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
                default -> "String"; // fallback
            };
            itemFeilds.put("javaType", javaType);
            itemFeilds.put("columnName", e.getName());
            itemFeilds.put("fieldName", e.getName()); // Tên cột
            fields.add(itemFeilds);
        });
        context.put("fields", fields);
        new File(conInfo.getBackEndSourceURL()).mkdirs(); // Tạo thư mục nếu chưa có
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");

        try (Writer writer = new FileWriter(
                conInfo.getBackEndSourceURL() + "/Entity" + "/" + conInfo.getTblName() + ".java")) {
            System.out.println(context.get("fields"));
            mustache.execute(writer, context);
        }

    }

    public void HandleGenerateRepository(ConnectInfo connectInfo, List<String> packageNameSplit,
            List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("className", connectInfo.getTblName());
        context.put("packageName", packageNameSplit.get(packageNameSplit.size() - 1) + "."
                + packageNameSplit.get(packageNameSplit.size() - 2));
        String javaType = switch (listtBLColumn.get(0).getSqlType()) {
            case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
            case "INT", "INTEGER" -> "int";
            case "BIGINT" -> "long";
            case "BIT" -> "boolean";
            case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
            default -> "String"; // fallback
        };

        context.put("idType", javaType);

        new File(conInfo.getBackEndSourceURL()).mkdirs(); // Tạo thư mục nếu chưa có
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("TemplateToGenerate/repository.mustache");

        try (Writer writer = new FileWriter(
                conInfo.getBackEndSourceURL() + "/Repository" + "/" + conInfo.getTblName() + "Repository.java")) {

            mustache.execute(writer, context);
        }
    }

    public void HandleDefineRepositoryToService(ConnectInfo connectInfo, List<String> packageNameSplit,
            List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
        Path path = Paths.get(conInfo.getBackEndSourceURL() + "/Service/Service.java");
        String content = Files.readString(path);
        String className = connectInfo.getTblName();
        String lowerClassName = Character.toLowerCase(className.charAt(0)) + className.substring(1);

        // Vị trí chèn trước dấu "}" cuối cùng
        int insertPos = content.lastIndexOf('}');

        // Đoạn cần thêm
        String toAppend = 
                "\n  @Autowired\n" +
                "    private " + className + "Repository " + lowerClassName + "Repository;\n\n" +

                "    public List<" + className + "> findAll() {\n" +
                "        return " + lowerClassName + "Repository.findAll();\n" +
                "    }\n\n" +

                "    public Optional<" + className + "> findById({{idType}} id) {\n" +
                "        return " + lowerClassName + "Repository.findById(id);\n" +
                "    }\n\n" +

                "    public " + className + " save(" + className + " " + lowerClassName + ") {\n" +
                "        return " + lowerClassName + "Repository.save(" + lowerClassName + ");\n" +
                "    }\n\n" +

                "    public void deleteById({{idType}} id) {\n" +
                "        " + lowerClassName + "Repository.deleteById(id);\n" +
                "    }\n";

        // Ghi lại file
        String updatedContent = content.substring(0, insertPos) + toAppend + "\n" + content.substring(insertPos);
        Files.writeString(path, updatedContent);
    }

}
