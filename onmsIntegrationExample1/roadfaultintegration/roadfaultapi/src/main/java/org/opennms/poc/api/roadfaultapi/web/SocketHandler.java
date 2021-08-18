package org.opennms.poc.api.roadfaultapi.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.socket.CloseStatus;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(SocketHandler.class);

    // note this is static because it needs shared accross user sessions 
    // would have thought it was already a singleton
    private static List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void sendJsonTextMessage(Object jsonObject) {

        // serialise to socket
        ObjectMapper objectMapper = new ObjectMapper();
        String messageStr;
        try {
            messageStr = objectMapper.writeValueAsString(jsonObject);
            LOG.debug("0K SENDING "+messageStr);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("cannot serialise json object: ", ex);
        }
        
        LOG.error("SESSIONS SIZE: "+ sessions.size());

        // send to all sessions
        for (WebSocketSession webSocketSession : sessions) {
            try {
                LOG.debug("0K SENDING "+messageStr+ "to session "+webSocketSession);
                webSocketSession.sendMessage(new TextMessage(messageStr));
            } catch (Exception e) {
                LOG.error("session error - removing session:" + e.getMessage());
                sessions.remove(webSocketSession);
            }
        }
    }

    // handles incomming text messages
    // not used in this example
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

        JsonNode jsonMsg = new ObjectMapper().readTree(message.getPayload());

        //Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
        // send to all sessions
        for (WebSocketSession webSocketSession : sessions) {
            try {
                webSocketSession.sendMessage(new TextMessage("Hello " + jsonMsg.get("name") + " !"));
            } catch (Exception e) {
                LOG.error("session error - removing session:" + e.getMessage());
                sessions.remove(webSocketSession);
            }
        }
        //single messagesession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.remove(session);
    }

    @PreDestroy
    public void destroy() {
        LOG.info("shutting down  @PreDestroy ");
        for (WebSocketSession webSocketSession : sessions) {
            sessions.remove(webSocketSession);
            try {
                webSocketSession.close();
            } catch (IOException ex) {
            }
        }

    }

}
