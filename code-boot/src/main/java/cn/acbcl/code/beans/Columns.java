package cn.acbcl.code.beans;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("INFORMATION_SCHEMA.COLUMNS")
public class Columns {
    private String columnName; //列名
    private String tableSchema; //列名
    private String dataType; //字段类型
//    private String characterMaximunLength; //长度
    private String isNullable; //是否为空
    private String columnComment; //备注

    public void casing(){
        String columnName = this.columnName;
        String[] s = columnName.split("_");
        String successName = s[0] + "";

        if (s.length > 0) {
            for (int i = 1; i < s.length; i++) {
                successName += String.valueOf(s[i].toCharArray()[0]).toUpperCase() + s[i].substring(1);
            }
        }
        this.columnName=successName;
    }
}
