package service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
//xoa dinh ky, kich thuoc toi da cua cache
public class CacheTTL<K,V> implements Map<K,V> {
    private int m; //time to live
    private int n; //time access

    public CacheTTL(int n, int m) {
        this.n = n*1000;
        this.m = m*1000;
    }
    private class CacheEntry{
        V value;
        long createdTime;
        long lastTimeAccess;

        public CacheEntry(V value) {
            this.value = value;
            this.createdTime = System.currentTimeMillis();
            this.lastTimeAccess = System.currentTimeMillis();
        }
    }
    private Map<K,CacheEntry> cache=new HashMap<>();
    private int totalAccess=0;
    private int hitRate=0;

    @Override
    public V get(Object key){
        totalAccess++;
        CacheEntry entry=cache.get(key);
        if(entry ==null) return null;
        long now=System.currentTimeMillis();
        if((now-entry.createdTime)>m || (now-entry.lastTimeAccess)>n){
            cache.remove(key);
            return null;
        }else{
            entry.lastTimeAccess=now;
            hitRate++;
            return entry.value;
        }
    }

    @Override
    public V put(K key, V value) {
        CacheEntry entry=new CacheEntry(value);
        CacheEntry oldEntry=cache.put(key,entry);
        return oldEntry != null ? oldEntry.value : null;
    }

    public Map<K,V> getMap(){
        Map<K,V> map=new HashMap<>();
        Set<K> set=cache.keySet();
        for(Map.Entry<K, CacheEntry> e : cache.entrySet()){ // 3. Duyệt từng cặp key-value trong cache
            CacheEntry entry=e.getValue();
            long now=System.currentTimeMillis();
            if((now-entry.createdTime)<=m || (now-entry.lastTimeAccess)<=n){
                map.put(e.getKey(),entry.value);
            }
        }
        return map;
    }

    public int getHitRate(){
        if(totalAccess==0) return 0;
        return hitRate*100/totalAccess;
    }

    //getMap()-đã loại bỏ các phần tử chết
    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }


    @Override
    public V remove(Object key) {
        CacheEntry entry=cache.remove(key);
        return entry != null ? entry.value:null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<K> keySet() {
        return getMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return getMap().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() { //trả về Set chứa tất cả các cặp key-value của Map
        return getMap().entrySet();
    }
}
