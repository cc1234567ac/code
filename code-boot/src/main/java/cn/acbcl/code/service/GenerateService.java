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
    @Value("${p.db.name}")
    private String dbName;

    public void byTableName(String tableName) {
        List<Columns> table_name = columnsDao.selectList(new EntityWrapper<Columns>().eq("table_name", tableName).eq("table_schema",dbName));
        String classString = "";
        classString += "package " + packageName + ".beans;\n\n";
        classString += "import lombok.Data;\nimport lombok.ToString;\nimport lombok.Builder;\n";
        classString += "import com.fasterxml.jackson.annotation.JsonFormat;\n";
        classString += "import java.time.LocalDateTime;\n";
        classString += "\n\n@Data";
        classString += "\n@ToString";
        classString += "\n@Builder";
        classString += "\npublic class " + DBUtils.casing(tableName) + " {";
        for (Columns columns : table_name) {
            String type = DBUtils.ConvertDBType(columns.getDataType());
            String curCol = "";

            if (StringUtils.isBlank(columns.getColumnComment())) {
                columns.setColumnComment("无注释");
            }
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


            curCol += "\n    private " + type + " " + columns.getColumnName() + ";";
            classString += curCol;
        }
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
