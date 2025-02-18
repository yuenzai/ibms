package cn.ecosync.ibms.gateway.exception;

import cn.ecosync.ibms.util.CollectionUtils;
import lombok.ToString;

import java.util.List;

@ToString
public class ExcelAnalysisException extends RuntimeException {
    private List<Cell> cells;

    protected ExcelAnalysisException() {
    }

    public ExcelAnalysisException(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return CollectionUtils.nullSafeOf(cells);
    }

    @ToString
    public static class Cell {
        private Integer rowIndex;
        private Integer columnIndex;
        private Object cellData;

        protected Cell() {
        }

        public Cell(Integer rowIndex, Integer columnIndex, Object cellData) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.cellData = cellData;
        }

        public Integer getRowIndex() {
            return rowIndex;
        }

        public Integer getColumnIndex() {
            return columnIndex;
        }

        public Object getCellData() {
            return cellData;
        }
    }
}
