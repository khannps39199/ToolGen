package khannps39199.khannps39199.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import jakarta.servlet.http.HttpServletRequest;
import khannps39199.khannps39199.DTOS.ConnectInfoDTOS;
import khannps39199.khannps39199.Model.ConnectInfo;
import khannps39199.khannps39199.Model.ConnectInfoHolder;
import khannps39199.khannps39199.Model.ForeignKeyInfo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class RestAPI {
	@Autowired
	HttpServletRequest req;
	@Autowired
	SQLServerDataSource ds;
	@Autowired
	GetAllTables getAllTables;
	@Autowired
	private ConnectInfoHolder connectInfoHolder;

	@PostMapping("/ConnectSQL")
	public ResponseEntity<?> setConnectMetohd(@RequestBody ConnectInfoDTOS connectInfoDTOS) {
		ds.setDatabaseName(connectInfoDTOS.getDbName());
		ds.setServerName("localhost");
		ds.setPortNumber(1433);
		ds.setUser(connectInfoDTOS.getUserName());
		ds.setPassword(connectInfoDTOS.getPassword());
//		ds.setTrustServerCertificate(true);
		ds.setEncrypt(true);
		System.out.println(connectInfoHolder.getConnectInfo());
		ds.setTrustServerCertificate(true);
		connectInfoHolder.setConnectInfo(new ConnectInfo(connectInfoDTOS.getUserName(), connectInfoDTOS.getPassword(),
				connectInfoDTOS.getDbName(), connectInfoDTOS.getTblName() != null ? connectInfoDTOS.getTblName() : "",
				connectInfoDTOS.getBackEndSourceURL() != null ? connectInfoDTOS.getBackEndSourceURL() : "",
				connectInfoDTOS.getFrontEndSourceURL() != null ? connectInfoDTOS.getFrontEndSourceURL() : "",
				new ForeignKeyInfo()));

		List<String> listDB = getAllTables.getAllDatabases();
		return ResponseEntity.ok(listDB);
	}

	@GetMapping("/GetAllDataBaseName")
	public ResponseEntity<List<String>> getAllDataBaseName() {
		List<String> listDB = getAllTables.getAllDatabases();
		return ResponseEntity.ok(listDB);
	}

	@GetMapping("/GetAllTableName/{dbName}")
	public ResponseEntity<List<String>> getAllTableName(@PathVariable String dbName) {
		System.out.println(dbName);
		ds.setDatabaseName(dbName);
		ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
		connectInfoHolder.setConnectInfo(conInfo);
		List<String> listtBL = getAllTables.getAllTableNames();
		return ResponseEntity.ok(listtBL);
	}

}
