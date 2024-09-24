package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entities.Player;

public class AudioPlayer 
{
	private static Random random = new Random();
	
	//Fade in Fade out tempo
	private float volumeVar = 0.00075f;
	
	private float songVolume = 0.5f;
	private float effectsVolume = 0.9f;
	private float ambienceSoundsVolume = 0.9f;
	
	private float generalSongsVolume=0f;
	private float generalAmbienceSoundsVolume=1;
	
	private Clip[] songs;
	private static Clip[] effects;
	private static Clip[] ambienceSounds;
	private int currentSongId;
	
	private static int saveRandomize;

	
	
	//Esta ordem tem de ser da mesma ordem do LoadAmbienceSounds
	public static int FOREST = 0;
	public static int TOP_MONTAIN = 1;
	public static int CAVE = 2;
	public static int CITY = 3;

	
	
	//Esta ordem tem de ser da mesma ordem do LoadsSongs
	public static int MENU = 0;
	public static int CUTSCENE = 1;
	public static int LEVEL_ONE = 2;
	public static int LEVEL_TWO = 3;
	public static int LEVEL_END = 4;
	
	//Esta ordem tem de ser da mesma ordem do LoadEffects
	//public static int CHECKPOINT = 0;
	
	public static int START_NEW_GAME = 16;
	public static int CHECKPOINT = 17;
	public static int DASH1 = 18;
	public static int DASH2 = 19;
	public static int BINOCULAROUT = 27;
	
	public static int BINOCULARIN1 = 28;
	public static int BINOCULARIN2 = 29;
	public static int BINOCULARIN3 = 30;
	public static int BINOCULARIN4 = 31;
	public static int BINOCULARIN5 = 32;
	public static int BINOCULARIN6 = 33;
	
	public static int INVISIBLEWALL = 34;
	
	private void loadSongs()
	{
		//Carregar as musicas, menu, fase, final
		//Obs: aqui é o nome do arquivo .wav
		String[] names = {"LooperMan", "SubwooferLullaby", "Loop1", "FirstSteps", "House1"};
		songs = new Clip[names.length];
		
		for (int i=0; i<songs.length; i++)
		{
			songs[i] = getClip(names[i]);
		}
		
		updateSongsVolume();
	}
	
	private void loadEffects()
	{
		//Carregar efeitos sonoros, pulo, dash, pegar o lixo, tem que ser a mesma ordem das constantes la em cima
		//Obs: aqui é o nome do arquivo .wav
		String[] names = {
		//     1              2                3                4                5                6               7                  8
		"Steps/Grass1", "Steps/Grass2",  "Steps/Grass3",  "Steps/Grass4",  "Steps/Grass5",  "Steps/Grass6",  "Steps/Grass7",  "Steps/Grass8", 
		"Steps/Grass9", "Steps/Grass10", "Steps/Grass11", "Steps/Grass12", "Steps/Grass13", "Steps/Grass14", "Steps/Grass15", "Steps/Grass16",
		"StartNewGame", "CheckPoint",    "Dash", "Dash" , "Springs/Spring1", "Springs/Spring2", "Springs/Spring3", "Springs/Spring4", "Springs/Spring5", 
		"Springs/Spring6", "Springs/Spring7", "SpyGlassesOut", "SpyGlassesIn", "SpyGlassesIn", "SpyGlassesIn", "SpyGlassesIn", "SpyGlassesIn", "SpyGlassesIn",
		"InvisibleWall"
		
		
		};
		
		effects = new Clip[names.length];
		
		for (int i=0; i<effects.length; i++)
		{
			effects[i] = getClip(names[i]);
		}
		
		updateEffectsVolume();
	}
	
	private void loadAmbienceSounds()
	{
		//Carregar as musicas, menu, fase, final
		//Obs: aqui é o nome do arquivo .wav
		String[] names = {"0", "0", "0"};
		ambienceSounds = new Clip[names.length];
		
		for (int i=0; i<ambienceSounds.length; i++)
		{
			ambienceSounds[i] = getClip(names[i]);
		}
		
		updateSongsVolume();
	}
	
	public AudioPlayer()
	{
		loadSongs();
		loadEffects();
		//loadAmbienceSounds();
		playSong(MENU);
	}
	
	public void setSongVolume(float songVolume)
	{
		this.songVolume = songVolume;
		updateSongsVolume();
	}
	
	public void subSongVolume()
	{
		if (songVolume > 0.05)
		{
			this.songVolume -= volumeVar;
			updateSongsVolume();
		}
	}
	
	public void subSongVolume(float volume)
	{
		if (songVolume > volume)
		{
			this.songVolume -= volumeVar;
			updateSongsVolume();
		}
	}
	
	
	public void setEffectsVolume(float effectsVolume)
	{
		this.effectsVolume = effectsVolume;
		updateEffectsVolume();
	}
	
	public void stopSong()
	{
		//Depois tentar fazer um fade out
		if (songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}

	
	public void addSongVolume()
	{
		if (songVolume <0.95 * generalSongsVolume)
		{
			this.songVolume += volumeVar;
			updateSongsVolume();
		}
	}
	

	
	//Trocar as musicas entre o menu, o level e o final
	public void setLevelSong(int newSong, float volume)
	{
		if (getCurrentSongId() == newSong)
		{
			if (songVolume < volume * generalSongsVolume)
				addSongVolume();
		}
		else
		{
			if (getSongVolume() > 0.45)
			{
				subSongVolume();
			}
			else
			{
				setSongVolume(0);
				playSong(newSong);
				setSongVolume(0.45f);
			}
		}
	}
	
	//Trocar as musicas entre o menu, o level e o final
	public void setLevelSong(int newSong)
	{
		if (getCurrentSongId() == newSong)
		{
			addSongVolume();
		}
		else
		{
			if (getSongVolume() > 0.45)
			{
				subSongVolume();
			}
			else
			{
				setSongVolume(0);
				playSong(newSong);
				setSongVolume(0.45f);
			}
		}
	}
	
	public void playSong(int song)
	{
		stopSong();
		
		//Colocar outra/ tentar fazer fade in
		currentSongId = song;
		songs[currentSongId].setMicrosecondPosition(0);
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
	}
	

	
	
	
	public static void playEffect(int effect)
	{
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}
	
	private Clip getClip(String name)
	{
		URL url = getClass().getResource("/" + name + ".wav");
		AudioInputStream audio;
		
		try 
		{
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);

			return c;
		} 
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}

	public void updateSongsVolume()
	{
		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * songVolume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	private void updateEffectsVolume()
	{
		for(Clip c : effects)
		{
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * effectsVolume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
	
	
	public float getSongVolume()
	{
		return songVolume;
	}
	
	public float getEffectsVolume()
	{
		return effectsVolume;
	}
	
	public int getCurrentSongId()
	{
		return currentSongId;
	}
	
}
