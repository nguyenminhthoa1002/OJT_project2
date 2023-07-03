package project.model.dto.response;

import lombok.Data;

import javax.persistence.MappedSuperclass;
@MappedSuperclass
@Data
public abstract class RootResponse {
    protected int id;
    protected String name;
    protected int status;
}
