package org.herac.tuxguitar.android.toolbar;

public class TGToolbarPlayRateListItem {
    public TGToolbarPlayRateListItem(int rate) {
        this.rate = rate;
    }

    public int rate;

    public String toString() {
        return rate + " %";
    }
}
