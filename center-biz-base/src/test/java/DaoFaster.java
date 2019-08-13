

import utils.MyBatisFaster;

import java.util.HashMap;
import java.util.Map;

public class DaoFaster {

    private static final String TABLE_NAME = "tableName";
    private static final String DAO_NAME = "daoName";
    private static final String DOMAIN_NAME = "domainName";

    private static final String IP_PORT = "localhost:3306";
    private static final String DB_NAME = "center";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String PREFIX = "com.xiaoliu.centerbiz";

    public static void main(String[] args) {
        try {
            table("module, module_button, role, role_module, role_module_button, role_user, user");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void table(String tables) throws Exception {
        String[] split = tables.split(",");
        for (String s : split) {
            if (s != null && !"".equals(s)) {
                Map<String, String> outStr = outStr(s.trim());

                MyBatisFaster faster = new MyBatisFaster();
                faster.setupConnection(IP_PORT, DB_NAME, USERNAME, PASSWORD, MyBatisFaster.MYSQL);
                faster.tableName(outStr.get(TABLE_NAME)).DAOInterfacePackage(PREFIX + ".dao")
                        .DAOName(outStr.get(DAO_NAME)).domainName(outStr.get(DOMAIN_NAME))
                        .domainPackage(PREFIX + ".domain");

                faster.outSQLMapper(System.getProperty("user.dir") + "\\src\\main\\resources\\mapper", MyBatisFaster.MYSQL);
                faster.outDAOInterface(System.getProperty("user.dir"), MyBatisFaster.MYSQL);
                faster.outDomain(System.getProperty("user.dir"), MyBatisFaster.MYSQL);
            }
        }
    }

    private static Map<String, String> outStr(String tableName) {
        if (tableName == null) {
            return null;
        }
        String[] split = tableName.split("_");
        String domainName = "";
        for (String s : split) {
            String string = s.toLowerCase();
            domainName += string.substring(0, 1).toUpperCase()
                    + (string.length() > 1 ? string.subSequence(1, string.length()) : "");
        }

        Map<String, String> outMap = new HashMap<String, String>();
        outMap.put(TABLE_NAME, tableName);
        outMap.put(DOMAIN_NAME, domainName);
        outMap.put(DAO_NAME, "I" + domainName + "Dao");
        return outMap;
    }
}
