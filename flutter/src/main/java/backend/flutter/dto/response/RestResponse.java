package backend.flutter.dto.response;


import lombok.Data;

@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;
}
