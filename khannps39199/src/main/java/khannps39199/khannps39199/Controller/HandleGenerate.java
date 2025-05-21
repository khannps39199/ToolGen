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
import khannps39199.khannps39199.Model.ForeignKeyInfo;

public class HandleGenerate {
        public void HandleGenerateEntity(ConnectInfo connectInfo, List<String> packageNameSplit,
                        List<ColumnInfo> listtBLColumn, ConnectInfo conInfo,
                        List<ForeignKeyInfo> importeddKeysInfosList)
                        throws SQLException, IOException {
                Map<String, Object> context = new HashMap<>();
                String firstUpcaseClassName = Character.toUpperCase(connectInfo.getTblName().charAt(0))
                                + connectInfo.getTblName().substring(1);

                context.put("className", firstUpcaseClassName);
                context.put("tableName", connectInfo.getTblName());
                context.put("packageName", packageNameSplit.get(packageNameSplit.size() - 3) + "."
                                + packageNameSplit.get(packageNameSplit.size() - 2) + "."
                                + packageNameSplit.get(packageNameSplit.size() - 1));
                List<Map<String, String>> fields = new ArrayList<>();
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

                        itemFeilds.put("javaType", javaType);
                        itemFeilds.put("columnName", e.getName());
                        itemFeilds.put("fieldName", e.getName()); // Tên cột
                        fields.add(itemFeilds);
                }

                context.put("fields", fields);
                new File(conInfo.getBackEndSourceURL() + "/Entity").mkdirs(); // Tạo thư mục nếu chưa có
                MustacheFactory mf = new DefaultMustacheFactory();
                Mustache mustache = mf.compile("TemplateToGenerate/entity.mustache");
                try (Writer writer = new FileWriter(
                                conInfo.getBackEndSourceURL() + "/Entity" + "/" + firstUpcaseClassName + ".java")) {

                        mustache.execute(writer, context);
                }

        }

        public void HandleGenerateRepository(ConnectInfo connectInfo, List<String> packageNameSplit,
                        List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
                Map<String, Object> context = new HashMap<>();

                String firstUpcaseClassName = Character.toUpperCase(connectInfo.getTblName().charAt(0))
                                + connectInfo.getTblName().substring(1);
                context.put("className", firstUpcaseClassName);
                context.put("packageName", packageNameSplit.get(packageNameSplit.size() - 3) + "."
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
                                conInfo.getBackEndSourceURL() + "/Repository" + "/" + firstUpcaseClassName
                                                + "Repository.java")) {

                        mustache.execute(writer, context);
                }

        }
        
        public int HandleDefineRepositoryToService(ConnectInfo connectInfo, List<String> packageNameSplit,
                        List<ColumnInfo> listtBLColumn, ConnectInfo conInfo) throws SQLException, IOException {
                boolean isUpdate = false;
                Path path = Paths.get(conInfo.getBackEndSourceURL() + "/Service/Service.java");
                String content = Files.readString(path);

                String firstUpcaseClassName = Character.toUpperCase(connectInfo.getTblName().charAt(0))
                                + connectInfo.getTblName().substring(1);
                String lowerClassName = Character.toLowerCase(connectInfo.getTblName().charAt(0))
                                + connectInfo.getTblName().substring(1);
                // check xem đã khai báo vào service chưa
                
                // Vị trí chèn trước dấu "}" cuối cùng
                int insertPos = content.lastIndexOf('}');
                String idType = switch (listtBLColumn.get(0).getSqlType().toUpperCase()) {
                        case "VARCHAR", "NVARCHAR", "CHAR", "TEXT" -> "String";
                        case "INT", "INT IDENTITY", "INTEGER" -> "int";
                        case "BIGINT" -> "long";
                        case "BIT" -> "boolean";
                        case "DECIMAL" -> "double";
                        case "DATE", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
                        default -> "String"; // fallback
                };

                // Đoạn cần thêm
                String toAppend = "\n@Autowired\n" +
                                "    private " + firstUpcaseClassName + "Repository " + lowerClassName
                                + "Repository;\n\n" +

                                "    public List<" + firstUpcaseClassName + "> " + lowerClassName + "FindAll() {\n" +
                                "        return " + lowerClassName + "Repository.findAll();\n" +
                                "    }\n" +

                                "    public " + firstUpcaseClassName + " " + lowerClassName + "Save("
                                + firstUpcaseClassName + " " + lowerClassName + ") {\n"

                                +
                                "        return " + lowerClassName + "Repository.save(" + lowerClassName + ");\n" +
                                "    }\n" +
                                "    public Optional<" + firstUpcaseClassName + "> " + lowerClassName
                                + "FindById("
                                + idType + " id) {\n" +
                                "        return " + lowerClassName + "Repository.findById(id);\n" +
                                "    }\n" +

                                "    public void " + lowerClassName + "DeleteById(" + idType + " id) {\n" +
                                "        " + lowerClassName + "Repository.deleteById(id);\n" +
                                "    }\n";
                ;
                isUpdate=content.contains("\n@Autowired\n" +
                                "    private " + firstUpcaseClassName + "Repository " + lowerClassName
                                + "Repository;\n\n");
                        System.out.println("isContains"+"");
                if (isUpdate==true) {
                        System.out.println("isContains");
                } else {
                        // Ghi lại file
                        String updatedContent = content.substring(0, insertPos) + toAppend + "\n"
                                        + content.substring(insertPos);
                        Files.writeString(path, updatedContent);

                }
                return 0;
        }

}
