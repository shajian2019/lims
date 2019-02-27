package com.zzhb.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zzhb.domain.User;
import com.zzhb.mapper.MessageMapper;
import com.zzhb.mapper.UserMapper;

@Controller
@ServerEndpoint(value = "/message/{id}")
public class MessageWebSocket {

	public static final Logger log = Logger.getLogger(MessageWebSocket.class);

	// 静态变量，用来记录当前在线连接数。线程安全的
	private volatile static int onlineCount = 0;

	// 线程安全HashMap key userid value this
	private static ConcurrentHashMap<String, MessageWebSocket> webSocketMap = new ConcurrentHashMap<>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	private String id;

	private static MessageMapper messageMapper;

	@Autowired
	public void setMessageMapper(MessageMapper messageMapper) {
		MessageWebSocket.messageMapper = messageMapper;
	}

	private static UserMapper userMapper;

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		MessageWebSocket.userMapper = userMapper;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam("id") String id, Session session) {
		this.session = session;
		this.id = id;
		webSocketMap.put(id, this); // 加入set中
		addOnlineCount(); // 在线数加1
		log.info(session.getId() + "有新连接加入！当前在线人数为" + getOnlineCount());
		try {
			User user = new User();
			user.setU_id(Integer.parseInt(id));
			user.setLogin("1");
			userMapper.updateUserLogin(user);
			sendMessage(messageMapper.countMessages(id) + "");
		} catch (IOException e) {
			log.error("websocket IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketMap.remove(this.getId()); // 从set中删除
		subOnlineCount(); // 在线数减1
		User user = new User();
		user.setU_id(Integer.parseInt(this.getId()));
		user.setLogin("0");
		userMapper.updateUserLogin(user);
		log.info("========================================" + this.getId() + "==有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		// 消息处理 id|message
		log.info(session.getId() + "来自客户端的消息:" + message);
		String[] split = message.split("#");
		String sendUserId = split[0];
		message = split[1];
		try {
			if (sendUserId.equals("0")) {
				sendtoAll(message);
			} else {
				sendtoUser(message, sendUserId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendtoUser(String message, String userId) throws IOException {
		if (webSocketMap.get(userId) != null) {
			webSocketMap.get(userId).sendMessage(message);
		} else {
			// 用户不在线
		}
	}

	public void sendtoAll(String message) throws IOException {
		for (String key : webSocketMap.keySet()) {
			try {
				webSocketMap.get(key).sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		error.printStackTrace();
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MessageWebSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		MessageWebSocket.onlineCount--;
	}
}
