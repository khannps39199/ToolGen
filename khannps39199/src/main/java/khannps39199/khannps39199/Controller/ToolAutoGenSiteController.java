package khannps39199.khannps39199.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import jakarta.servlet.http.HttpServletRequest;
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
        return "index";
    }

    @PostMapping("/connectSQL")
    public String getConnect(Model model, @ModelAttribute ConnectInfo connectInfo) {
        System.out.println(connectInfo.getUserName() + connectInfo.getPassword() + connectInfo.getDbName());
        if (connectInfo.getUserName() == null || connectInfo.getPassword() == null || connectInfo.getDbName() == null) {
            return "redirect:/Home";
        }
        ds.setDatabaseName(connectInfo.getDbName());
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setUser(connectInfo.getUserName());
        ds.setPassword(connectInfo.getPassword());
        ds.setTrustServerCertificate(true);
        connectInfoHolder.setConnectInfo(
                new ConnectInfo(connectInfo.getUserName(), connectInfo.getPassword(), connectInfo.getDbName(), ""));
        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        List<String> listDB = getAllTables.getAllDatabases();

        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listDB", listDB);

        return "redirect:/getAllTableFormSelectedDB";
    }

    @PostMapping("/getAllTableFormSelectedDB")
    public String getAllTableFormSelectedDB(Model model) {
        ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
        String tblName = req.getParameter("tableSelect");
        conInfo.setTblName(tblName);
        connectInfoHolder.setConnectInfo(conInfo);
        conInfo = connectInfoHolder.getConnectInfo();
        List<String> listtBL = getAllTables.getAllTableNames();
        List<String> listDB = getAllTables.getAllDatabases();
        model.addAttribute("connectInfo", conInfo);
        model.addAttribute("listDB", listDB);
        model.addAttribute("listtBL", listtBL);
        return "index";
    }
    

}
