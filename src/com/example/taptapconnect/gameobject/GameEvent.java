package com.example.taptapconnect.gameobject;

public class GameEvent {
	final static int GAME_OBJECT_TOUCH 	= 1;
	final static int GAME_OBJECT_MOVE 	= 2;
	final static int GAME_OBJECT_DELETE	= 3;
	final static int GAME_OBJECT_CHANGE = 4;
	final static int GAME_OBJECT_EMPTY 	= 5;
	private int objectEvent;
	private GameObject gameobject;
	
	GameEvent(int event) {
		this(null, event);
	}
	
	GameEvent(GameObject eventObject, int event) {
		this.setEvent(event);
		this.setGameobject(eventObject);
	}
	

	/**
	 * @return the Event
	 */
	public int getEvent() {
		return objectEvent;
	}

	/**
	 * @param objectEvent the Event to set
	 */
	public void setEvent(int event) {
		this.objectEvent = event;
	}


	/**
	 * @return the gameobject
	 */
	public GameObject getGameobject() {
		return gameobject;
	}


	/**
	 * @param gameobject the gameobject to set
	 */
	public void setGameobject(GameObject gameobject) {
		this.gameobject = gameobject;
	}

}
