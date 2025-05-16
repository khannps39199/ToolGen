package khannps39199.khannps39199.Controller;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import jakarta.servlet.http.HttpServletRequest;
import khannps39199.khannps39199.Model.ColumnInfo;
import khannps39199.khannps39199.Model.ConnectInfo;
import khannps39199.khannps39199.Model.ConnectInfoHolder;

@Controller
public class ToolAutoGenSiteController {
    @Autowired
    HttpServletRequest req;
    @Autowired
    SQLServerDataSource ds;
    @Autowired
    GetAllTables getAllTables;
    @Autowired
    private ConnectInfoHolder connectInfoHolder;

    @GetMapping("/Home")
    public String getMethodName(Model model) {

        model.addAttribute("connectInfo", new ConnectInfo());
        List<String> listDB = getAllTables.getAllDatabases();
        model.addAttribute("listDB", listDB);
        model.addAttribute("Component", "ConnectAndSelectDB");
        return "ExtendLayout";
    }

    @PostMapping("/connectSQL")
    public String getConnect(Model model, @ModelAttribute ConnectInfo connectInfo) {
        if (connectInfo.getUserName() == null || connectInfo.getPassword() == null || connectInfo.getDbName() == null) {
            return "redirect:/Home";
        }
        ds.setDatabaseName(connectInfo.getDbName());
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setUser(connectInfo.getUserName());
        ds.setPassword(connectInfo.getPassword());
        ds.setTrustServerCertificate(true);
        ds.setEncrypt(true);
        // ds.setTrustServerCertificate(false);

        connectInfoHolder.setConnectInfo(
                new ConnectInfo(connectInfo.getUserName(), connectInfo.getPassword(), connectInfo.getDbName(), "", "",
                        ""));
        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        List<String> listDB = getAllTables.getAllDatabases();

        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listDB", listDB);

        return "redirect:/getAllTableFormSelectedDB";
    }

    // @PostMapping("/getAllTableFormSelectedDB")
    // public String getAllTableFormSelectedDB(Model model) {
    // ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
    // String tblName = req.getParameter("tableSelect");
    // conInfo.setTblName(tblName);
    // connectInfoHolder.setConnectInfo(conInfo);
    // conInfo = connectInfoHolder.getConnectInfo();
    // List<String> listtBL = getAllTables.getAllTableNames();
    // List<String> listDB = getAllTables.getAllDatabases();
    // model.addAttribute("connectInfo", conInfo);
    // model.addAttribute("listDB", listDB);
    // model.addAttribute("listtBL", listtBL);
    // return "index";
    // }

    @GetMapping("/getAllTableFormSelectedDB")
    public String getAllTableFormSelectedDBLayout(Model model) {

        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        if (conInfo == null || conInfo.getTblName() == null) {
            return "redirect:/Home";
        }
        List<String> listDB = getAllTables.getAllDatabases();
        List<String> listtBL = getAllTables.getAllTableNames();
        model.addAttribute("listDB", listDB);
        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listtBL", listtBL);
        model.addAttribute("Component", "SelectTableComponent");
        return "ExtendLayout";
    }

    @GetMapping("/customTableComlumnToGenerate")
    public String getCustomTableComlumnToGenerate(Model model) {

        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        if (conInfo == null || conInfo.getTblName() == null) {
            return "redirect:/Home";
        }

        List<String> listDB = getAllTables.getAllDatabases();
        List<String> listtBL = getAllTables.getAllTableNames();
        model.addAttribute("listDB", listDB);
        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listtBL", listtBL);
        model.addAttribute("Component", "SelectTableComponent");
        return "ExtendLayout";
    }

    @PostMapping("/customTableComlumnToGenerate")
    public String PostCustomTableComlumnToGenerate(Model model, @ModelAttribute ConnectInfo connectInfo)
            throws SQLException, IOException {
        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        if (conInfo == null || conInfo.getTblName() == null) {
            return "redirect:/Home";
        }
        conInfo.setTblName(connectInfo.getTblName());
        conInfo.setBackEndSourceURL(connectInfo.getBackEndSourceURL());
        conInfo.setFrontEndSourceURL(connectInfo.getFrontEndSourceURL());
        connectInfoHolder.setConnectInfo(conInfo);
        conInfo = connectInfoHolder.getConnectInfo();
        List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(connectInfo.getTblName());
        List<String> listDB = getAllTables.getAllDatabases();
        List<String> listtBL = getAllTables.getAllTableNames();
        List<String> packageNameSplit = Arrays.asList(connectInfo.getBackEndSourceURL().split("\\\\"));
        Map<String, Object> context = new HashMap<>();
        context.put("className", connectInfo.getTblName());
        context.put("tableName", connectInfo.getTblName());
        context.put("packageName",packageNameSplit.get(packageNameSplit.size()-1)+"."+packageNameSplit.get(packageNameSplit.size()-2)+".Entity");

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

        model.addAttribute("listDB", listDB);
        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listtBL", listtBL);
        // model.addAttribute("Component", "SelectTableComponent");
        // return "ExtendLayout";
        return "redirect:/getAllTableFormSelectedDB";
    }

    // @GetMapping("/Generate")
    // public String Generate(Model model, @ModelAttribute ConnectInfo connectInfo)
    // throws SQLException {
    // ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
    // if (conInfo == null || conInfo.getTblName() == null) {
    // return "redirect:/Home";
    // }
    // List<ColumnInfo> listtBLColumn =
    // getAllTables.getTableColumns(connectInfo.getTblName());
    // List<String> listDB = getAllTables.getAllDatabases();
    // List<String> listtBL = getAllTables.getAllTableNames();
    // model.addAttribute("listDB", listDB);
    // model.addAttribute("connectInfo", conInfo);
    // model.addAttribute("listtBL", listtBL);
    // model.addAttribute("Component", "SelectTableComponent");
    // return "ExtendLayout";
    // }
}
