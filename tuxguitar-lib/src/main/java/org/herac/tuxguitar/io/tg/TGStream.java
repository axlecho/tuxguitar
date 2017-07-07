package org.herac.tuxguitar.io.tg;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGVelocities;
import org.herac.tuxguitar.util.TGVersion;

public class TGStream {
	
	public static final String TG_FORMAT_NAME = ("TuxGuitar File Format");
	
	public static final String TG_FORMAT_VERSION = (TG_FORMAT_NAME + " - " + new TGVersion(1,3,0).getVersion() );
	
	public static final String TG_FORMAT_CODE = ("tg");
	
	public static final TGFileFormat TG_FORMAT = new TGFileFormat("TuxGuitar", new String[]{ TG_FORMAT_CODE });
	
	protected static final int TRACK_SOLO = 0x01;
	
	protected static final int TRACK_MUTE = 0x02;
	
	protected static final int TRACK_LYRICS = 0x04;
	
	protected static final int MEASURE_HEADER_TIMESIGNATURE = 0x01;
	
	protected static final int MEASURE_HEADER_TEMPO = 0x02;
	
	protected static final int MEASURE_HEADER_REPEAT_OPEN = 0x04;
	
	protected static final int MEASURE_HEADER_REPEAT_CLOSE = 0x08;
	
	protected static final int MEASURE_HEADER_REPEAT_ALTERNATIVE = 0x10;
	
	protected static final int MEASURE_HEADER_MARKER = 0x20;
	
	protected static final int MEASURE_HEADER_TRIPLET_FEEL = 0x40;
	
	protected static final int MEASURE_CLEF = 0x01;
	
	protected static final int MEASURE_KEYSIGNATURE = 0x02;
	
	protected static final int BEAT_HAS_NEXT = 0x01;
	
	protected static final int BEAT_HAS_STROKE = 0x02;
	
	protected static final int BEAT_HAS_CHORD = 0x04;
	
	protected static final int BEAT_HAS_TEXT = 0x08;
	
	protected static final int BEAT_HAS_VOICE = 0x10;
	
	protected static final int BEAT_HAS_VOICE_CHANGES = 0x20;
	
	protected static final int VOICE_HAS_NOTES = 0x01;
	
	protected static final int VOICE_NEXT_DURATION = 0x02;
	
	protected static final int VOICE_DIRECTION_UP = 0x04;
	
	protected static final int VOICE_DIRECTION_DOWN = 0x08;
	
	protected static final int NOTE_HAS_NEXT = 0x01;
	
	protected static final int NOTE_TIED = 0x02;
	
	protected static final int NOTE_EFFECT = 0x04;
	
	protected static final int NOTE_VELOCITY = 0x08;
	
	protected static final int DURATION_DOTTED = 0x01;
	
	protected static final int DURATION_DOUBLE_DOTTED = 0x02;
	
	protected static final int DURATION_NO_TUPLET = 0x04;
	
	protected static final int EFFECT_BEND = 0x000001;
	
	protected static final int EFFECT_TREMOLO_BAR = 0x000002;
	
	protected static final int EFFECT_HARMONIC = 0x000004;
	
	protected static final int EFFECT_GRACE = 0x000008;
	
	protected static final int EFFECT_TRILL = 0x000010;
	
	protected static final int EFFECT_TREMOLO_PICKING = 0x000020;
	
	protected static final int EFFECT_VIBRATO = 0x000040;
	
	protected static final int EFFECT_DEAD = 0x000080;
	
	protected static final int EFFECT_SLIDE = 0x000100;
	
	protected static final int EFFECT_HAMMER = 0x000200;
	
	protected static final int EFFECT_GHOST = 0x000400;
	
	protected static final int EFFECT_ACCENTUATED = 0x000800;
	
	protected static final int EFFECT_HEAVY_ACCENTUATED = 0x001000;
	
	protected static final int EFFECT_PALM_MUTE = 0x002000;
	
	protected static final int EFFECT_STACCATO = 0x004000;
	
	protected static final int EFFECT_TAPPING = 0x008000;
	
	protected static final int EFFECT_SLAPPING = 0x010000;
	
	protected static final int EFFECT_POPPING = 0x020000;
	
	protected static final int EFFECT_FADE_IN = 0x040000;
	
	protected static final int EFFECT_LET_RING = 0x080000;
	
	protected static final int GRACE_FLAG_DEAD = 0x01;
	
	protected static final int GRACE_FLAG_ON_BEAT = 0x02;
	
	protected class TGBeatData {
		private long currentStart;
		private TGVoiceData[] voices;
		
		protected TGBeatData(TGMeasure measure){
			this.init(measure);
		}
		
		private void init(TGMeasure measure){
			this.currentStart = measure.getStart();
			this.voices = new TGVoiceData[TGBeat.MAX_VOICES];
			for(int i = 0 ; i < this.voices.length ; i ++ ){
				this.voices[i] = new TGVoiceData(measure);
			}
		}
		
		protected TGVoiceData getVoice(int index){
			return this.voices[index];
		}
		
		public long getCurrentStart(){
			long minimumStart = -1;
			for(int i = 0 ; i < this.voices.length ; i ++ ){
				if( this.voices[i].getStart() > this.currentStart ){
					if( minimumStart < 0 || this.voices[i].getStart() < minimumStart ){
						minimumStart = this.voices[i].getStart();
					}
				}
			}
			if( minimumStart > this.currentStart ){
				this.currentStart = minimumStart;
			}
			return this.currentStart;
		}
	}
	
	protected class TGVoiceData {
		private long start;
		private int velocity;
		private int flags;
		private TGDuration duration;
		
		protected TGVoiceData(TGMeasure measure){
			this.init(measure);
		}
		
		private void init(TGMeasure measure){
			this.flags = 0;
			this.setStart(measure.getStart());
			this.setVelocity(TGVelocities.DEFAULT);
			this.setDuration(new TGFactory().newDuration());
		}
		
		public TGDuration getDuration() {
			return this.duration;
		}
		
		public void setDuration(TGDuration duration) {
			this.duration = duration;
		}
		
		public long getStart() {
			return this.start;
		}
		
		public void setStart(long start) {
			this.start = start;
		}
		
		public int getVelocity() {
			return this.velocity;
		}
		
		public void setVelocity(int velocity) {
			this.velocity = velocity;
		}
		
		public int getFlags() {
			return this.flags;
		}
		
		public void setFlags(int flags) {
			this.flags = flags;
		}
	}
}
