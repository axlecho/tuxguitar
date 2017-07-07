package org.herac.tuxguitar.android;

import org.herac.tuxguitar.android.midi.port.gervill.MidiOutputPortProviderPlugin;
import org.herac.tuxguitar.io.gpx.GPXInputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP1InputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP2InputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP3InputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP3OutputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP4InputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP4OutputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP5InputStreamPlugin;
import org.herac.tuxguitar.io.gtp.GP5OutputStreamPlugin;
import org.herac.tuxguitar.io.ptb.PTInputStreamPlugin;
import org.herac.tuxguitar.util.plugin.TGPlugin;

import java.util.ArrayList;

public class TGPluginList extends ArrayList<TGPlugin> {
    public TGPluginList() {
        this.add(new MidiOutputPortProviderPlugin());
        this.add(new GP1InputStreamPlugin());
        this.add(new GP2InputStreamPlugin());
        this.add(new GP3InputStreamPlugin());
        this.add(new GP4InputStreamPlugin());
        this.add(new GP5InputStreamPlugin());
        this.add(new GP3OutputStreamPlugin());
        this.add(new GP4OutputStreamPlugin());
        this.add(new GP5OutputStreamPlugin());
        this.add(new GPXInputStreamPlugin());
        this.add(new PTInputStreamPlugin());
    }
}
