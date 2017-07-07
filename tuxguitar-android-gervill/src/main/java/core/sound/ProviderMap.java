package core.sound;

import java.util.ArrayList;
import java.util.List;

public class ProviderMap {
    public static List<Object> getProviders(Class<?> providerClass) {
        List<Object> providers = new ArrayList<Object>();
        if (providerClass.getCanonicalName().equals("core.sound.midi.spi.MidiDeviceProvider")) {
            providers.add(new com.sun.media.sound.SoftProvider());
        } else if (providerClass.getCanonicalName().equals("core.sound.midi.spi.SoundbankReader")) {
            providers.add(new com.sun.media.sound.SF2SoundbankReader());
            providers.add(new com.sun.media.sound.DLSSoundbankReader());
            providers.add(new com.sun.media.sound.AudioFileSoundbankReader());
            providers.add(new com.sun.media.sound.JARSoundbankReader());
        } else if (providerClass.getCanonicalName().equals("core.sound.sampled.spi.AudioFileReader")) {
            providers.add(new com.sun.media.sound.WaveFloatFileReader());
            providers.add(new com.sun.media.sound.WaveExtensibleFileReader());
            // providers.add(new com.sun.media.sound.SoftMidiAudioFileReader());
        } else if (providerClass.getCanonicalName().equals("core.sound.sampled.spi.AudioFileWriter")) {
            providers.add(new com.sun.media.sound.WaveFloatFileWriter());
        } else if (providerClass.getCanonicalName().equals("core.sound.sampled.spi.FormatConversionProvider")) {
            providers.add(new com.sun.media.sound.AudioFloatFormatConverter());
        } else if (providerClass.getCanonicalName().equals("core.sound.sampled.spi.MixerProvider")) {
            providers.add(new org.herac.tuxguitar.android.sound.TGMixerProvider());
        }
        return providers;
    }
}
