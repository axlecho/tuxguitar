package org.herac.tuxguitar.android.view.tablature;

import android.util.Log;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.graphics.TGFontImpl;
import org.herac.tuxguitar.graphics.TGFontModel;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTrack;

import java.util.List;

public class TGSongViewTitlePainter {
    private static float TITLE_HEIGHT = 0f;
    private float keyline = 16f;
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
        this.keyline = controller.findContext().getResources().getDimension(R.dimen.key_line);
        if (fromY > TITLE_HEIGHT) {
            return;
        }

        this.paintTitle(target, clientArea, clientArea.getWidth() / 2.0f, this.keyline * 2 - fromY);
        this.paintTurning(target, this.keyline * 2, this.keyline + target.getFMHeight() + this.keyline * 2 - fromY);
    }

    public float getTitleHeight() {
        return TITLE_HEIGHT;
    }


    private void paintTitle(TGPainter target, TGRectangle clientArea, float x, float y) {
        float maxWidth = clientArea.getWidth() / 4.0f * 2.5f;
        String title = this.getTitle();
        TGFontModel tgFontModel = new TGFontModel();
        tgFontModel.setHeight(48);
        tgFontModel.setBold(true);
        tgFontModel.setItalic(false);
        target.setFont(new TGFontImpl(tgFontModel));

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
        TGFontModel tgFontModel = new TGFontModel();
        tgFontModel.setHeight(24);
        tgFontModel.setBold(false);
        tgFontModel.setItalic(false);
        target.setFont(new TGFontImpl(tgFontModel));

        for (String line : getKey().split("\n")) {
            target.drawString(line, x, y);
            if (TITLE_HEIGHT < y + keyline) {
                TITLE_HEIGHT = y + keyline;
            }
            y += target.getFMHeight() + keyline / 2;
        }
    }


    private String getTitle() {
        CharSequence activityTitle = TGActivityController.getInstance(controller.getContext()).getActivity().getTitle();
        return activityTitle == null ? "（无标题）" : activityTitle.toString();
    }

    private String getKey() {
        TGCaret caret = TGSongViewController.getInstance(controller.getContext()).getCaret();
        TGTrack track = caret.getTrack();
        List<TGString> strings = track.getStrings();

        String key;
        if (isStandKey(strings)) {
            key = "标准调弦";
        } else {
            key = praseStrings(strings);
        }

        if (track.getOffset() != 0) {
            key = "capo=" + track.getOffset() + "\n" + key;
        }

        return key;
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
