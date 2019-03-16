class ColumnID {
   private String name;
   private boolean containsKeys;

   ColumnID() {
   }

   ColumnID(String name) {
      this.name = name;
      this.containsKeys = false;
   }

   ColumnID(String name, boolean containsKeys) {
      this.name = name;
      this.containsKeys = containsKeys;
   }

   String getName() {
      return this.name;
   }

   void setName(String name) {
      this.name = name;
   }

   boolean containsKeys() {
      boolean status = this.containsKeys;
      return status;
   }

   void setKeyStatus(boolean status) {
      this.containsKeys = status;
   }

   // --- testing ---

   void testColumnCreation() {
      String testStr = "test";
      String newTest = "testkey";
      ColumnID test0 = new ColumnID(testStr);
      assert(test0.getName().equals(testStr));
      assert(test0.containsKeys() == false);
      ColumnID test1 = new ColumnID(newTest, true);
      assert(test1.getName().equals(newTest));
      assert(test1.containsKeys() == true);
      test1.setKeyStatus(false);
      assert(test1.containsKeys() == false);
   }

   void runTests() {
      testColumnCreation();
   }

   public static void main(String[] args) {
      ColumnID program = new ColumnID();
      program.runTests();
   }
}
