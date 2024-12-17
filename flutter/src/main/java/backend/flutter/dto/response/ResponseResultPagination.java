package backend.flutter.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResultPagination {
    private Meta meta;
    private Object result;
}
