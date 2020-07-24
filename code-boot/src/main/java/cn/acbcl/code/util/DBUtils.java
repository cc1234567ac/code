package cn.acbcl.code.util;

public class DBUtils {
    public static String ConvertDBType(String dbType){
        switch (dbType){
            case "timestamp":
            case "datetime":
                return "LocalDateTime";
            case "bigint":
                return "Long";
            case "int":
                return "Integer";
            case"time":
                return "LocalTime";
            case"date":
                return "LocalDate";
            case"double":
                return "Double";
            case"float":
                return "Float";
            default:
                return "String";
        }
    }
    public static String casing(String columnNameP){
        String columnName = columnNameP;
        String[] s = columnName.split("_");
        String successName = s[0] + "";

        if (s.length > 0) {
            for (int i = 1; i < s.length; i++) {
                successName += String.valueOf(s[i].toCharArray()[0]).toUpperCase() + s[i].substring(1);
            }
        }
        return String.valueOf(successName.toCharArray()[0]).toUpperCase()+successName.substring(1);
    }
}
