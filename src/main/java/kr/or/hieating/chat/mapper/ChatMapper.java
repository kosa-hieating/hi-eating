package kr.or.hieating.chat.mapper;

import java.util.List;
import java.util.Optional;
import kr.or.hieating.chat.dto.ChatMessageCreateCommand;
import kr.or.hieating.chat.dto.ChatMessageDto;
import kr.or.hieating.chat.dto.ChatRoomCreateCommand;
import kr.or.hieating.chat.dto.ChatRoomSummaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChatMapper {

  Optional<ChatRoomSummaryDto> findRoomByUserId(@Param("userId") long userId);

  Optional<ChatRoomSummaryDto> findRoomById(@Param("roomId") long roomId);

  List<ChatRoomSummaryDto> findRooms();

  List<ChatMessageDto> findMessagesByRoomId(@Param("roomId") long roomId);

  Optional<ChatMessageDto> findMessageById(@Param("messageId") long messageId);

  int insertRoom(ChatRoomCreateCommand command);

  int insertMessage(ChatMessageCreateCommand command);

  int updateRoomForUserMessage(@Param("roomId") long roomId);

  int updateRoomForAdminMessage(@Param("roomId") long roomId, @Param("adminId") long adminId);

  int markUserRead(@Param("roomId") long roomId);

  int markAdminRead(@Param("roomId") long roomId);
}
