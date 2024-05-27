package hello.hellospring.table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    public TableController(TableService tableService){this.tableService = tableService; }

    @GetMapping("/tableList")
    public List<TableDto> showTables() {
        return tableService.getTables();
    }

    @GetMapping("/table/{tblIdx}")
    public List<Map<String, String>> selectTbl(@PathVariable int tblIdx) {
        return tableService.selectTbl(tblIdx);
    }

    @PostMapping("/table/{tblIdx}")
    public void insertData(@PathVariable int tblIdx, @RequestBody Map<String, Object> data) {

        System.out.println("tblIdx : " + tblIdx);
        System.out.println("data : " + data.toString());
        tableService.insertData(tblIdx, data);

    }
    @PostMapping("/table/{tblIdx}/addColumn")
    public void addColumn(@PathVariable int tblIdx, @RequestBody Map<String, String> data) {

        System.out.println("tblIdx : " + tblIdx);
        System.out.println("data : " + data.toString());
        tableService.addColumn(tblIdx, data);

    }

@PostMapping("/table/create")
    public void createTable(@RequestBody TableDto tableDto) {

        System.out.println("data : " + tableDto.toString());
        tableService.createTable(tableDto);

    }


    @GetMapping("datatype")
    public Map<Integer, String> helloString(){
        return JdbcDataTypeMapping.getDataType();
    }

}
