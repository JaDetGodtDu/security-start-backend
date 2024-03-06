package dat3.security_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private Integer id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime edited;

    public CategoryDto(Category c, boolean includeAll) {
        this.id = c.getId();
        this.name = c.getName();
        if(includeAll){
            this.created = c.getCreated();
            this.edited = c.getEdited();
        }
    }
}
