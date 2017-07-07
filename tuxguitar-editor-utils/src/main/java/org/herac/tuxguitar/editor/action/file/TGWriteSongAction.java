package org.herac.tuxguitar.editor.action.file;

import java.io.InputStream;
import java.io.OutputStream;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionException;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGSongWriterHandle;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.util.TGContext;

public class TGWriteSongAction extends TGActionBase{
	
	public static final String NAME = "action.song.write";
	
	public static final String ATTRIBUTE_FORMAT = TGFileFormat.class.getName();
	public static final String ATTRIBUTE_OUTPUT_STREAM = InputStream.class.getName();
	
	public TGWriteSongAction(TGContext context) {
		super(context, NAME);
	}

	protected void processAction(TGActionContext context){
		try{
			OutputStream stream = (OutputStream) context.getAttribute(ATTRIBUTE_OUTPUT_STREAM);
			TGFileFormat fileFormat = (TGFileFormat) context.getAttribute(ATTRIBUTE_FORMAT);
			TGSong song = (TGSong) context.getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);
			TGSongManager songManager = getSongManager(context);
			
			TGSongWriterHandle tgSongWriterHandle = new TGSongWriterHandle();
			tgSongWriterHandle.setFactory(songManager.getFactory());
			tgSongWriterHandle.setSong(song);
			tgSongWriterHandle.setFormat(fileFormat);
			tgSongWriterHandle.setOutputStream(stream);
			
			TGFileFormatManager.getInstance(getContext()).getWriter().write(tgSongWriterHandle);
		} catch(TGFileFormatException e){
			throw new TGActionException(e);
		}
	}
}
