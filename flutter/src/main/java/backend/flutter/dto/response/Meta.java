package backend.flutter.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
