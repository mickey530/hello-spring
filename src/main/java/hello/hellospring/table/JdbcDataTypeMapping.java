package hello.hellospring.table;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JdbcDataTypeMapping {
    private static final Map<Integer, String> dataTypeMap = new HashMap<>();

    static {
        // JDBC 데이터 타입 상수와 이름 매핑 초기화
        // Types 클래스의 모든 상수 값을 dataTypeMap에 저장
        for (Field field : Types.class.getFields()) {
            try {
                if (field.getType() == int.class) {
                    int value = field.getInt(null);
                    dataTypeMap.put(value, field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // JDBC 타입 코드를 받아 해당하는 데이터 타입 이름을 반환하는 메서드
    public static String getDataTypeName(int typeCode) {
        return dataTypeMap.getOrDefault(typeCode, "Unknown");
    }

    public static Map<Integer, String> getDataType() {
        return dataTypeMap;
    }
}
