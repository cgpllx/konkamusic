package com.konka.music.listener;

public interface AOnlyHaveMethod {
 	
 	void registerListener(MusicPlayerAction action);
 	
 	void unRegisterListener(MusicPlayerAction action);
 	
}
