package com.example.taptapconnect.gameobject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import android.util.Log;

/**
 * @since 1.0
 * @author ET
 *
 */
public class GameObjectHandler implements Iterable<GameObject> {

	private static int handlerCounter = 0;
	Deque<GameObject> gameObjects;
	private GameObjectListener listener;
	private int id;

	public GameObjectHandler() {
		gameObjects = new ArrayDeque<GameObject>();
		listener = new GameObjectListener() {

			@Override
			public void onChange(GameEvent event) {}

			@Override
			public void onEmpty(GameEvent event) {}

			@Override
			public boolean onMove(GameEvent event) {return false;}

			@Override
			public void onTouch(GameEvent event)  {}

			@Override
			public void onDelete(GameEvent event) {}
			
		};
		setId(handlerCounter);
		handlerCounter++;
		Log.i(this.getClass().getName(), "GameObjectHandler Create!");
	}

	/**
	 * 取得遊戲物件集合物件之總數
	 * 
	 * @return 遊戲物件集合 總數
	 */
	public static int getHandlerCounter() {
		return handlerCounter;
	}
	

	/**
	 * 設定遊戲物件聆聽器
	 * 
	 * @param l
	 *            遊戲物件聆聽器
	 */
	public void setListener(GameObjectListener l) {
		this.listener = l;
	}

	/**
	 * 取得內含之遊戲物件數量
	 * 
	 * @return 遊戲物件數量
	 */
	public int getCount() {
		return gameObjects.size();
	}

	/**
	 * 新增遊戲物件至陣列之尾，加入到GameObjectHandler裡。
	 * 
	 * @see java.util.ArrayDeque 以此陣列之形式呈現
	 * @param gameobject
	 *            要新增的遊戲物件
	 */
	public void add(GameObject gameobject) {
		addLast(gameobject);
	}

	/**
	 * 新增遊戲物件至陣列之尾，加入到GameObjectHandler裡。
	 * 
	 * @see java.util.ArrayDeque 以此陣列之形式呈現
	 * @param gameobject
	 *            要新增的遊戲物件
	 */
	public void addLast(GameObject gameobject) {
		gameObjects.addLast(gameobject);
		notifyListener(new GameEvent(this,gameobject, GameEvent.GAME_OBJECT_CHANGE));
	}

	/**
	 * 新增遊戲物件至陣列之頭，加入到GameObjectHandler裡。
	 * 
	 * @see java.util.ArrayDeque 以此陣列之形式呈現
	 * @param gameobject
	 *            要新增的遊戲物件
	 */
	public void addFirst(GameObject gameobject) {
		gameObjects.addFirst(gameobject);
		notifyListener(new GameEvent(this,gameobject, GameEvent.GAME_OBJECT_CHANGE));
	}

	/**
	 * 取得陣列之頭的遊戲物件，並且移除之。
	 * @return 遊戲物件
	 */
	public GameObject pollGameObject() {
		return pollFirstGameObject();
	}

	/**
	 * 取得陣列之尾的遊戲物件。
	 * @return 遊戲物件
	 */
	public GameObject peekGameObject() {
		notifyListener(new GameEvent(this,gameObjects.peekLast(),GameEvent.GAME_OBJECT_CHANGE));
		return gameObjects.peekLast();
	}
	/**
	 * 取得陣列之頭的遊戲物件，並且移除之。
	 * @return 遊戲物件
	 */
	public GameObject pollFirstGameObject() {
		GameObject tempObject = gameObjects.pollFirst();
		if (tempObject == null) {
			notifyListener(new GameEvent(this,GameEvent.GAME_OBJECT_EMPTY));
		} else {
			notifyListener(new GameEvent(this,tempObject,
					GameEvent.GAME_OBJECT_DELETE));
		}
		return tempObject;
	}

	/**
	 * 取得陣列之尾的遊戲物件，並且移除之。
	 * @return 遊戲物件
	 */
	public GameObject pollLastGameObject() {
		GameObject tempObject = gameObjects.pollLast();
		if (tempObject == null) {
			notifyListener(new GameEvent(this,GameEvent.GAME_OBJECT_EMPTY));
		} else {
			notifyListener(new GameEvent(this,tempObject,
					GameEvent.GAME_OBJECT_DELETE));
		}
		return tempObject;
	}
	
	public void clear() {
		this.gameObjects.clear();
		notifyListener(new GameEvent(this,GameEvent.GAME_OBJECT_EMPTY));
	}

	public void notifyListener(GameEvent event) {
		switch (event.getEvent()) {

		case GameEvent.GAME_OBJECT_MOVE:
			if (this.listener.onMove(event)) {
				break;
			}
		case GameEvent.GAME_OBJECT_TOUCH:
			this.listener.onTouch(event);
			break;
		case GameEvent.GAME_OBJECT_DELETE:
			this.listener.onDelete(event);
			break;
		case GameEvent.GAME_OBJECT_CHANGE:
			this.listener.onChange(event);
			break;
		case GameEvent.GAME_OBJECT_EMPTY:
			this.listener.onEmpty(event);
		default:

		}
	}

	/**
	 * 遊戲物件聆聽器
	 * 
	 * @author ET
	 */
	public interface GameObjectListener {
		
//		private GameObjectHandler listenerOwner;
//		
//		public void setOnwer(GameObjectHandler onwer) {
//			this.listenerOwner = onwer;
//		}
//		
//		public GameObjectHandler getOnwer(){
//			return this.listenerOwner;
//		}
		
		/**
		 * 當物件數改變時呼叫
		 * @param event 
		 */
		
		public void onChange(GameEvent event);

		/**
		 * 當物件數為空時呼叫
		 * @param event 
		 */
		public void onEmpty(GameEvent event);

		/**
		 * 當遊戲物件移動後呼叫
		 * @param event 
		 * 
		 * @return 當要留給onTouch()時，回傳false
		 */
		public boolean onMove(GameEvent event);

		/**
		 * 當遊戲物件被點擊後呼叫
		 * @param event 
		 */
		public void onTouch(GameEvent event);

		/**
		 * 當有遊戲物件被刪除時呼叫
		 * @param event 
		 */
		public abstract void onDelete(GameEvent event);

	}

	@Override
	public Iterator<GameObject> iterator() {
		return gameObjects.iterator();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}