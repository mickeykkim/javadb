import java.io.*;
import java.math.*;

class Print {
   private int[] colWidth;

   private static final String X_DIV = "+";
   private static final String H_DIV = "-";
   private static final String V_DIV = "|";
   private static final String EMPTY = " ";
   private static final String NEWLN = "\n";
   private static final int XPADD = 2;
   private static final int HPADD = XPADD/2;

   Print() {
      this.colWidth = new int[0];
   }

   // return a copy of the selected width
   int getColWidth(int i) {
      int ret = this.colWidth[i];
      return ret;
   }

   // set initial column widths from column names
   void setInitialWidths(Table inputTable, int colNum) {
      this.colWidth = new int[colNum];
      for (int i = 0; i < colNum; i++) {
         this.colWidth[i] = inputTable.getColumnName(i).length();
      }
   }

   // get max column widths from record fields
   void setMaxWidths(Table inputTable) {
      int colNum = inputTable.getColumnSize();
      setInitialWidths(inputTable, colNum);
      for (int i = 0; i < inputTable.getRecordSize(); i++) {
         for (int j = 0; j < colNum; j++) {
            this.colWidth[j] = 
               Math.max(this.colWidth[j], inputTable.select(i).getField(j).length());
         }
      }
   }

   // Returns string like "+---+---+---+\n"
   String generateHorizontalDivider(Table inputTable) {
      int colsz = inputTable.getColumnSize();
      StringBuilder horDivBuilder = new StringBuilder();
      for (int i = 0; i < colsz; i++) {
         horDivBuilder.append(X_DIV);
         for (int j = 0; j < getColWidth(i) + XPADD; j++) {
            horDivBuilder.append(H_DIV);
         }
      }
      horDivBuilder.append(X_DIV);
      horDivBuilder.append(NEWLN);
      return horDivBuilder.toString();
   }

   // Returns string like "| column 1 | column 2 | column 3 |\n"
   String generateColumnsString(Table inputTable) {
      int colsz = inputTable.getColumnSize();
      StringBuilder columnsBuilder = new StringBuilder();
      for (int i = 0; i < colsz; i++) {
         String currName = inputTable.getColumnName(i);
         columnsBuilder.append(V_DIV);
         columnsBuilder.append(EMPTY);
         columnsBuilder.append(currName);
         for (int j = 0; j < getColWidth(i) - currName.length() + HPADD; j++) {
            columnsBuilder.append(EMPTY);
         }
      }
      columnsBuilder.append(V_DIV);
      columnsBuilder.append(NEWLN);
      return columnsBuilder.toString();
   }

   // Returns string like "| record 1 | record 2 | record 3 |\n"
   String generateRecordString(Table inputTable, int recordNum) {
      int colsz = inputTable.getColumnSize();
      if (recordNum > colsz || recordNum < 0) {
         System.out.println("No such field in record.");
         throw new IndexOutOfBoundsException();
      }
      StringBuilder recordsBuilder = new StringBuilder();
      for (int i = 0; i < colsz; i++) {
         String currField = inputTable.select(recordNum).getField(i);
         recordsBuilder.append(V_DIV);
         recordsBuilder.append(EMPTY);
         recordsBuilder.append(currField);
         for (int j = 0; j < getColWidth(i) - currField.length() + HPADD; j++) {
            recordsBuilder.append(EMPTY);
         }
      }
      recordsBuilder.append(V_DIV);
      recordsBuilder.append(NEWLN);
      return recordsBuilder.toString();
   }

   // Returns string   +---+---+---+
   // in the format:   | 1 | 2 | 3 |
   //                  +---+---+---+
   //                  | a | b | c |
   //                  +---+---+---+
   String printTableToString(Table inputTable) {
      int colsz = inputTable.getColumnSize();
      int recsz = inputTable.getRecordSize();
      String horDiv = generateHorizontalDivider(inputTable);
      StringBuilder tableStringBuilder = new StringBuilder();
      tableStringBuilder.append(horDiv);
      // i = -1 for column headers; i = 0..recsz for records
      for (int i = -1; i < recsz; i++) {
         if (i == -1) {
            tableStringBuilder.append(generateColumnsString(inputTable));
            tableStringBuilder.append(horDiv);
         } else {
            tableStringBuilder.append(generateRecordString(inputTable, i));
         }
      }
      tableStringBuilder.append(horDiv);
      return tableStringBuilder.toString();
   }

   // --- testing ---

   void testPrintingMethods() {
      Print testPrint = new Print();
      String testStr = "test_table";
      // make tables
      Table testTable = new Table(testStr);
      testTable.setColumnNames("1", "2", "3");
      Record testR1 = new Record("a", "b", "c");
      testTable.add(testR1);
      Record testR2 = new Record("dd", "eee", "fff");
      testTable.add(testR2);
      Record testR3 = new Record("gg", "hh", "iiii");
      testTable.add(testR3);
      testPrint.setInitialWidths(testTable, testTable.getColumnSize());
      assert(testPrint.getColWidth(0)==1);
      assert(testPrint.getColWidth(1)==1);
      assert(testPrint.getColWidth(2)==1);
      testPrint.setMaxWidths(testTable);
      assert(testPrint.getColWidth(0)==2);
      assert(testPrint.getColWidth(1)==3);
      assert(testPrint.getColWidth(2)==4);
      assert(testPrint.generateHorizontalDivider(testTable).equals(
         "+----+-----+------+\n"
      ));
      assert(testPrint.generateColumnsString(testTable).equals(
         "| 1  | 2   | 3    |\n"
      ));
      assert(testPrint.generateRecordString(testTable, 0).equals(
         "| a  | b   | c    |\n"
      ));
      assert(testPrint.generateRecordString(testTable, 1).equals(
         "| dd | eee | fff  |\n"
      ));
      assert(testPrint.generateRecordString(testTable, 2).equals(
         "| gg | hh  | iiii |\n"
      ));
      assert(testPrint.printTableToString(testTable).equals(
         "+----+-----+------+\n" +
         "| 1  | 2   | 3    |\n" +
         "+----+-----+------+\n" +
         "| a  | b   | c    |\n" +
         "| dd | eee | fff  |\n" +
         "| gg | hh  | iiii |\n" +
         "+----+-----+------+\n"
      ));
   }

   void runTests() {
      testPrintingMethods();
   }

   public static void main(String[] args) {
      Print program = new Print();
      program.runTests();
   }
}
