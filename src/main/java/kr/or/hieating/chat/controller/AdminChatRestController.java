package kr.or.hieating.chat.controller;

import java.util.List;
import kr.or.hieating.chat.dto.ChatRoomResponseDto;
import kr.or.hieating.chat.dto.ChatRoomSummaryDto;
import kr.or.hieating.chat.service.ChatService;
import kr.or.hieating.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/chats")
public class AdminChatRestController {

  private final ChatService chatService;

  @GetMapping("/rooms")
  public ApiResponse<List<ChatRoomSummaryDto>> rooms() {
    return ApiResponse.onSuccess(chatService.findRooms());
  }

  @GetMapping("/rooms/{roomId}/messages")
  public ApiResponse<ChatRoomResponseDto> messages(@PathVariable("roomId") long roomId) {
    return ApiResponse.onSuccess(chatService.getAdminRoomMessages(roomId));
  }
}
