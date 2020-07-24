package cn.acbcl.code.service;

import cn.acbcl.code.beans.Columns;
import cn.acbcl.code.dao.ColumnsDao;
import cn.acbcl.code.util.DBUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.regex.Matcher;

@Service
public class GenerateService {


    @Resource
    private ColumnsDao columnsDao;

    @Value("${package.name}")
    private String packageName;
    @Value("${camel.casing}")
    private boolean camelCasing;

    public void byTableName(String tableName) {
        List<Columns> table_name = columnsDao.selectList(new EntityWrapper<Columns>().eq("table_name", tableName));
        String classString = "";
        String classBuilderString = "";
        String builderClassString = "";
        classString += "package " + packageName + ".beans;\n\n";
        classString += "import lombok.Data;\nimport lombok.ToString;\n";
        classString += "import com.fasterxml.jackson.annotation.JsonFormat;\n";
        classString += "import java.time.LocalDateTime;\n";
        classBuilderString += "\n\n\n    public static Builder create() {";
        classBuilderString += "\n        return new Builder();";
        classBuilderString += "\n    }";
        classBuilderString += "\n\n    public " + DBUtils.casing(tableName) + "(Builder builder) {";
        classString += "\n\n@Data";
        classString += "\n@ToString";
        classString += "\npublic class " + DBUtils.casing(tableName) + " {";
        builderClassString += "\n\n    @Data";
        builderClassString += "\n    public static class Builder {";
        for (Columns columns : table_name) {
            String type = DBUtils.ConvertDBType(columns.getDataType());
            String curCol = "";
            curCol += "\n    // " + columns.getColumnComment() + "";
            if (type.equalsIgnoreCase("LocalDateTime")) {
                curCol += "\n     @JsonFormat(pattern = \"yyyy-MM-dd\", timezone = \"GMT+8\")";
            } else if (type.equalsIgnoreCase("LocalDate")) {
                curCol += "\n     @JsonFormat(pattern = \"yyyy\", timezone = \"GMT+8\")";
            } else if (type.equalsIgnoreCase("LocalTime")) {
                curCol += "\n     @JsonFormat(pattern = \"MM-dd\", timezone = \"GMT+8\")";
            }
            if (camelCasing) {
                columns.casing();
            }
            if (StringUtils.isBlank(columns.getColumnComment())) {
                columns.setColumnComment("无注释");
            }

            curCol += "\n    private " + type + " " + columns.getColumnName() + ";";
            classString += curCol;
            classBuilderString += "\n        this." + columns.getColumnName() + " = builder." + columns.getColumnName()+";";
            builderClassString += "\n        private " + type + " " + columns.getColumnName() + ";";
            builderClassString += "\n\n        public Builder add" + String.valueOf(columns.getColumnName().toCharArray()[0]).toUpperCase() + columns.getColumnName().substring(1) + "(" + type + " " + columns.getColumnName() + ") {";
            builderClassString += "\n            this." + columns.getColumnName() + " = " + columns.getColumnName() + ";";
            builderClassString += "\n            return this;";
            builderClassString += "\n        }\n";
        }

        builderClassString += "\n        public "+DBUtils.casing(tableName)+" build() {";
        builderClassString += "\n            return new "+DBUtils.casing(tableName)+"(this);";
        builderClassString += "\n        }";
        classBuilderString += "\n    }";
        builderClassString += "\n    }";


        classString += classBuilderString;
        classString += builderClassString;

        classString += "\n}";

        write(tableName, classString);

    }

    private void write(String name, String text) {
        try {
            String casing = DBUtils.casing(name);
            casing = String.valueOf(casing.toCharArray()[0]).toUpperCase() + casing.substring(1);
            String path = "c:\\code\\" + packageName.replaceAll("[.]", Matcher.quoteReplacement(File.separator)) + "\\" + casing + ".java";
            File file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
