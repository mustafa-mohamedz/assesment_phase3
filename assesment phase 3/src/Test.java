import java.sql.*;
public class Test {
   // JDBC driver name and database URL
   static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
   //  Database credentials
   static final String USER = "postgres", PASS = "postgres123postgres";
   public static void main(String[] args) throws SQLException {
       try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS)){
           try(Statement stmt = conn.createStatement()) {
               String [] sql = {
            		   "SELECT ename FROM employee, department WHERE eid = manager_id and salary = (SELECT max(salary) FROM employee, department WHERE eid = manager_id and salary)" ,
            		   "SELECT pname FROM project, employee, works WHERE project_id = pid and eid = employee_id group by pname HAVING sum(salary) = (SELECT max(sum_salary) FROM (SELECT pname, sum(salary) sum_salary FROM project, employee, works WHERE project_id = pid AND eid = employee_id GROUP BY pname) x)",
            		   "SELECT pname FROM project WHERE pid not in ( select project_id from works )",
            		   "SELECT e.ename FROM employee e, employee m, department d WHERE d.manager_id = e.eid and m.department_id = d.did GROUP BY e.ename HAVING count(*) = (SELECT MAX(mycount) FROM (SELECT COUNT(*) mycount FROM employee GROUP BY department_id) x)",
            		   "SELECT dname FROM department, employee WHERE did = department_id GROUP BY dname HAVING count(*) = 4"
            		   };
               for (int i = 0; i < sql.length; i++) {
            	   try(ResultSet rs = stmt.executeQuery(sql[i])) {
                       int cols = rs.getMetaData().getColumnCount();
                       while(rs.next()){
                           for (int j=1; j<=cols; j++) System.out.print(rs.getObject(j) + ",\t");
                           System.out.println();
                       }
                   }
			}
               
           }
       }
   }
}
