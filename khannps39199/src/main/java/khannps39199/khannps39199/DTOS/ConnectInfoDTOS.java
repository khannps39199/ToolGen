package khannps39199.khannps39199.DTOS;

import lombok.Data;

@Data
public class ConnectInfoDTOS {
	private String userName;
	private String password;
	private String dbName;
	private String tblName;
	private String backEndSourceURL;
	private String frontEndSourceURL;
}
