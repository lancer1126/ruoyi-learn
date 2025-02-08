package fun.lance.admin.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

import java.sql.Types;

public class MybatisGeneratorTest {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder(
            "xxxx",
            "xxxx",
            "xxxx"
    );

    /**
     * 执行 run
     */
    public static void main(String[] args) {
        // 初始化数据库脚本
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("lancer") // 设置作者
                            .outputDir("D:\\code"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("fun.lance.admin.sys") // 设置父包名
                )
                .strategyConfig(builder ->
                        builder.addInclude("sys_config") // 设置需要生成的表名
                                .entityBuilder()
                                .enableLombok()
                                .controllerBuilder()
                                .enableRestStyle()
                )
                .execute();
        System.out.println("done");
    }
}
