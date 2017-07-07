package org.herac.tuxguitar.android.view.tablature;

import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.graphics.TGFontImpl;
import org.herac.tuxguitar.graphics.TGColorModel;
import org.herac.tuxguitar.graphics.TGFontModel;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;

import java.util.List;

public class TGSongViewTitlePainter {
    private static final float TITLE_HEIGHT = 240.0f;
    private static final String[] KEY_NAMES = new String[]{
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    private static final int[] STANDARD_KEY = new int[]{
            64, 59, 55, 50, 45, 40
    };
    private static final int MAX_NOTES = 12;
    private TGSongViewController controller;

    public TGSongViewTitlePainter(TGSongViewController controller) {
        this.controller = controller;
    }

    public void paint(TGPainter target, TGRectangle clientArea, float fromX, float fromY) {
        if (fromY > TITLE_HEIGHT) {
            return;
        }
        TGFontModel tgFontModel = new TGFontModel();
        tgFontModel.setHeight(36);
        tgFontModel.setBold(true);
        tgFontModel.setItalic(false);
        target.setFont(new TGFontImpl(tgFontModel));
        target.setAlpha(180);
        target.setForeground(target.createColor(new TGColorModel(0x33, 0x33, 0x33)));
        this.paintTitle(target, clientArea, clientArea.getWidth() / 2.0f, this.TITLE_HEIGHT / 4.0f - fromY);

        tgFontModel.setHeight(24);
        tgFontModel.setBold(false);
        tgFontModel.setItalic(true);
        target.setFont(new TGFontImpl(tgFontModel));
        this.paintTurning(target, clientArea.getWidth() / 28.0f, this.TITLE_HEIGHT / 4.0f * 2.8f - fromY);
    }

    public float getTitleHeight() {
        return TITLE_HEIGHT;
    }

    private void paintTitle(TGPainter target, TGRectangle clientArea, float x, float y) {
        TGCaret caret = TGSongViewController.getInstance(controller.getContext()).getCaret();
        CharSequence activityTitle = TGActivityController.getInstance(controller.getContext()).getActivity().getTitle();
        String title;

        if (activityTitle == null) {
            title = "（无标题）";
        } else {
            title = activityTitle.toString();
        }

        float maxwidth = clientArea.getWidth() / 4.0f * 2.5f;
        this.painTitle(target, maxwidth, title, x, y);

    }

    private void painTitle(TGPainter target, float maxWidth, String title, float x, float y) {
        if (target.getFMWidth(title) > maxWidth) {
            for (int i = 0; i < title.length(); ++i) {
                String thisLine = title.substring(0, i);
                thisLine += "...";
                if (target.getFMWidth(thisLine) > maxWidth) {
                    target.drawStringCenter(thisLine, x, y);

                    // muti line
                    // String nextLine = title.substring(i);
                    // if(!nextLine.equals("")) {
                    //    y += target.getFMHeight() + 8;
                    //    this.painTitle(target,maxWidth,nextLine,x,y);
                    // }
                    return;
                }
            }
        } else {
            target.drawStringCenter(title, x, y);
        }
    }


    private void paintTurning(TGPainter target, float x, float y) {
        TGCaret caret = TGSongViewController.getInstance(controller.getContext()).getCaret();
        TGTrack track = caret.getTrack();
        List<TGString> strings = track.getStrings();

        String key = "";
        if (isStandKey(strings)) {
            key = "标准调弦";
        } else {
            key = praseStrings(strings);
        }

        if (track.getOffset() != 0) {
            key = "capo=" + track.getOffset() + "\n" + key;
        }

        for (String line : key.split("\n")) {
            target.drawString(line, x, y);
            y += target.getFMHeight() + 8;
        }
    }

    private boolean isStandKey(List<TGString> strings) {
        int pos = 1;
        int diff = Integer.MAX_VALUE;

        for (TGString string : strings) {
            int i = string.getValue();
            if (pos <= STANDARD_KEY.length) {
                if (diff == Integer.MAX_VALUE) {
                    diff = i - STANDARD_KEY[pos - 1];
                } else {
                    if (diff != i - STANDARD_KEY[pos - 1]) {
                        return false;
                    }
                }
            }
            pos++;
        }

        return diff >= 0;
    }

    private String praseStrings(List<TGString> strings) {
        int total = strings.size();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < total / 2; i++) {
            // String noteName = (KEY_NAMES[(i - ((i / MAX_NOTES) * MAX_NOTES))] + (i / MAX_NOTES));
            sb.append("(");
            sb.append(i + 1);
            sb.append(")");
            sb.append("=");
            sb.append(getNoteNameByPos(strings.get(i).getValue()));
            sb.append("   ");

            sb.append("(");
            sb.append(i + total / 2 + 1);
            sb.append(")");
            sb.append("=");
            sb.append(getNoteNameByPos(strings.get(i + total / 2).getValue()));
            sb.append("\n");
        }

        if (total % 2 != 0) { //what the fu*k...
            sb.append("(");
            sb.append(total);
            sb.append(")");
            sb.append(getNoteNameByPos(strings.get(total - 1).getValue()));
        }
        return sb.toString();
    }

    private String getNoteNameByPos(int i) {
        return KEY_NAMES[(i - ((i / MAX_NOTES) * MAX_NOTES))];
    }


}
