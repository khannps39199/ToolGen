package khannps39199.khannps39199.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import khannps39199.khannps39199.Model.ColumnInfo;
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

	HandleGenerateForAPI handelGen = new HandleGenerateForAPI();

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

	@GetMapping("/handelGenEntity")
	public ResponseEntity<?> handelGenEntity() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleGenerateEntity(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
						ExportedKeysInfos);

			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateEntity(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
							ExportedKeysInfos);
				}
			}
			return ResponseEntity.ok("Entity generation successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Entity generation failed: " + e.getMessage());
		}
	}

	@GetMapping("/handelGenDTOS")
	public ResponseEntity<?> handelGenDTOS() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleGenerateDTOS(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
						ExportedKeysInfos);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateDTOS(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
							ExportedKeysInfos);
				}
			}
			return ResponseEntity.ok("handelGenDTOS successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenDTOS failed: " + e.getMessage());
		}

	}

	@GetMapping("/handelGenMapper")
	public ResponseEntity<?> handelGenMapper() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleGenerateMapper(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
						ExportedKeysInfos);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateMapper(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
							ExportedKeysInfos);
				}
			}
			return ResponseEntity.ok("handelGenMapper successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenMapper failed: " + e.getMessage());
		}
	}

	@GetMapping("/handleGenRepository")
	public ResponseEntity<?> handleGenRepository() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				handelGen.HandleGenerateRepository(packageNameSplit, listtBLColumn, conInfo);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateRepository(packageNameSplit, listtBLColumn, conInfo);
				}
			}
			return ResponseEntity.ok("handleGenRepository successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handleGenRepository failed: " + e.getMessage());
		}
	}

	@GetMapping("/handelGenDefineRepositoryToService")
	public ResponseEntity<?> handelGenDefineRepositoryToService() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleDefineRepositoryToService(packageNameSplit, listtBLColumn, conInfo);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleDefineRepositoryToService(packageNameSplit, listtBLColumn, conInfo);
				}
			}
			return ResponseEntity.ok("handelGenDefineRepositoryToService successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenDefineRepositoryToService failed: " + e.getMessage());
		}
	}

	@GetMapping("/handelGenControllerAPIBasic")
	public ResponseEntity<?> handelGenControllerAPIBasic() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				handelGen.HandlGenerateControllerAPIBasic(packageNameSplit, listtBLColumn, conInfo);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandlGenerateControllerAPIBasic(packageNameSplit, listtBLColumn, conInfo);
				}
			}
			return ResponseEntity.ok("handelGenControllerAPIBasic successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenControllerAPIBasic failed: " + e.getMessage());
		}
	}

	@GetMapping("/handleGenAdminRouter")
	public ResponseEntity<?> handleGenAdminRouter() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<String> listtBL = getAllTables.getAllTableNames();
			handelGen.handleGenerateAdminRouter(listtBL, conInfo);
			return ResponseEntity.ok("handleGenAdminRouter successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handleGenAdminRouter failed: " + e.getMessage());
		}
	}

	@GetMapping("/HandleGenIndex")
	public ResponseEntity<?> HandleGenIndexAndForm() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleGenerateIndex(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
						ExportedKeysInfos);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateIndex(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
							ExportedKeysInfos);
				}
			}
			return ResponseEntity.ok("handelGenControllerAPIBasic successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenControllerAPIBasic failed: " + e.getMessage());
		}

	}

	@GetMapping("/HandleGenForm")
	public ResponseEntity<?> HandleGenForm() {
		try {
			ConnectInfo conInfo = connectInfoHolder.getConnectInfo();
			List<ColumnInfo> listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
			List<String> packageNameSplit = Arrays.asList(conInfo.getBackEndSourceURL().split("\\\\"));
			if (!conInfo.getTblName().equals("All")) {
				List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, conInfo.getTblName());
				List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, conInfo.getTblName());
				handelGen.HandleGenerateForm(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
						ExportedKeysInfos);
			} else {
				List<String> listtBL = getAllTables.getAllTableNames();
				for (String tableItem : listtBL) {
					List<ForeignKeyInfo> ImportedKeysInfos = getAllTables.getImportedForeignKeys(ds, tableItem);
					List<ForeignKeyInfo> ExportedKeysInfos = getAllTables.getExportedForeignKeys(ds, tableItem);
					conInfo.setTblName(tableItem);
					listtBLColumn = getAllTables.getTableColumns(conInfo.getTblName());
					handelGen.HandleGenerateForm(packageNameSplit, listtBLColumn, conInfo, ImportedKeysInfos,
							ExportedKeysInfos);
				}
			}
			return ResponseEntity.ok("handelGenControllerAPIBasic successful.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("handelGenControllerAPIBasic failed: " + e.getMessage());
		}
	}

}
