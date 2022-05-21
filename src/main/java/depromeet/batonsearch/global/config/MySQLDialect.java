package depromeet.batonsearch.global.config;

import org.hibernate.spatial.dialect.mysql.MySQL8SpatialDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;

public class MySQLDialect extends org.hibernate.spatial.dialect.mysql.MySQL8SpatialDialect {
    public MySQLDialect() {
        super();
        registerFunction("calc_distance", new SQLFunctionTemplate(DoubleType.INSTANCE, "ST_DISTANCE_SPHERE(st_pointfromtext(?1, 4326), ?2)"));
        registerFunction("geocontains", new SQLFunctionTemplate(BooleanType.INSTANCE, "MBRContains(st_linestringfromtext(?1, 4326), ?2)"));
        registerFunction("bitand", new SQLFunctionTemplate(IntegerType.INSTANCE, "(?1 & ?2)"));
    }
}