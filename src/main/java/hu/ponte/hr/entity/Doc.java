package hu.ponte.hr.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Data
@Table(name = "doc")
public class Doc extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "sign", length = 1000)
    private String sign;

    @Column(name = "size")
    private Long size;

    /**
    You can find the file in the following folder based on Doc Id
     */
    @Column(name = "folderPath")
    private String folderPath;

    @Transient
    public String getFilePath() {
        if (folderPath == null || getId() == null || extension == null){
            return null;
        }

        return folderPath + getId()  + extension;
    }

}
