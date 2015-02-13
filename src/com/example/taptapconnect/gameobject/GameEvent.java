package com.example.taptapconnect.gameobject;

public class GameEvent {
	public final static int GAME_OBJECT_TOUCH = 1;
	public final static int GAME_OBJECT_MOVE = 2;
	public final static int GAME_OBJECT_DELETE = 3;
	public final static int GAME_OBJECT_CHANGE = 4;
	public final static int GAME_OBJECT_EMPTY = 5;
	private int objectEvent;
	private GameObject gameobject;
	private GameObjectHandler gameobjecthandler;

	GameEvent(int event) {
		this(null, null, event);
	}

	GameEvent(GameObject eventObject, int event) {
		this(null, eventObject, event);
	}

	GameEvent(GameObjectHandler objectHandler, int event) {
		this(objectHandler, null, event);
	}

	GameEvent(GameObjectHandler objectHandler, GameObject eventObject, int event) {
		this.setEvent(event);
		this.setGameobject(eventObject);
		this.setGameObjectHandler(objectHandler);
	}

	/**
	 * @return the Event
	 */
	public int getEvent() {
		return objectEvent;
	}

	/**
	 * @param objectEvent
	 *            the Event to set
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
	 * @param gameobject
	 *            the gameobject to set
	 */
	public void setGameobject(GameObject gameobject) {
		this.gameobject = gameobject;
	}

	/**
	 * @return the gameobjecthandler
	 */
	public GameObjectHandler getGameObjectHandler() {
		return gameobjecthandler;
	}

	/**
	 * @param gameobjecthandler
	 *            the gameobjecthandler to set
	 */
	public void setGameObjectHandler(GameObjectHandler gameobjecthandler) {
		this.gameobjecthandler = gameobjecthandler;
	}

}
