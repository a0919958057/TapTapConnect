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
	 * ���o�C�����󶰦X�����`��
	 * 
	 * @return �C�����󶰦X �`��
	 */
	public static int getHandlerCounter() {
		return handlerCounter;
	}
	

	/**
	 * �]�w�C�������ť��
	 * 
	 * @param l
	 *            �C�������ť��
	 */
	public void setListener(GameObjectListener l) {
		this.listener = l;
	}

	/**
	 * ���o���t���C������ƶq
	 * 
	 * @return �C������ƶq
	 */
	public int getCount() {
		return gameObjects.size();
	}

	/**
	 * �s�W�C������ܰ}�C�����A�[�J��GameObjectHandler�̡C
	 * 
	 * @see java.util.ArrayDeque �H���}�C���Φ��e�{
	 * @param gameobject
	 *            �n�s�W���C������
	 */
	public void add(GameObject gameobject) {
		addLast(gameobject);
	}

	/**
	 * �s�W�C������ܰ}�C�����A�[�J��GameObjectHandler�̡C
	 * 
	 * @see java.util.ArrayDeque �H���}�C���Φ��e�{
	 * @param gameobject
	 *            �n�s�W���C������
	 */
	public void addLast(GameObject gameobject) {
		gameObjects.addLast(gameobject);
		notifyListener(new GameEvent(this,gameobject, GameEvent.GAME_OBJECT_CHANGE));
	}

	/**
	 * �s�W�C������ܰ}�C���Y�A�[�J��GameObjectHandler�̡C
	 * 
	 * @see java.util.ArrayDeque �H���}�C���Φ��e�{
	 * @param gameobject
	 *            �n�s�W���C������
	 */
	public void addFirst(GameObject gameobject) {
		gameObjects.addFirst(gameobject);
		notifyListener(new GameEvent(this,gameobject, GameEvent.GAME_OBJECT_CHANGE));
	}

	/**
	 * ���o�}�C���Y���C������A�åB�������C
	 * @return �C������
	 */
	public GameObject pollGameObject() {
		return pollFirstGameObject();
	}

	/**
	 * ���o�}�C�������C������C
	 * @return �C������
	 */
	public GameObject peekGameObject() {
		notifyListener(new GameEvent(this,gameObjects.peekLast(),GameEvent.GAME_OBJECT_CHANGE));
		return gameObjects.peekLast();
	}
	/**
	 * ���o�}�C���Y���C������A�åB�������C
	 * @return �C������
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
	 * ���o�}�C�������C������A�åB�������C
	 * @return �C������
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
	 * �C�������ť��
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
		 * ����Ƨ��ܮɩI�s
		 * @param event 
		 */
		
		public void onChange(GameEvent event);

		/**
		 * ����Ƭ��ŮɩI�s
		 * @param event 
		 */
		public void onEmpty(GameEvent event);

		/**
		 * ��C�����󲾰ʫ�I�s
		 * @param event 
		 * 
		 * @return ��n�d��onTouch()�ɡA�^��false
		 */
		public boolean onMove(GameEvent event);

		/**
		 * ��C������Q�I����I�s
		 * @param event 
		 */
		public void onTouch(GameEvent event);

		/**
		 * ���C������Q�R���ɩI�s
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