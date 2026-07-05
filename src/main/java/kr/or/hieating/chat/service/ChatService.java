package kr.or.hieating.chat.service;

import java.util.List;
import kr.or.hieating.chat.dto.ChatMessageCreateCommand;
import kr.or.hieating.chat.dto.ChatMessageDto;
import kr.or.hieating.chat.dto.ChatRoomCreateCommand;
import kr.or.hieating.chat.dto.ChatRoomResponseDto;
import kr.or.hieating.chat.dto.ChatRoomSummaryDto;
import kr.or.hieating.chat.dto.ChatWebSocketEvent;
import kr.or.hieating.chat.mapper.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

  private static final int MAX_CONTENT_LENGTH = 1000;
  private static final String SENDER_TYPE_USER = "USER";
  private static final String SENDER_TYPE_ADMIN = "ADMIN";

  private final ChatMapper chatMapper;

  @Transactional
  public ChatRoomResponseDto getUserRoom(long userId) {
    ChatRoomSummaryDto room = findOrCreateRoom(userId);
    chatMapper.markUserRead(room.getRoomId());
    ChatRoomSummaryDto updatedRoom = requireRoom(room.getRoomId());
    return new ChatRoomResponseDto(updatedRoom, chatMapper.findMessagesByRoomId(updatedRoom.getRoomId()));
  }

  @Transactional(readOnly = true)
  public List<ChatRoomSummaryDto> findRooms() {
    return chatMapper.findRooms();
  }

  @Transactional
  public ChatRoomResponseDto getAdminRoomMessages(long roomId) {
    requireRoom(roomId);
    chatMapper.markAdminRead(roomId);
    ChatRoomSummaryDto updatedRoom = requireRoom(roomId);
    return new ChatRoomResponseDto(updatedRoom, chatMapper.findMessagesByRoomId(roomId));
  }

  @Transactional
  public ChatWebSocketEvent sendUserMessage(long userId, String rawContent) {
    String content = normalizeContent(rawContent);
    ChatRoomSummaryDto room = findOrCreateRoom(userId);
    ChatMessageDto message = saveMessage(room.getRoomId(), userId, SENDER_TYPE_USER, content);
    chatMapper.updateRoomForUserMessage(room.getRoomId());
    return ChatWebSocketEvent.message(requireRoom(room.getRoomId()), message);
  }

  @Transactional
  public ChatWebSocketEvent sendAdminMessage(long adminId, long roomId, String rawContent) {
    String content = normalizeContent(rawContent);
    requireRoom(roomId);
    ChatMessageDto message = saveMessage(roomId, adminId, SENDER_TYPE_ADMIN, content);
    chatMapper.updateRoomForAdminMessage(roomId, adminId);
    return ChatWebSocketEvent.message(requireRoom(roomId), message);
  }

  private ChatRoomSummaryDto findOrCreateRoom(long userId) {
    return chatMapper
        .findRoomByUserId(userId)
        .orElseGet(
            () -> {
              ChatRoomCreateCommand command = new ChatRoomCreateCommand(null, userId);
              chatMapper.insertRoom(command);
              if (command.getId() == null) {
                throw new IllegalStateException("Failed to create chat room.");
              }
              return requireRoom(command.getId());
            });
  }

  private ChatRoomSummaryDto requireRoom(long roomId) {
    return chatMapper
        .findRoomById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("Chat room not found."));
  }

  private ChatMessageDto saveMessage(
      long roomId, long senderId, String senderType, String content) {
    ChatMessageCreateCommand command =
        new ChatMessageCreateCommand(null, roomId, senderId, senderType, content);
    chatMapper.insertMessage(command);
    if (command.getId() == null) {
      throw new IllegalStateException("Failed to create chat message.");
    }
    return chatMapper
        .findMessageById(command.getId())
        .orElseThrow(() -> new IllegalStateException("Failed to load chat message."));
  }

  private String normalizeContent(String rawContent) {
    String content = rawContent == null ? "" : rawContent.trim();
    if (content.isEmpty()) {
      throw new IllegalArgumentException("Message content is required.");
    }
    if (content.length() > MAX_CONTENT_LENGTH) {
      throw new IllegalArgumentException("Message content is too long.");
    }
    return content;
  }
}
