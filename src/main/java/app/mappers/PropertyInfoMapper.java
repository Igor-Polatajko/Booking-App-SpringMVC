package app.mappers;

import app.domains.PropertyInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class PropertyInfoMapper implements RowMapper<PropertyInfo> {

    @Override
    public PropertyInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setId(resultSet.getString("id"));
        propertyInfo.setOwnerId(resultSet.getString("owner_id"));
        propertyInfo.setLocation(resultSet.getString("location"));
        propertyInfo.setDescription(resultSet.getString("description"));
        propertyInfo.setName(resultSet.getString("name"));
        propertyInfo.setCreatedDate(resultSet.getObject("created_date", LocalDateTime.class));
        propertyInfo.setUpdatedDate(resultSet.getObject("updated_date", LocalDateTime.class));
        return propertyInfo;
    }
}
