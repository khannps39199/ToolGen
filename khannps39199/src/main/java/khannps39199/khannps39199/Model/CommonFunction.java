package khannps39199.khannps39199.Model;

import java.util.ArrayList;
import java.util.List;

public class CommonFunction {
	public String[] HandelSplitName(String input) {
		String[] splitName = {};
		if (input.contains("_")) {
			splitName = input.split("_");
			return splitName;
		}
		return splitName;
	}

	public String firstUpCase(String input) {
		return Character.toUpperCase(input.charAt(0)) + input.substring(1);
	}

	public String firstLowCase(String input) {
		return Character.toLowerCase(input.charAt(0)) + input.substring(1);
	}

	public String ConvertToClassName(String input) {
		String result = "";
		if (input.contains("_")) {
			String[] extractName = HandelSplitName(input);
			for (int i = 0; i < extractName.length; i++) {
				result += firstUpCase(extractName[i]);
			}
			return result;
		}
		return firstUpCase(input);
	}

	public String ConvertToVariableName(String input) {
		String result = "";
		if (input.contains("_")) {
			String[] extractName = HandelSplitName(input);
			int incols = 0;
			if (input.contains("id")) {
				incols = -1;
			}
			result = extractName[0];
			for (int i = 1; i < extractName.length - incols; i++) {
				result += firstUpCase(extractName[i]);
			}
			return result;
		}
		return firstLowCase(input);
	}
}
