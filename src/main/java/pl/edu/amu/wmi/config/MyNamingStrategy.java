package pl.edu.amu.wmi.config;

import com.google.common.base.CaseFormat;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.io.Serializable;

public class MyNamingStrategy extends SpringPhysicalNamingStrategy implements Serializable {

    public static final MyNamingStrategy INSTANCE = new MyNamingStrategy();

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        String result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.getText());
        return new Identifier(result, name.isQuoted());
    }
}