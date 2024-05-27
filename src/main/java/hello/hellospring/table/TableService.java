package hello.hellospring.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {this.tableRepository = tableRepository; }

    private static List<TableDto> cachedTables;

    // 테이블 데이터를 로드하고 캐싱하는 메서드
    public synchronized List<TableDto> getTables() {
        if (cachedTables == null) {
            // 캐시가 비어있는 경우에만 데이터를 로드하고 캐싱
            cachedTables = loadTables();
        }
        return cachedTables;
    }

    // 테이블 데이터를 로드하는 메서드
    private List<TableDto> loadTables() {
        List<TableDto> tableList = new ArrayList<>();

        // 테이블명 전체 조회
        List<String> tables = tableRepository.getTables();

        // 각 테이블에 대해 컬럼 조회
        tables.forEach(tName -> {
            tableList.add(
                    TableDto.builder()
                            .tableName(tName)
                            .columnList(tableRepository.getColumns(tName))
                            .build());
        });

        return tableList;
    }

    public List<Map<String, String>> selectTbl(int tblIdx){

        // 선택한 테이블
        TableDto tableDto = cachedTables.get(tblIdx);

        System.out.println(tblIdx);
        System.out.println(tableDto.getTableName());

        return tableRepository.selectTbl(tableDto.getTableName());

    }

    public void insertData(int tblIdx, Map<String, Object> data) {

        TableDto table = getTables().get(tblIdx);
        tableRepository.insertData(table, data);

    }
    public void addColumn(int tblIdx, Map<String, String> data) {

        TableDto table = getTables().get(tblIdx);
        tableRepository.addColumn(table, data);

        // 컬럼 추가 성공시 테이블, 컬럼 데이터 다시 캐싱
        cachedTables = loadTables();

    }

    public void createTable(TableDto tableDto) {

        tableRepository.createTable(tableDto);

        // 테이블 생성 완료시, 테이블 테이터 다시 캐싱
        cachedTables = loadTables();

    }

}
