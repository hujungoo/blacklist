import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        String[] strs = {"NAME", "ADDRESS"};
        List<String> list = Arrays.asList(strs);
        System.out.println(list.contains("NAME"));
        System.out.println(list.contains("ADDRESS"));
    }
}
