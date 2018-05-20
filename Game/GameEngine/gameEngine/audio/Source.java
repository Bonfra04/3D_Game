package gameEngine.audio;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceId;

	public Source() {
		sourceId = AL10.alGenSources();
		this.setRollOfFactor(1);
		this.setReferenceDistance(2);
		this.setMaxDistance(5);
	}

	public void setRollOfFactor(float rollOfFactor) {
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, rollOfFactor);
	}

	public void setReferenceDistance(float referenceDistance) {
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
	}

	public void setMaxDistance(float maxDistance) {
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, maxDistance);
	}

	public void play(int buffer, boolean override) {
		if (override) {
			this.stop();
			AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
			this.continuePlayng();
		} else if (!this.isPlaying()) {
			AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
			AL10.alSourcePlay(sourceId);
		}
	}

	public void delete() {
		this.stop();
		AL10.alDeleteSources(sourceId);
	}

	public void pause() {
		AL10.alSourcePause(sourceId);
	}

	public void continuePlayng() {
		AL10.alSourcePlay(sourceId);
	}

	public void stop() {
		AL10.alSourceStop(sourceId);
	}

	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
	}

	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void setVolume(float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}

	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, x, y, z);
	}
}
