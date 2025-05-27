package khannps39199.khannps39199.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
	private String fkColumn;
	private String pkTable;
	private String pkColumn;
}
