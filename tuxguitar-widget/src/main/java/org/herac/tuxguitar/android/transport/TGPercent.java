package org.herac.tuxguitar.android.transport;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TGPercent {


    final private Integer[] percents = new Integer[]{10, 20, 30, 50, 75, 90, 100, 150, 200, 300, 500};

    public static TGPercent getInstance(TGContext context) {
        return TGSingletonUtil.getInstance(context, TGPercent.class.getName(), new TGSingletonFactory<TGPercent>() {
            public TGPercent createInstance(TGContext context) {
                return new TGPercent();
            }
        });
    }

    public List<Integer> getPercentList() {
        return Arrays.asList(percents);
    }
}
