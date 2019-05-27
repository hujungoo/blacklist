import com.brilliance.eibs.core.service.ResultMsg;
import com.brilliance.eibs.main.Client;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class InterfaceTest {

    private Client client;

    @Before
    public void init() {
        client = new Client();
    }

    @Test
    public void simJson() throws IOException {
        String path = "D:\\工作\\新晨科技\\邯郸银行\\黑名单\\index1";
        client.call("sim", "json", new Object[]{path});

    }

    @Test
    public void simSwift() throws IOException {
        String path = "D:\\工作\\新晨科技\\邯郸银行\\黑名单\\swift\\example\\103(103+202)1.sf2";

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            boolean var4 = true;

            int len;
            while ((len = br.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }

            String result = sb.toString();
            ResultMsg resultMsg = client.call("sim", "swift", new Object[]{result.substring(1, result.length() - 1)});

            Map<String, Object> m = (Map<String, Object>) resultMsg.getContent();
            Object count = m.get("count");
            if (null != count) {
                Integer count1 = Integer.valueOf((String) count);
                Object[] msgids = (Object[]) m.get("msgid");
                Object[] svalues = (Object[]) m.get("svalue");
                Object[] rlevels = (Object[]) m.get("rlevel");
                Object[] rpercents = (Object[]) m.get("rpercent");


                for (int i = 0; i < count1; i++) {
                    StringBuffer sb1 = new StringBuffer();
                    sb1.append((i + 1) + "|" + msgids[i] + "|" + svalues[i] + "|" + rlevels[i] + "|" + rpercents[i]);
                    System.out.println(sb1.toString());
                }
            }
        } finally {
            IOUtils.closeQuietly(br);
        }


    }

    @Test
    public void test() {
        client.call("test", "test1");
    }

    @Test
    public void sim() {
        String[] data = {"Jean Bokassa"};
        String type = "NAME";
        String strat = "2";

        ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

        Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
        Object[] names = m.get("value");
        Object[] types = m.get("type");
        Object[] rlevels = m.get("rlevel");
        Object[] rpercents = m.get("rpercent");

        if (null != names) {
            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(data[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }
        }
    }


    @Test
    public void testBatchBlackList() throws IOException {
        int size = 10;
        int len = 10;//随机单词长度
        String type = "ADDRESS";
        String strat = "2";
        String path = "D:/工作/新晨科技/邯郸银行/黑名单/测试/text/" + type.toLowerCase();

        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            int total = getFileTotalNum(path);
            Random random = new Random();

            String[] ori = new String[size];
            String[] data = new String[size];
            for (int i = 0; i < size; i++) {
                StringBuffer sb = new StringBuffer();
                int num = random.nextInt(total);
                lnr.setLineNumber(num);
                String line = lnr.readLine();
                line = line.split("\\|")[0];
                ori[i] = line;
                sb.append(randomStr(len)).append(" ").append(line).append(" ").append(randomStr(len));
                data[i] = sb.toString();
            }

            ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

            Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
            Object[] names = m.get("value");
            Object[] types = m.get("type");
            Object[] rlevels = m.get("rlevel");
            Object[] rpercents = m.get("rpercent");

            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(ori[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }

    }

    @Test
    public void testBatchBlackList1() throws IOException {
        int size = 10;
        String type = "ADDRESS";
        String path = "D:/工作/新晨科技/邯郸银行/黑名单/测试/text/" + type.toLowerCase();
        String strat = "2";

        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            int total = getFileTotalNum(path);
            Random random = new Random();

            String[] ori = new String[size];
            String[] data = new String[size];
            for (int i = 0; i < size; i++) {
                StringBuffer sb = new StringBuffer();
                String line = getStrNoBlank(random, total, lnr);
                ori[i] = line;

                String[] fields = line.split(" ");
                List<String> fieldLst = Arrays.asList(fields);
                fieldLst = new ArrayList<String>(fieldLst);
                fieldLst.remove(random.nextInt(fields.length));

                for (int j = 0; j < fieldLst.size(); j++) {
                    sb.append(fieldLst.get(j));
                    if (j != fieldLst.size() - 1) {
                        sb.append(" ");
                    }
                }
                data[i] = sb.toString();
            }

            ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

            Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
            Object[] names = m.get("value");
            Object[] types = m.get("type");
            Object[] rlevels = m.get("rlevel");
            Object[] rpercents = m.get("rpercent");

            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(ori[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }

    }

    @Test
    public void testBatchBlackList2() throws IOException {
        String type = "ADDRESS";
        int size = 10;
        int len = 10;
        String strat = "2";
        String path = "D:/工作/新晨科技/邯郸银行/黑名单/测试/text/" + type.toLowerCase();

        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            int total = getFileTotalNum(path);
            Random random = new Random();

            String[] ori = new String[size];
            String[] data = new String[size];
            for (int i = 0; i < size; i++) {
                StringBuffer sb = new StringBuffer();
                String line = getStrNoBlank(random, total, lnr);
                ori[i] = line;

                String[] fields = line.split(" ");
                List<String> fieldLst = Arrays.asList(fields);
                fieldLst = new ArrayList<String>(fieldLst);

                int index = random.nextInt(fieldLst.size() - 1);
                index = index == 0 ? index + 1 : index;

                for (int j = 0; j < fieldLst.size(); j++) {
                    if (index == j) {
                        sb.append(randomStr(len)).append(" ");
                    }
                    sb.append(fieldLst.get(j));
                    if (j != fieldLst.size() - 1) {
                        sb.append(" ");
                    }
                }
                data[i] = sb.toString();
            }

            ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

            Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
            Object[] names = m.get("value");
            Object[] types = m.get("type");
            Object[] rlevels = m.get("rlevel");
            Object[] rpercents = m.get("rpercent");

            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(ori[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }

    }

    @Test
    public void testBatchBlackList3() throws IOException {
        int size = 10;
        double percent = 0.5;
        String type = "ADDRESS";
        String path = "D:/工作/新晨科技/邯郸银行/黑名单/测试/text/" + type.toLowerCase();
        String strat = "2";

        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            int total = getFileTotalNum(path);
            Random random = new Random();

            String[] ori = new String[size];
            String[] data = new String[size];
            for (int i = 0; i < size; i++) {
                StringBuffer sb = new StringBuffer();
                String line = getStrNoBlank(random, total, lnr);
                ori[i] = line;

                String[] fields = line.split(" ");
                int index = random.nextInt(fields.length - 1);
                fields[index] = rendomReplacePercentStr(fields[index], percent);

                for (int j = 0; j < fields.length; j++) {
                    sb.append(fields[j]);
                    if (j != fields.length - 1) {
                        sb.append(" ");
                    }
                }

                data[i] = sb.toString();
            }

            ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

            Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
            Object[] names = m.get("value");
            Object[] types = m.get("type");
            Object[] rlevels = m.get("rlevel");
            Object[] rpercents = m.get("rpercent");

            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(ori[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }

    }

    @Test
    public void testBatchBlackList4() throws IOException {
        String type = "ADDRESS";
        int size = 10;
        double percent = 0.3;
        String strat = "2";
        String path = "D:/工作/新晨科技/邯郸银行/黑名单/测试/text/" + type.toLowerCase();

        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            int total = getFileTotalNum(path);
            Random random = new Random();

            String[] ori = new String[size];
            String[] data = new String[size];
            for (int i = 0; i < size; i++) {
                StringBuffer sb = new StringBuffer();
                String line = getStrNoBlank(random, total, lnr);
                ori[i] = line;

                data[i] = rendomReplacePercentStr(line, percent);
            }

            ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

            Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
            Object[] names = m.get("value");
            Object[] types = m.get("type");
            Object[] rlevels = m.get("rlevel");
            Object[] rpercents = m.get("rpercent");

            for (int i = 0; i < names.length; i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(i + 1).append("|").append(ori[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
                System.out.println(sb.toString());
            }

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }

    }

    @Test
    public void testBatchBlackListCountry() throws IOException {
        String type = "COUNTRY";
        String[] data = {"RO", "Romania", "罗马尼亚", "Saudi Arabia"};
        String strat = "1";

        ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

        Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
        Object[] names = m.get("value");
        Object[] types = m.get("type");
        Object[] rlevels = m.get("rlevel");
        Object[] rpercents = m.get("rpercent");

        for (int i = 0; i < names.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(i + 1).append("|").append(data[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
            System.out.println(sb.toString());
        }

    }

    @Test
    public void testBatchBlackListIDNum() throws IOException {
        String type = "IDNUM";
        String[] data = {"CFI.001", "CAR", "Test"};
        String strat = "1";

        ResultMsg resultMsg = client.call("sim", "sim", new Object[]{data, type, strat});

        Map<String, Object[]> m = (Map<String, Object[]>) resultMsg.getContent();
        Object[] names = m.get("value");
        Object[] types = m.get("type");
        Object[] rlevels = m.get("rlevel");
        Object[] rpercents = m.get("rpercent");

        for (int i = 0; i < names.length; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(i + 1).append("|").append(data[i]).append("|").append(types[i]).append("|").append(names[i]).append("|").append(rlevels[i]).append("|").append(rpercents[i]);
            System.out.println(sb.toString());
        }

    }

    private String rendomReplacePercentStr(String s, double percent) {
        Random random = new Random();
        int len = s.length();
        int count = (int) (len * percent);
        HashSet<Integer> set = new HashSet<Integer>();
        while (set.size() < count + 1) {
            set.add(random.nextInt(len));
        }
        StringBuffer sb = new StringBuffer(s);
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            int i = iter.next();
            sb.replace(i, i + 1, randomChar() + "");
        }
        return sb.toString();
    }

    private String getStrNoBlank(Random random, int total, LineNumberReader lnr) throws IOException {
        int num = random.nextInt(total);
        lnr.setLineNumber(num);
        String line = lnr.readLine();
        line = line.split("\\|")[0];

        String[] fields = line.split(" ");
        int count = fields.length;
        if (count > 1)
            return line;
        else {
            return getStrNoBlank(random, total, lnr);
        }
    }

    private String randomStr(int len) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int size = random.nextInt(len);
        for (int i = 0; i < size; i++) {
            sb.append(randomChar());
        }
        return sb.toString();
    }

    private char randomChar() {
        return (char) (int) (Math.random() * 26 + 97);
    }

    private int getFileTotalNum(String path) throws IOException {
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            File file = new File(path);//文件路径
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);

            lnr.skip(file.length());

            return lnr.getLineNumber();

        } finally {
            if (null != fr) fr.close();
            if (null != lnr) lnr.close();
        }
    }

}
