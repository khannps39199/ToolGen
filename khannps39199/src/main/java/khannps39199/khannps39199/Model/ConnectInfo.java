package khannps39199.khannps39199.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectInfo {

	private String userName;
	private String password;
	private String dbName;
	private String tblName;
	private String backEndSourceURL;
	private String frontEndSourceURL;
	private ForeignKeyInfo foreignKeyInfo;
}