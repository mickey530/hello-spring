package hello.hellospring.table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getTables() {
        return jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' ORDER BY TABLE_NAME", String.class);
    }

    public List<ColumnDto> getColumns(String tableName) {
        String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = 'PUBLIC'";
        return jdbcTemplate.query(sql, new Object[]{tableName}, (rs, rowNum) -> {
            ColumnDto columnDto = new ColumnDto();
            columnDto.setColumnName(rs.getString("COLUMN_NAME"));
            columnDto.setDataType(JdbcDataTypeMapping.getDataTypeName(rs.getInt("DATA_TYPE")));
            return columnDto;
        });
    }

    public List<Map<String, String>> selectTbl(String tblNm) {
        String sql = "SELECT * FROM " + tblNm;
        System.out.println(sql);
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // ResultSet에서 데이터를 읽어와 Map으로 변환하여 반환
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, String> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnValue = rs.getString(columnName);
                row.put(columnName, columnValue);
            }
            return row;
        });
    }

    public void insertData(TableDto table, Map<String, Object> data){

        // 테이블명
        String tblNm = table.getTableName();

        // ID 값 계산
        String idSql = "SELECT COALESCE(MAX(ID), 0) + 1 FROM " + tblNm;
        int newId = jdbcTemplate.queryForObject(idSql, Integer.class);

        // 컬럼명 리스트와 값 리스트 생성
        List<String> columnNames = table.getColumnList().stream()
                .map(ColumnDto::getColumnName)
                .collect(Collectors.toList());

        // 값을 리스트로 변환하고 ID 값을 추가
        List<Object> values = columnNames.stream()
                .map(columnName -> columnName.equals("ID") ? newId : data.get(columnName))
                .collect(Collectors.toList());

        // 컬럼명과 값 리스트를 CSV 형식으로 변환
        String columns = String.join(", ", columnNames);
        String placeholders = columnNames.stream().map(c -> "?").collect(Collectors.joining(", "));

        // SQL 쿼리 생성
        String sql = "INSERT INTO " + tblNm + " (" + columns + ") VALUES (" + placeholders + ")";

        // 쿼리 실행
        jdbcTemplate.update(sql, values.toArray());

        // 확인용 출력
        System.out.println("SQL: " + sql);
        System.out.println("Values: " + values);

    }

    public void addColumn(TableDto table, Map<String, String> data) {
        String columnName = data.get("columnName");
        String dataType = data.get("dataType");

        if (columnName == null || dataType == null) return;

        String sql = "ALTER TABLE " + table.getTableName() + " ADD COLUMN " + columnName + " " + dataType;
        System.out.println("SQL: " + sql);
        jdbcTemplate.execute(sql);
    }

    public void createTable(TableDto tableDto) {
        String tableName = tableDto.getTableName();
        List<ColumnDto> columns = tableDto.getColumnList();

        String columnsDefinition = columns.stream()
                .map(column -> column.getColumnName() + " " + column.getDataType())
                .collect(Collectors.joining(", "));

        String sql = "CREATE TABLE " + tableName + " (" + columnsDefinition + ")";

        jdbcTemplate.execute(sql);
    }

}
