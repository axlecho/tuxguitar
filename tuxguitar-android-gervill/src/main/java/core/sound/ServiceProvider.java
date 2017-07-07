package core.sound;

import com.sun.media.sound.SoftProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.android.sound.TGMixerProvider;
import org.herac.tuxguitar.resource.TGResourceManager;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.TGServiceReader;

public class ServiceProvider {

    private static TGContext context;

    public static void setContext(TGContext context) {
        ServiceProvider.context = context;
    }

    public static List<?> getProviders(Class<?> providerClass) {
        System.out.println("[getProviders] class " + providerClass.getCanonicalName());
        List<Object> providers = ProviderMap.getProviders(providerClass);
        if (providers.isEmpty()) {
            throw new RuntimeException("Invaid provider class: " + providerClass);
        }

        return providers;
    }

    public static List<?> getProviders_old(Class<?> providerClass) {
        List<Object> providers = new ArrayList<Object>();

        // Search available providers
        Iterator<?> it = TGServiceReader.lookupProviders(providerClass, TGResourceManager.getInstance(ServiceProvider.context));
        while (it.hasNext()) {
            providers.add(it.next());
        }
        if (providers.isEmpty()) {
            throw new RuntimeException("Invaid provider class: " + providerClass);
        }

        return providers;
    }
}
