import com.brilliance.blacklist.common.DetectMethod;
import com.brilliance.blacklist.common.DomainType;
import com.brilliance.blacklist.dsimp.BlackListIndexWriter;
import com.brilliance.blacklist.dsimp.writer.LuceneIndexWriter;
import com.brilliance.blacklist.search.LuceneSearcher;
import com.brilliance.blacklist.search.LuceneUtil;
import com.brilliance.word.blacklist.search.Result;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Lane.Hu
 */

public class LuceneSearchTest {


    @Test
    public void search() throws Exception {
        String q = "Jean Bokassa";//
        String indexDir = "D:\\工作\\新晨科技\\邯郸银行\\黑名单\\blacklist_interface\\index";

        LuceneSearcher searcher = new LuceneSearcher(indexDir);
        List<Result> results = searcher.search(q, DomainType.NAME, DetectMethod.SIMILAR, 90);
        System.out.println(results.size());
        for (Result result : results) {
            System.out.println(result);
        }
    }

    @Test
    public void add() throws Exception {
        String q = "Jean Bokassa";//
        String indexDir = "D:\\工作\\新晨科技\\邯郸银行\\黑名单\\blacklist_interface\\index";
        String backupDir = "D:\\工作\\新晨科技\\邯郸银行\\黑名单\\backup";

        BlackListIndexWriter writer = new LuceneIndexWriter(indexDir,backupDir);
        writer.addDocument(DomainType.WNAME,q,"TEST0001");
        writer.commit();
        writer.close();
    }


}
