package tests.myTests.testUtils;

import java.util.Arrays;

public class RequiredData {

    private static final String[] tableNames =
            {"table", "parent_table", "child_table", "first_partner_table",
                    "second_partner_table", "business_table", "users_table", "profile_table"};
    private static final String[] tableTypes = {"small", "medium", "large", "huge"};

    private static String getTablesByType(String type) {
        if (!Arrays.asList(tableTypes).contains(type)) {
            throw new RuntimeException("Incorrect table size type, it should be: small, medium, large, huge, all");
        }
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < tableNames.length; i++) {
            query.append("select 1 from ").append(type).append("_").append(tableNames[i]);
            if (i != tableNames.length - 1) {
                query.append(" union all ");
            }
        }
        return query.toString();
    }

    public static String checkTables(String type) {
        return getTablesByType(type);
    }
}
