package kr.or.hieating.table.mapper;

import java.util.List;
import kr.or.hieating.table.dto.TableBuilderProductDto;
import kr.or.hieating.table.dto.TableCaptureCreateCommand;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableBuilderMapper {

  List<TableBuilderProductDto> findTableBuilderProducts();

  void insertPost(TableCaptureCreateCommand command);
}
