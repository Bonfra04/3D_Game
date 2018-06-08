package gameEngine.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioMaster {

	private static List<Integer> buffers = new ArrayList<Integer>();
	private static List<Source> sources = new ArrayList<Source>();

	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			System.err.println("Error while preparing sounds");
			AudioMaster.init();
		}

	}

	public static void setAttenuationType(int type) {
		AL10.alDistanceModel(type);
	}

	public static void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create("pokémonBoundary/audio/" + file + ".wav");
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public static Source generateSource() {
		Source source = new Source();
		sources.add(source);
		return source;
	}

	public static void cleanUp() {
		for (Source source : sources)
			source.delete();
		for (int buffer : buffers)
			AL10.alDeleteBuffers(buffer);
		AL.destroy();
	}

}
