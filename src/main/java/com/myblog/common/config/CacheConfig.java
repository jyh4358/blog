package com.myblog.common.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        CacheConfiguration sideBarCategoryConfiguration = new CacheConfiguration()
                .eternal(false) // 저장된 캐시를 제거할지 여부. 설정이 true 인 경우 저장된 캐시는 제거되지 않으며 timeToIdleSeconds, timeToLiveSeconds 설정은 무시
                .timeToIdleSeconds(1800)    // 생성후 해당 시간 동안 캐쉬가 사용되지 않으면 삭제, 0은 삭제되지 않음
                .timeToLiveSeconds(1800)    // 생성후 해당 시간이 지나면 캐쉬는 삭제. 0은 삭제되지 않음
                .maxEntriesLocalHeap(1000)      // 메모리에 생성될 객체의 최대 수
                .memoryStoreEvictionPolicy("LRU") // maxEntriesLocalHeap 설정 값에 도달했을때 설정된 정책에 따라 객체가 제거
                .name("sideBarCategoryCaching");

        Cache sadeBarCategoryCache = new Cache(sideBarCategoryConfiguration);
//        Objects.requireNonNull(ehCacheManagerFactoryBean().getObject()).addCache(sadeBarCategoryCache);
        ehCacheManagerFactoryBean.getObject().addCache(sadeBarCategoryCache);

        CacheConfiguration sideBarRecentCommentConfiguration = new CacheConfiguration()
                .eternal(false)
                .timeToIdleSeconds(1800)
                .timeToLiveSeconds(1800)
                .maxEntriesLocalHeap(100)
                .memoryStoreEvictionPolicy("LRU")
                .name("sideBarRecentCommentCaching");

        Cache sideBarRecentCommentCache = new Cache(sideBarRecentCommentConfiguration);
        ehCacheManagerFactoryBean.getObject().addCache(sideBarRecentCommentCache);

        return new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());

    }
}
