package org.herac.tuxguitar.io.tg;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGInputStreamBase;
import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChannelParameter;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGColor;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGStroke;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.TGEffectBend;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloBar;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;

public class TGInputStream extends TGStream implements TGInputStreamBase{
	
	private DataInputStream dataInputStream;
	private String version;
	private TGFactory factory;
	
	public TGInputStream() {
		super();
	}
	
	public void init(TGFactory factory,InputStream stream) {
		this.factory = factory;
		this.dataInputStream = new DataInputStream(stream);
		this.version = null;
	}
	
	public TGFileFormat getFileFormat(){
		return TG_FORMAT;
	}
	
	public boolean isSupportedVersion(String version){
		return (version.equals(TG_FORMAT_VERSION));
	}
	
	public boolean isSupportedVersion(){
		try{
			readVersion();
			return isSupportedVersion(this.version);
		}catch(Throwable throwable){
			return false;
		}
	}
	
	public TGSong readSong() throws TGFileFormatException{
		try {
			if(this.isSupportedVersion()){
				TGSong song = this.read();
				this.dataInputStream.close();
				return song;
			}
			throw new TGFileFormatException("Unsupported Version");
		} catch (Throwable throwable) {
			throw new TGFileFormatException(throwable);
		}
	}
	
	private void readVersion() throws IOException{
		if(this.version == null){
			this.version = readUnsignedByteString();
		}
	}
	
	private TGSong read() throws IOException{
		TGSong song = this.factory.newSong();
		
		//leo el nombre
		song.setName(readUnsignedByteString());
		
		//leo el artista
		song.setArtist(readUnsignedByteString());
		
		//leo el album
		song.setAlbum(readUnsignedByteString());
		
		//leo el autor
		song.setAuthor(readUnsignedByteString());
		
		//leo la fecha
		song.setDate(readUnsignedByteString());
		
		//leo el copyright
		song.setCopyright(readUnsignedByteString());
		
		//leo el creador
		song.setWriter(readUnsignedByteString());
		
		//leo el transcriptor
		song.setTranscriber(readUnsignedByteString());
		
		//leo los comentarios
		song.setComments(readIntegerString());
		
		//leo la cantidad de canales
		int channelCount = readByte();
		
		//leo las canales
		for(int i = 0;i < channelCount;i++){
			TGChannel channel = this.factory.newChannel();
			readChannel(channel);
			song.addChannel(channel);
		}
		
		//leo la cantidad de measure headers
		int headerCount = readShort();
		
		//leo las pistas
		TGMeasureHeader lastHeader = null;
		long headerStart = TGDuration.QUARTER_TIME;
		for(int i = 0;i < headerCount;i++){
			TGMeasureHeader header = readMeasureHeader(i + 1,headerStart,lastHeader);
			song.addMeasureHeader(header);
			headerStart += header.getLength();
			lastHeader = header;
		}
		
		//leo la cantidad de pistas
		int trackCount = readByte();
		
		//leo las pistas
		for(int i = 0;i < trackCount;i++){
			song.addTrack(readTrack(i + 1,song));
		}
		
		return song;
	}
	
	private TGTrack readTrack(int number,TGSong song) throws IOException{
		//header
		int header = readHeader();
		
		TGTrack track = this.factory.newTrack();
		
		track.setNumber(number);
		
		//leo el nombre
		track.setName(readUnsignedByteString());
		
		//leo el solo
		track.setSolo((header & TRACK_SOLO) != 0);
		
		//leo el mute
		track.setMute((header & TRACK_MUTE) != 0);
		
		//leo el id del canal
		track.setChannelId(readShort());
		
		//leo la cantidad de compases
		int measureCount = song.countMeasureHeaders();
		
		//leo los compases
		TGMeasure lastMeasure = null;
		for(int i = 0;i < measureCount;i++){
			TGMeasure measure = readMeasure(song.getMeasureHeader(i),lastMeasure);
			track.addMeasure(measure);
			lastMeasure = measure;
		}
		
		//leo la cantidad de cuerdas
		int stringCount = readByte();
		
		//leo las cuerdas
		for(int i = 0;i < stringCount;i++){
			track.getStrings().add(readInstrumentString(i + 1));
		}
		
		//leo el offset
		track.setOffset(TGTrack.MIN_OFFSET + readByte());
		
		//leo el color
		readRGBColor(track.getColor());
		
		//leo el lyrics
		if(((header & TRACK_LYRICS) != 0)){
			readLyrics(track.getLyrics());
		}
		
		return track;
	}
	
	private TGMeasureHeader readMeasureHeader(int number,long start,TGMeasureHeader lastMeasureHeader) throws IOException{
		int header = readHeader();
		
		TGMeasureHeader measureHeader = this.factory.newHeader();
		measureHeader.setNumber(number);
		measureHeader.setStart(start);
		
		//leo el time signature
		if(((header & MEASURE_HEADER_TIMESIGNATURE) != 0)){
			readTimeSignature(measureHeader.getTimeSignature());
		}else if(lastMeasureHeader != null){
			measureHeader.getTimeSignature().copyFrom(lastMeasureHeader.getTimeSignature());
		}
		
		//leo el tempo
		if(((header & MEASURE_HEADER_TEMPO) != 0)){
			readTempo(measureHeader.getTempo());
		}else if(lastMeasureHeader != null){
			measureHeader.getTempo().copyFrom(lastMeasureHeader.getTempo());
		}
		
		//leo el comienzo de la repeticion
		measureHeader.setRepeatOpen((header & MEASURE_HEADER_REPEAT_OPEN) != 0);
		
		//leo el numero de repeticiones
		if(((header & MEASURE_HEADER_REPEAT_CLOSE) != 0)){
			 measureHeader.setRepeatClose(readShort());
		}
		
		//leo los finales alternativos
		if(((header & MEASURE_HEADER_REPEAT_ALTERNATIVE) != 0)){
			 measureHeader.setRepeatAlternative(readByte());
		}
		
		//leo el marker
		if(((header & MEASURE_HEADER_MARKER) != 0)){
			measureHeader.setMarker(readMarker(number));
		}
		
		measureHeader.setTripletFeel((lastMeasureHeader != null)?lastMeasureHeader.getTripletFeel():TGMeasureHeader.TRIPLET_FEEL_NONE);
		if(((header & MEASURE_HEADER_TRIPLET_FEEL) != 0)){
			measureHeader.setTripletFeel(readByte());
		}
		
		return measureHeader;
	}
	
	private TGMeasure readMeasure(TGMeasureHeader measureHeader,TGMeasure lastMeasure) throws IOException{
		int header = readHeader();
		
		TGMeasure measure = this.factory.newMeasure(measureHeader);
		TGBeatData data = new TGBeatData(measure);
		
		//leo la los beats
		readBeats(measure, data);
		
		//leo la clave
		measure.setClef( (lastMeasure == null)?TGMeasure.CLEF_TREBLE:lastMeasure.getClef());
		if(((header & MEASURE_CLEF) != 0)){
			measure.setClef(readByte());
		}
		
		//leo el key signature
		measure.setKeySignature((lastMeasure == null)?0:lastMeasure.getKeySignature());
		if(((header & MEASURE_KEYSIGNATURE) != 0)){
			measure.setKeySignature(readByte());
		}
		
		return measure;
	}
	
	private void readChannel(TGChannel channel) throws IOException{
		//leo el id
		channel.setChannelId(readShort());
		
		//leo el banco de sonidos
		channel.setBank((short)(readByte() & 0xff));
		
		//leo el programa
		channel.setProgram((short)(readByte() & 0xff));
		
		//leo el volumen
		channel.setVolume((short)(readByte() & 0xff));
		
		//leo el balance
		channel.setBalance((short)(readByte() & 0xff));
		
		//leo el chorus
		channel.setChorus((short)(readByte() & 0xff));
		
		//leo el reverb
		channel.setReverb((short)(readByte() & 0xff));
		
		//leo el phaser
		channel.setPhaser((short)(readByte() & 0xff));
		
		//leo el tremolo
		channel.setTremolo((short)(readByte() & 0xff));

		//leo el nombre
		channel.setName(readUnsignedByteString());
		
		//leo los parametros
		readChannelParameters(channel);
	}
	
	private void readChannelParameters(TGChannel channel) throws IOException{
		//leo la cantidad de parametros
		int count = readShort();
		
		for(int i = 0 ; i < count ; i ++ ){
			readChannelParameter(channel);
		}
	}
	
	private void readChannelParameter(TGChannel channel) throws IOException{
		TGChannelParameter parameter = this.factory.newChannelParameter();
			
		//leo el key
		parameter.setKey(readUnsignedByteString());
		
		//leo el value
		parameter.setValue(readIntegerString());
		
		channel.addParameter(parameter);
	}
	
	private void readBeats(TGMeasure measure,TGBeatData data) throws IOException{
		int header = BEAT_HAS_NEXT;
		while(((header & BEAT_HAS_NEXT) != 0)){
			header = readHeader();
			readBeat(header, measure, data);
		}
	}
	
	private void readBeat(int header, TGMeasure measure,TGBeatData data) throws IOException{
		TGBeat beat = this.factory.newBeat();
		
		beat.setStart(data.getCurrentStart());
		
		readVoices(header, beat, data);
		
		//leo el stroke
		if(((header & BEAT_HAS_STROKE) != 0)){
			readStroke(beat.getStroke());
		}
		
		//leo el acorde
		if(((header & BEAT_HAS_CHORD) != 0)){
			readChord(beat);
		}
		
		//leo el texto
		if(((header & BEAT_HAS_TEXT) != 0)){
			readText(beat);
		}
		
		measure.addBeat(beat);
	}
	
	private void readVoices(int header, TGBeat beat, TGBeatData data) throws IOException{
		for(int i = 0 ; i < TGBeat.MAX_VOICES; i ++ ){
			int shift = (i * 2 );
			
			beat.getVoice(i).setEmpty(true);
			
			if(((header & (BEAT_HAS_VOICE << shift)) != 0)){
				if(((header & (BEAT_HAS_VOICE_CHANGES << shift)) != 0)){
					data.getVoice(i).setFlags( readHeader() );
				}
				
				int flags = data.getVoice(i).getFlags();
				
				//leo la duracion
				if(((flags & VOICE_NEXT_DURATION) != 0)){
					readDuration(data.getVoice(i).getDuration());
				}
				
				//leo las notas
				if(((flags & VOICE_HAS_NOTES) != 0)){
					readNotes(beat.getVoice(i), data);
				}
				
				//leo la direccion
				if(((flags & VOICE_DIRECTION_UP) != 0)){
					beat.getVoice(i).setDirection( TGVoice.DIRECTION_UP );
				}
				else if(((flags & VOICE_DIRECTION_DOWN) != 0)){
					beat.getVoice(i).setDirection( TGVoice.DIRECTION_DOWN );
				}
				beat.getVoice(i).getDuration().copyFrom(data.getVoice(i).getDuration());
				data.getVoice(i).setStart(data.getVoice(i).getStart() + beat.getVoice(i).getDuration().getTime());
				
				beat.getVoice(i).setEmpty(false);
			}
		}
	}
	
	private void readNotes(TGVoice voice,TGBeatData data) throws IOException{
		int header = NOTE_HAS_NEXT;
		while(((header & NOTE_HAS_NEXT) != 0)){
			header = readHeader();
			readNote(header, voice, data);
		}
	}
	
	private void readNote(int header,TGVoice voice,TGBeatData data) throws IOException{
		TGNote note = this.factory.newNote();
		
		//leo el valor
		note.setValue(readByte());
		
		//leo la cuerda
		note.setString(readByte());
		
		//leo la ligadura
		note.setTiedNote((header & NOTE_TIED) != 0);
		
		//leo el velocity
		if(((header & NOTE_VELOCITY) != 0)){
			data.getVoice(voice.getIndex()).setVelocity(readByte());
		}
		note.setVelocity(data.getVoice(voice.getIndex()).getVelocity());
		
		//leo los efectos
		if(((header & NOTE_EFFECT) != 0)){
			readNoteEffect(note.getEffect());
		}
		
		voice.addNote(note);
	}
	
	private void readChord(TGBeat beat) throws IOException{
		TGChord chord = this.factory.newChord(readByte());
		
		//leo el nombre
		chord.setName( readUnsignedByteString() );
		
		//leo el primer fret
		chord.setFirstFret(readByte());
		
		//leo las cuerdas
		for(int string = 0; string < chord.countStrings(); string ++){
			chord.addFretValue(string, readByte());
		}
		beat.setChord(chord);
	}
	
	private void readText(TGBeat beat) throws IOException{
		TGText text = this.factory.newText();
		
		//leo el texto
		text.setValue(readUnsignedByteString());
		
		beat.setText(text);
	}
	
	private TGString readInstrumentString(int number) throws IOException{
		TGString string = this.factory.newString();
		
		string.setNumber(number);
		
		//leo el valor
		string.setValue(readByte());
		
		return string;
	}
	
	private void readTempo(TGTempo tempo) throws IOException{
		//leo el valor
		tempo.setValue(readShort());
	}
	
	private void readTimeSignature(TGTimeSignature timeSignature) throws IOException{
		//leo el numerador
		timeSignature.setNumerator(readByte());
		
		//leo el denominador
		readDuration(timeSignature.getDenominator());
	}
	
	private void readDuration(TGDuration duration) throws IOException{
		int header = readHeader();
		
		// leo el puntillo
		duration.setDotted((header & DURATION_DOTTED) != 0);
		
		//leo el doble puntillo
		duration.setDoubleDotted((header & DURATION_DOUBLE_DOTTED) != 0);
		
		//leo el valor
		duration.setValue(readByte());
		
		//leo el tipo de divisiones
		if(((header & DURATION_NO_TUPLET) != 0)){
			readDivisionType(duration.getDivision());
		}
		else{
			duration.getDivision().copyFrom(TGDivisionType.NORMAL);
		}
	}
	
	private void readDivisionType(TGDivisionType divisionType) throws IOException{
		//leo los enters
		divisionType.setEnters(readByte());
		
		//leo los tiempos
		divisionType.setTimes(readByte());
	}
	
	private void readStroke(TGStroke stroke) throws IOException{
		//leo la direccion
		stroke.setDirection( readByte() );
		
		//leo el valor
		stroke.setValue( readByte() );
	}
	
	private void readNoteEffect(TGNoteEffect effect) throws IOException{
		int header = readHeader(3);
		
		//leo el bend
		if(((header & EFFECT_BEND) != 0)){
			effect.setBend(readBendEffect());
		}
		
		//leo el tremolo bar
		if(((header & EFFECT_TREMOLO_BAR) != 0)){
			effect.setTremoloBar(readTremoloBarEffect());
		}
		
		//leo el harmonic
		if(((header & EFFECT_HARMONIC) != 0)){
			effect.setHarmonic(readHarmonicEffect());
		}
		
		//leo el grace
		if(((header & EFFECT_GRACE) != 0)){
			effect.setGrace(readGraceEffect());
		}
		
		//leo el trill
		if(((header & EFFECT_TRILL) != 0)){
			effect.setTrill(readTrillEffect());
		}
		
		//leo el tremolo picking
		if(((header & EFFECT_TREMOLO_PICKING) != 0)){
			effect.setTremoloPicking(readTremoloPickingEffect());
		}
		
		//vibrato
		effect.setVibrato(((header & EFFECT_VIBRATO) != 0));
		
		//dead note
		effect.setDeadNote(((header & EFFECT_DEAD) != 0));
		
		//slide
		effect.setSlide(((header & EFFECT_SLIDE) != 0));
		
		//hammer-on/pull-off
		effect.setHammer(((header & EFFECT_HAMMER) != 0));
		
		//ghost note
		effect.setGhostNote(((header & EFFECT_GHOST) != 0));
		
		//accentuated note
		effect.setAccentuatedNote(((header & EFFECT_ACCENTUATED) != 0));
		
		//heavy accentuated note
		effect.setHeavyAccentuatedNote(((header & EFFECT_HEAVY_ACCENTUATED) != 0));
		
		//palm mute
		effect.setPalmMute(((header & EFFECT_PALM_MUTE) != 0));
		
		//staccato
		effect.setStaccato(((header & EFFECT_STACCATO) != 0));
		
		//tapping
		effect.setTapping(((header & EFFECT_TAPPING) != 0));
		
		//slapping
		effect.setSlapping(((header & EFFECT_SLAPPING) != 0));
		
		//popping
		effect.setPopping(((header & EFFECT_POPPING) != 0));
		
		//fade in
		effect.setFadeIn(((header & EFFECT_FADE_IN) != 0));
		
		//let ring
		effect.setLetRing(((header & EFFECT_LET_RING) != 0));
	}
	
	private TGEffectBend readBendEffect() throws IOException{
		TGEffectBend bend = this.factory.newEffectBend();
		
		//leo la cantidad de puntos
		int count = readByte();
		
		for(int i = 0;i < count;i++){
			//leo la posicion
			int position = readByte();
			
			//leo el valor
			int value = readByte();
			
			//agrego el punto
			bend.addPoint(position,value);
		}
		return bend;
	}
	
	private TGEffectTremoloBar readTremoloBarEffect() throws IOException{
		TGEffectTremoloBar tremoloBar = this.factory.newEffectTremoloBar();
		
		//leo la cantidad de puntos
		int count = readByte();
		
		for(int i = 0;i < count;i++){
			//leo la posicion
			int position = readByte();
			
			//leo el valor
			int value =  (readByte() - TGEffectTremoloBar.MAX_VALUE_LENGTH);
			
			//agrego el punto
			tremoloBar.addPoint(position,value);
		}
		return tremoloBar;
	}
	
	private TGEffectHarmonic readHarmonicEffect() throws IOException{
		TGEffectHarmonic effect = this.factory.newEffectHarmonic();
		
		//leo el tipo
		effect.setType(readByte());
		
		//leo la data
		if(effect.getType() != TGEffectHarmonic.TYPE_NATURAL){
			effect.setData(readByte());
		}
		return effect;
	}
	
	private TGEffectGrace readGraceEffect() throws IOException{
		int header = readHeader();
		
		TGEffectGrace effect = this.factory.newEffectGrace();
		
		effect.setDead((header & GRACE_FLAG_DEAD) != 0) ;
		
		effect.setOnBeat((header & GRACE_FLAG_ON_BEAT) != 0) ;
		
		//leo el fret
		effect.setFret(readByte());
		
		//leo la duracion
		effect.setDuration(readByte());
		
		//leo el velocity
		effect.setDynamic(readByte());
		
		//leo la transicion
		effect.setTransition(readByte());
		
		return effect;
	}
	
	private TGEffectTremoloPicking readTremoloPickingEffect() throws IOException{
		TGEffectTremoloPicking effect = this.factory.newEffectTremoloPicking();
		
		//leo la duracion
		effect.getDuration().setValue(readByte());
		
		return effect;
	}
	
	private TGEffectTrill readTrillEffect() throws IOException{
		TGEffectTrill effect = this.factory.newEffectTrill();
		
		//leo el fret
		effect.setFret(readByte());
		
		//leo la duracion
		effect.getDuration().setValue(readByte());
		
		return effect;
	}
	
	private TGMarker readMarker(int measure) throws IOException{
		TGMarker marker = this.factory.newMarker();
		
		marker.setMeasure(measure);
		
		//leo el titulo
		marker.setTitle(readUnsignedByteString());
		
		//leo el color
		readRGBColor(marker.getColor());
		
		return marker;
	}
	
	private void readRGBColor(TGColor color) throws IOException{
		//leo el RGB
		color.setR((readByte() & 0xff));
		color.setG((readByte() & 0xff));
		color.setB((readByte() & 0xff));
	}
	
	private void readLyrics(TGLyric lyrics) throws IOException{
		//leo el compas de comienzo
		lyrics.setFrom(readShort());
		
		//leo el texto
		lyrics.setLyrics(readIntegerString());
	}
	
	private byte readByte() throws IOException{
		return (byte)this.dataInputStream.read();
	}
	
	private int readHeader() throws IOException{
		return this.dataInputStream.read();
	}
	
	private int readHeader(int bCount) throws IOException{
		int header = 0;
		for(int i = bCount; i > 0; i --){
			header += ( readHeader() << ( (8 * i) - 8 ) );
		}
		return header;
	}
	
	private short readShort() throws IOException{
		return this.dataInputStream.readShort();
	}
	
	private String readUnsignedByteString() throws IOException{
		return readString( (this.dataInputStream.read() & 0xFF ));
	}
	
	private String readIntegerString() throws IOException{
		return readString(this.dataInputStream.readInt());
	}
	
	private String readString(int length) throws IOException{
		char[] chars = new char[length];
		for(int i = 0;i < chars.length; i++){
			chars[i] = this.dataInputStream.readChar();
		}
		return String.copyValueOf(chars);
	}
}
