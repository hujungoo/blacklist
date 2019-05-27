package com.brilliance.word.blacklist.loader;

import com.brilliance.blacklist.common.DetectMethod;
import com.brilliance.blacklist.common.DomainType;
import com.brilliance.blacklist.search.LuceneSearcher;
import com.brilliance.word.blacklist.search.Result;

import java.io.IOException;
import java.util.List;

public class BlackListLucenceSearchLoader {

    private static String indexDir;
    private LuceneSearcher searcher;

    private BlackListLucenceSearchLoader(String indexDir) {
        try {
            this.searcher = new LuceneSearcher(indexDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonInstance {
        private static final BlackListLucenceSearchLoader INSTANCE = new BlackListLucenceSearchLoader(indexDir);
    }

    public static BlackListLucenceSearchLoader getInstance() {
        if (null == BlackListLucenceSearchLoader.indexDir) {
            throw new RuntimeException("SearchLoader未初始化");
        }
        return SingletonInstance.INSTANCE;
    }

    public static void init(String indexDir) {
        BlackListLucenceSearchLoader.indexDir = indexDir;
    }

    public synchronized List<Result> shot(String content, Double percent, String type, String detect) {
        return searcher.search(content, DomainType.valueOf(type), DetectMethod.valueOf(detect), percent.intValue());
    }

    public synchronized List<Result> shot(String content, Double percent, String type) {
        return searcher.search(content, DomainType.valueOf(type), DetectMethod.SIMILAR, percent.intValue());
    }

    @Deprecated
    public synchronized void load(String indexDir) throws IOException {
        BlackListLucenceSearchLoader.indexDir = indexDir;
        this.searcher.close();
        this.searcher = new LuceneSearcher(indexDir);
    }

}
