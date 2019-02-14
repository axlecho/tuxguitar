package org.herac.tuxguitar.graphics.control;

import android.util.LruCache;

import org.herac.tuxguitar.graphics.TGResource;

import java.util.ArrayList;
import java.util.List;

public class TGResourceBuffer {

    private List<Object> registry;

    private LruCache<Object, TGResource> buffer;
    private int cacheSize;

    public TGResourceBuffer() {
        this.cacheSize = (int) Runtime.getRuntime().maxMemory() / 4 * 3;
        this.buffer = new LruCache<Object, TGResource>(cacheSize) {
            protected int sizeOf(Object key, TGResource value) {
                return value.getSize();
            }
        };

        this.registry = new ArrayList<Object>();
    }

    @SuppressWarnings("unchecked")
    public <T extends TGResource> T getResource(Object key) {
        if (this.buffer.snapshot().containsKey(key)) {
            return (T) this.buffer.get(key);
        }
        return null;
    }

    public void setResource(Object key, TGResource resource) {
        if (this.buffer.snapshot().containsKey(key)) {
            this.disposeResource(key);
        }

        this.buffer.put(key, resource);
    }

    public void disposeResource(Object key) {
        TGResource resource = this.getResource(key);
        if (resource != null && !resource.isDisposed()) {
            resource.dispose();
        }
        this.buffer.remove(key);
    }

    public void disposeAllResources() {
        List<Object> keys = new ArrayList<Object>(this.buffer.snapshot().keySet());
        for (Object key : keys) {
            this.disposeResource(key);
        }
    }

    public void disposeUnregisteredResources() {
        List<Object> keys = new ArrayList<Object>(this.buffer.snapshot().keySet());
        for (Object key : keys) {
            if (!this.isRegistered(key)) {
                this.disposeResource(key);
            }
        }
    }

    public void clearRegistry() {
        this.registry.clear();
    }

    public void register(Object key) {
        if (!this.isRegistered(key)) {
            this.registry.add(key);
        }
    }

    public void unregister(Object key) {
        if (this.isRegistered(key)) {
            this.registry.remove(key);
        }
    }

    public boolean isRegistered(Object key) {
        return this.registry.contains(key);
    }

    public boolean isResourceDisposed(Object key) {
        TGResource resource = this.getResource(key);

        return (resource == null || resource.isDisposed());
    }
}
